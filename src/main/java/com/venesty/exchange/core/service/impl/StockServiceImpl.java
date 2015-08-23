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

/**
 * {@link StockService} implementation.
 * 
 * Maintains two {@link List}s, open orders and executed orders.
 * If a new {@link Order} is matched according to this implementation then it will be added
 * to the executed list. Else it will remain in the open orders list.
 * 
 * N.B: There should be a mechanism in place to deal with long standing open order.
 * Either they are removed after a n number of new orders are unmatched or use some timestamp
 * stategy and let the Order expire etc.
 * 
 * @author vikash
 *
 */
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
        notifyDataHandler(order);
    }

    public Integer getExecutedQuantityFor(String ric, String user) {
        Iterable<Integer> quantities = Iterables.transform(
                        Iterables.filter(getExecutedOrders(),
                                        Predicates.and(new RicMatcher(ric), new UserMatcher(user))),
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

    public Double getAverageExecutedPriceFor(String ric) {
        Iterable<Order> orders = Iterables.filter(getExecutedOrders(), new RicMatcher(ric));
        Double sum = 0.0;
        for (Order anOrder : orders) {
            sum += anOrder.getExecutionPrice();
        }
        return sum / Iterables.size(orders);
    }

    public Map<Double, Integer> getOpenInterestFor(String ric, Direction direction) {
        Map<Double, Integer> map = Maps.newHashMap();
        Map<Double, Collection<Order>> openInterestOrders = Multimaps.index(
                        Iterables.filter(
                                        getOpenOrders(),
                                        Predicates.and(new RicMatcher(ric),
                                                        Predicates.not(new OpposingDirectionMatcher(direction)))),
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

    /**
     * Provide an ImmutableList copy of openOrders.
     * 
     * @return {@link Iterable} of open order.
     */
    protected Iterable<Order> getOpenOrders() {
        return ImmutableList.copyOf(this.openOrders);
    }

    /**
     * Provide an {@link ImmutableList} copy of executed orders.
     * 
     * @return {@link Iterable} of executed orders.
     */
    protected Iterable<Order> getExecutedOrders() {
        return ImmutableList.copyOf(this.executedOrders);
    }
    
    /*
     * Looks up a mathing Order's index.
     */
    private int findIndex(Order newOrder) {
        int index = Iterables.indexOf(this.openOrders, Predicates.and(new RicMatcher(newOrder.getRic()),
                        new OpposingDirectionMatcher(newOrder.getDirection()), new QuantityMatcher(newOrder.getQuantity()),
                        new SellPriceMatcher(newOrder.getPrice())));
        return index;
    }

    /*
     * Notify handler of newly added Order.
     */
    private void notifyDataHandler(Order order) {
        summaryHandler.handleNewOrder(order);
        summaryHandler.handleAverageExecutionPriceFor(getAverageExecutedPriceFor(order.getRic()), order.getRic());
        summaryHandler.handleExecutedQuantityFor(getExecutedQuantityFor(order.getRic(), order.getUser()), order.getRic(), order.getUser());
        Map<Double, Integer> openInterest = getOpenInterestFor(order.getRic(), order.getDirection());
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
