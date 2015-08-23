package com.venesty.exchange.core.processor.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.venesty.exchange.core.exception.ProcessorException;
import com.venesty.exchange.core.processor.OrderProcessor;
import com.venesty.exchange.integration.IntegrationTest;
import com.venesty.exchange.model.Order;
import com.venesty.exchange.util.OrderMother;
import com.venesty.exchange.util.TestStockService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context-exchange-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD )
@Category(IntegrationTest.class)
public class OrderProcessorTest {

    @Autowired
    private OrderProcessor orderProcessor;
    
    @Autowired
    private TestStockService stockService;
    
    
    @Test(expected = ProcessorException.class)
    public void testOrderProcessorNotStarted() {
    	for (Order order : OrderMother.getOpenOrders()) {
    		orderProcessor.addNewOrder(order);
    	}
    	assertThat("OrderProcessor not started.", false);
    }
    
    @Test(expected = ProcessorException.class)
    public void testOrderProcessingNotStartedAndAwaitCompletionCalled() {
    	orderProcessor.awaitCompletion();
    	assertThat("OrderProcessor not started.", false);
    }
    

    @Test
    public void testValidOrders() {
    	
    	orderProcessor.startProcessor();
    	
        for (Order order : OrderMother.getOpenOrders()) {
        	orderProcessor.addNewOrder(order);
        }
        
        // Need to await completion, else only partial orders may have been processed.
        orderProcessor.awaitCompletion();
        
        assertThat(stockService.currentOpenOrdersSize(), equalTo(6));
        assertThat(stockService.currentExecutedOrdersSize(), equalTo(2));
        
    }
    
    @Test
    public void testInvalidOrders() {
    	
    	orderProcessor.startProcessor();
    	
    	for (Order order : OrderMother.getInvalidOpenOrders()) {
    		orderProcessor.addNewOrder(order);
    	}

    	// Need to await completion, else only partial orders may have been processed.
    	orderProcessor.awaitCompletion();
    	
    	assertThat(stockService.currentExecutedOrdersSize(), equalTo(0));
    	assertThat(stockService.currentOpenOrdersSize(), equalTo(0));
    	
    }
    
    @Test
    public void testLargeInvalidAndValidOrders() {
    	orderProcessor.startProcessor();
    	
    	for (Order order : OrderMother.getLargeValidAndInvalidOrders()) {
    		orderProcessor.addNewOrder(order);
    	}
    	
    	orderProcessor.awaitCompletion();
    	
    	assertThat(stockService.currentOpenOrdersSize(), equalTo(100));
    	assertThat(stockService.currentExecutedOrdersSize(), equalTo(100));
    }

}
