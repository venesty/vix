package com.venesty.exchange.core.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.venesty.exchange.core.matchers.OpposingDirectionMatcher;
import com.venesty.exchange.core.matchers.QuantityMatcher;
import com.venesty.exchange.core.matchers.RicMatcher;
import com.venesty.exchange.core.matchers.SellPriceMatcher;
import com.venesty.exchange.core.matchers.UserMatcher;
import com.venesty.exchange.core.service.StockService;
import com.venesty.exchange.core.service.StockSummaryHandler;
import com.venesty.exchange.model.Order;
import com.venesty.exchange.model.Order.Direction;

public class StockServiceImpl implements StockService<Order> {
    private static Logger LOG = LoggerFactory.getLogger(StockServiceImpl.class);

    private List<Order> openOrders = Lists.newArrayList();

    private List<Order> executedOrders = Lists.newArrayList();
    
    private StockSummaryHandler summaryHandler;

    public StockServiceImpl() {
    }

    public void process(Order newOrder) {
        LOG.info("Processing new order" + newOrder);
        Order order = newOrder;
        int index = findIndex(newOrder);
        if (index > -1) {
            order = openOrders.remove(index);
            order.setExecutionPrice(newOrder.getPrice());
            executedOrders.add(order);
        } else {
            openOrders.add(order);
        }
        notifyDataConsumers(order);
    }

    public Integer getExecutedQuantityFor(Order order) {
        Iterable<Integer> quantities = Iterables.transform(
                        Iterables.filter(getExecutedOrders(),
                                        Predicates.and(new RicMatcher(order.getRic()), new UserMatcher(order.getUser()))),
                        new Function<Order, Integer>() {
                            public Integer apply(Order input) {
                                if (input.getDirection().equals(Direction.BUY)) {
                                    return input.getQuantity();
                                }
                                return -input.getQuantity();
                            }
                        });
        Integer qty = 0;
        for (Integer vol : quantities) {
            qty += vol;
        }
        return qty;
    }

    public Double getAverageExecutedOrderFor(Order order) {
        Iterable<Order> orders = Iterables.filter(getExecutedOrders(), new RicMatcher(order.getRic()));
        Double sum = 0.0;
        for (Order anOrder : orders) {
            sum += anOrder.getExecutionPrice();
        }
        return sum / Iterables.size(orders);
    }

    public Map<Double, Integer> getOpenInterestFor(Order value) {
        Map<Double, Integer> map = Maps.newHashMap();
        Map<Double, Collection<Order>> openInterestOrders = Multimaps.index(
                        Iterables.filter(
                                        getOpenOrders(),
                                        Predicates.and(new RicMatcher(value.getRic()),
                                                        Predicates.not(new OpposingDirectionMatcher(value.getDirection())))),
                        new Function<Order, Double>() {
                            public Double apply(Order input) {
                                return new Double(input.getPrice());
                            }
                        }).asMap();
        Set<Double> keySet = openInterestOrders.keySet();
        for (Double key : keySet) {
            Collection<Order> collection = openInterestOrders.get(key);
            int qty = 0;
            for (Order order : collection) {
                qty += order.getQuantity();
            }

            map.put(key, Integer.valueOf(qty));
        }
        return map;
    }

    protected Iterable<Order> getOpenOrders() {
        return ImmutableList.copyOf(this.openOrders);
    }

    protected Iterable<Order> getExecutedOrders() {
        return ImmutableList.copyOf(this.executedOrders);
    }
    
    private int findIndex(Order newOrder) {
        int index = Iterables.indexOf(this.openOrders, Predicates.and(new RicMatcher(newOrder.getRic()),
                        new OpposingDirectionMatcher(newOrder.getDirection()), new QuantityMatcher(newOrder.getQuantity()),
                        new SellPriceMatcher(newOrder.getPrice())));
        return index;
    }

    private void notifyDataConsumers(Order order) {
        summaryHandler.handleNewOrder(order);
        summaryHandler.handleAverageExecutionPriceFor(getAverageExecutedOrderFor(order), order.getRic());
        summaryHandler.handleExecutedQuantityFor(getExecutedQuantityFor(order), order.getRic(), order.getUser());
        Map<Double, Integer> openInterest = getOpenInterestFor(order);
        Double key = null;
        Integer totQty = null;
        if (!openInterest.keySet().isEmpty()) {
            key = Lists.newArrayList(openInterest.keySet()).get(0);
            totQty = openInterest.get(key);
        }

        summaryHandler.handleOpenInterestFor(totQty, order.getRic(), order.getDirection(), key);
    }

    @Required
    public void setSummaryHandler(StockSummaryHandler summaryHandler) {
        this.summaryHandler = summaryHandler;
    }
}
