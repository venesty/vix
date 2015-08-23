package com.venesty.exchange.core.processor.impl;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.venesty.exchange.core.exception.OrderProcessorException;
import com.venesty.exchange.core.exception.ProcessorException;
import com.venesty.exchange.core.processor.OrderProcessor;
import com.venesty.exchange.core.service.StockService;
import com.venesty.exchange.core.service.impl.StockServiceImpl;
import com.venesty.exchange.model.Order;
import com.venesty.exchange.model.OrderValidator;

/**
 * Simple implementation of {@link OrderProcessor}
 * 
 * Runs asynchronously using a buffer as a queue system where orders can be added
 * and queued up for later processing.
 * 
 * For the actual handling of the {@link Order}, see {@link StockServiceImpl}
 * 
 * @author vikash
 *
 */
public class OrderProcessorImpl implements OrderProcessor {
    private static Logger LOG = LoggerFactory.getLogger(OrderProcessorImpl.class);

    private static final int DEFAULT_BUFFER_SIZE = 100;


    private static final long TIME_OUT = 100;

    private AtomicBoolean started = null;

    private static ExecutorService executorService;

    private Future<Void> future;

    private final BlockingQueue<Order> orderBuffer;

    private int bufferSize = DEFAULT_BUFFER_SIZE;

    private List<StockService<Order>> stockServices;
    
    private OrderValidator validator;

    public OrderProcessorImpl() {
        this.orderBuffer = new LinkedBlockingQueue<Order>(bufferSize);
        this.started = new AtomicBoolean(true);
    }

    public void startProcessor() {
        LOG.info("OrderProcessor starting...");
        if (started.get()) {
            try {
            	future = executorService.submit(getOrderConsumer());
            } catch (RejectedExecutionException ree) {
            	LOG.error("Unable to start processesor", ree);
            	throw new ProcessorException("Unable to start processor", ree);
            }
        }
        LOG.info("...OrderProcessor started.");
    }

    public void addNewOrder(Order order) throws ProcessorException, OrderProcessorException {
    	
    	if (future == null) {
    		throw new ProcessorException("Cannot add new Order: " + order + " , processor is not started");
    	}
    	
    	validator.validate(order);
    	
    	try {
            while (!orderBuffer.offer(order, TIME_OUT, TimeUnit.MILLISECONDS)) {
                sleep(50);
            }
        } catch (Exception e) {
        	LOG.error("An error occurred whilst adding the new order: " + order);
        	throw new ProcessorException("An error occurred adding the new order");
        }
    }

    public void awaitCompletion() throws ProcessorException {
        LOG.info("OrderProcessor stopping...");
        this.started.compareAndSet(true, false);
    	try {
            this.future.get();
        } catch (Exception e) {
            throw new ProcessorException("Error occured while shutting down the OrderProcessor.", e);
        } finally {
            this.orderBuffer.clear();
            LOG.info("Order queue has been cleared");
        }
        LOG.info("... OrderProcesspr stopped.");
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Callable<Void> getOrderConsumer() {
        return new Callable<Void>() {

            private boolean hasOrders() {
                return started.get() || !orderBuffer.isEmpty();
            }

            public Void call() throws Exception {
                while (hasOrders()) {
                    Order order = orderBuffer.poll(1000, TimeUnit.MILLISECONDS);
                    if (order != null) {
                        for (StockService<Order> service : stockServices) {
                            service.process(order);
                        }
                    }
                }
                return null;
            }
        };
    }
    

    @Required
    public void setStockServices(List<StockService<Order>> stockServices) {
        this.stockServices = stockServices;
    }

    @Required
    public void setExecutorService(ExecutorService executorService) {
        OrderProcessorImpl.executorService = executorService;
    }
    
    @Required
    public void setValidator(OrderValidator validator) {
		this.validator = validator;
	}

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

}
