package com.venesty.exchange.core.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.venesty.exchange.core.service.impl.StockServiceImpl;
import com.venesty.exchange.model.Order;
import com.venesty.exchange.model.Order.Direction;
import com.venesty.exchange.util.OrderMother;

public class StockServiceTest {

    private StockService<Order> service;

    @Mock
    private Order order;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        service = new TestStockService();
        when(order.getRic()).thenReturn("VOD.L");
        when(order.getUser()).thenReturn("user1");
        when(order.getDirection()).thenReturn(Direction.SELL);
    }

    @Test
    public void testAverageExecutedOrder() {
        Double averageExecutedOrderFor = service.getAverageExecutedPriceFor(order.getRic());
        assertThat(averageExecutedOrderFor, equalTo(100.6));
    }

    @Test
    public void testExecuteQuantityFor() {
        Integer executedQuantityFor = service.getExecutedQuantityFor(order.getRic(), order.getUser());
        assertThat(executedQuantityFor, equalTo(-500));
    }

    @Test
    public void testOpenInterestFor() {
        Map<Double, Integer> openInterestFor = service.getOpenInterestFor(order.getRic(), order.getDirection());

        Map<Double, Integer> map1 = Maps.newHashMap(ImmutableMap.of(Double.valueOf(102.00), Integer.valueOf(1000),
                        Double.valueOf(101.00), Integer.valueOf(1000), Double.valueOf(100.00), Integer.valueOf(500)));
        assertThat(openInterestFor, equalTo(map1));
    }

    private static class TestStockService extends StockServiceImpl {
        @Override
        protected Iterable<Order> getOpenOrders() {
            return OrderMother.getOpenOrders();
        }

        @Override
        protected Iterable<Order> getExecutedOrders() {
            return OrderMother.getExecutedOrders();
        }
    }
}
