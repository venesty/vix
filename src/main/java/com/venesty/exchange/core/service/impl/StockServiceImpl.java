package com.venesty.exchange.core.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import utils.PriceUtil;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;
import com.venesty.exchange.core.matchers.ExactPriceMatcher;
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

    public synchronized void process(Order newOrder) {
        LOG.info("Processing new order" + newOrder);
        Order matchedOrder = null;
        Iterable<Order> openOrders = getOpenOrders();
        int index = Iterables.indexOf(openOrders, Predicates.and(new RicMatcher(newOrder.getRic()),
                        new OpposingDirectionMatcher(newOrder.getDirection()), new QuantityMatcher(newOrder.getQuantity()),
                        new ExactPriceMatcher(newOrder.getPrice())));

        if (index < 0) {
            List<Order> sortedCopy = null;
            // no Exact price match. So we look for sell price match
            Iterable<Order> sellPriceMatchOpenOrders = Iterables.filter(openOrders, Predicates.and(
                            new RicMatcher(newOrder.getRic()), new OpposingDirectionMatcher(newOrder.getDirection()),
                            new QuantityMatcher(newOrder.getQuantity()), new SellPriceMatcher(newOrder.getPrice())));
            if (newOrder.getDirection().equals(Direction.SELL)) {
                Ordering<Order> byHighestPrice = Ordering.natural().reverse().onResultOf(new Function<Order, BigDecimal>() {

                    public BigDecimal apply(Order input) {
                        return input.getPrice();
                    }
                });
                sortedCopy = byHighestPrice.sortedCopy(sellPriceMatchOpenOrders);
            } else {
                Ordering<Order> byLowestPrice = Ordering.natural().onResultOf(new Function<Order, BigDecimal>() {

                    public BigDecimal apply(Order input) {
                        return input.getPrice();
                    }
                });
                sortedCopy = byLowestPrice.sortedCopy(sellPriceMatchOpenOrders);
            }

            matchedOrder = sortedCopy.isEmpty() ? null : sortedCopy.get(0);
            if (matchedOrder != null) {
                this.openOrders.remove(matchedOrder);
            }
        } else {
            matchedOrder = this.openOrders.remove(index);
        }

        if (matchedOrder != null) {
            matchedOrder.setExecutionPrice(newOrder.getPrice());
            newOrder.setExecutionPrice(newOrder.getPrice());
            this.executedOrders.add(matchedOrder);
            this.executedOrders.add(newOrder);
        } else {
            this.openOrders.add(newOrder);
        }
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

    public BigDecimal getAverageExecutedPriceFor(String ric) {
        Iterable<Order> orders = Iterables.filter(getExecutedOrders(), new RicMatcher(ric));
        BigDecimal sum = PriceUtil.roundUp(0.00);
        for (Order anOrder : orders) {
            sum = sum.add(anOrder.getExecutionPrice());
        }
        BigDecimal orderSize = new BigDecimal(Iterables.size(orders));
        orderSize = orderSize.setScale(2, RoundingMode.UP);
        return sum.doubleValue() > 0.0 ? sum.divide(orderSize, 0) : null;
    }

    public Map<BigDecimal, Integer> getOpenInterestFor(String ric, Direction direction) {
        Map<BigDecimal, Integer> map = Maps.newHashMap();
        Map<BigDecimal, Collection<Order>> openInterestOrders = Multimaps.index(
                        Iterables.filter(
                                        getOpenOrders(),
                                        Predicates.and(new RicMatcher(ric),
                                                        Predicates.not(new OpposingDirectionMatcher(direction)))),
                        new Function<Order, BigDecimal>() {
                            public BigDecimal apply(Order input) {
                                return input.getPrice();
                            }
                        }).asMap();
        Set<BigDecimal> keySet = openInterestOrders.keySet();
        for (BigDecimal key : keySet) {
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
     * Notify handler of newly added Order.
     */
    private void notifyDataHandler(Order order) {
        if (order != null) {
            summaryHandler.handleNewOrder(order);
            // summaryHandler.handleAverageExecutionPriceFor(getAverageExecutedPriceFor(order.getRic()), order.getRic());
            summaryHandler.handleExecutedQuantityFor(getExecutedQuantityFor(order.getRic(), order.getUser()), order.getRic(),
                            order.getUser());
            Map<BigDecimal, Integer> openInterest = getOpenInterestFor(order.getRic(), order.getDirection());
            BigDecimal key = null;
            Integer totQty = null;
            if (!openInterest.keySet().isEmpty()) {
                key = Lists.newArrayList(openInterest.keySet()).get(0);
                totQty = openInterest.get(key);
            }

            summaryHandler.handleOpenInterestFor(totQty, order.getRic(), order.getDirection(), key);
        }
    }

    @Required
    public void setSummaryHandler(StockSummaryHandler summaryHandler) {
        this.summaryHandler = summaryHandler;
    }
}
