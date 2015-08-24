package com.venesty.exchange.core.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import utils.PriceUtil;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.venesty.exchange.core.service.impl.StockServiceImpl;
import com.venesty.exchange.model.Order;
import com.venesty.exchange.model.Order.Direction;
import com.venesty.exchange.util.OrderMother;

public class StockServiceTest {
	
	private static final String RIC = "VOD.L";
	private static final String USER = "user1";

    private StockServiceImpl service;
    
    @Mock
    private StockSummaryHandler summaryHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        service = new StockServiceImpl();
        service.setSummaryHandler(summaryHandler);
        
        for(Order order : OrderMother.getMatchingOrders()) {
        	service.process(order);
        }
        
        for (Order order : OrderMother.getUnMatchedOpenOrders()) {
            service.process(order);
        }
    }
    
    @Test
    public void testMatchForExactPriceBuyByOrder() {
    	
        // 3 similar sell orders, oldest by user3, user4, user5

        // before new buy order is added.
        assertThat(service.getExecutedQuantityFor("APP.L", "user3"), equalTo(-500));
        assertThat(service.getExecutedQuantityFor("APP.L", "user4"), equalTo(500));
        assertThat(service.getExecutedQuantityFor("APP.L", "user5"), equalTo(1000));

        Order order1 = new Order("APP.L", 1000, "102.00", Direction.BUY, "user6");

        service.process(order1);

        assertThat(service.getExecutedQuantityFor("APP.L", "user3"), equalTo(-1500));
        assertThat(service.getExecutedQuantityFor("APP.L", "user4"), equalTo(500)); // should still be 1000
        assertThat(service.getExecutedQuantityFor("APP.L", "user5"), equalTo(1000)); // should still be 1000

        service.process(order1);

        assertThat(service.getExecutedQuantityFor("APP.L", "user3"), equalTo(-1500)); // should still be 1000
        assertThat(service.getExecutedQuantityFor("APP.L", "user4"), equalTo(-500));
        assertThat(service.getExecutedQuantityFor("APP.L", "user5"), equalTo(1000)); // should still be 1000

        service.process(order1);

        assertThat(service.getExecutedQuantityFor("APP.L", "user3"), equalTo(-1500)); // should still be 1000
        assertThat(service.getExecutedQuantityFor("APP.L", "user4"), equalTo(-500)); // should still be -500
        assertThat(service.getExecutedQuantityFor("APP.L", "user5"), equalTo(0)); // should still be 1000
    }

    @Test
    public void testMatchForExactPriceSellByOrder() {

        // 3 similar buy orders, oldest by user12, user14, user15

        // before new buy order is added.
        assertThat(service.getExecutedQuantityFor("APP.L", "user13"), equalTo(0));
        assertThat(service.getExecutedQuantityFor("APP.L", "user14"), equalTo(0));
        assertThat(service.getExecutedQuantityFor("APP.L", "user15"), equalTo(0));

        Order order1 = new Order("APP.L", 2000, "100.00", Direction.SELL, "user6");

        service.process(order1);

        assertThat(service.getExecutedQuantityFor("APP.L", "user13"), equalTo(2000)); // match
        assertThat(service.getExecutedQuantityFor("APP.L", "user14"), equalTo(0));
        assertThat(service.getExecutedQuantityFor("APP.L", "user15"), equalTo(0));

        service.process(order1);

        assertThat(service.getExecutedQuantityFor("APP.L", "user13"), equalTo(2000));
        assertThat(service.getExecutedQuantityFor("APP.L", "user14"), equalTo(2000)); // match
        assertThat(service.getExecutedQuantityFor("APP.L", "user15"), equalTo(0));

        service.process(order1);

        assertThat(service.getExecutedQuantityFor("APP.L", "user13"), equalTo(2000));
        assertThat(service.getExecutedQuantityFor("APP.L", "user14"), equalTo(2000));
        assertThat(service.getExecutedQuantityFor("APP.L", "user15"), equalTo(2000)); // match
    }

    @Test
    public void testMatchForSellOrder() {
        // 4 similar buy orders for VOD.L 1000 priced at 97.00-user3, 96.00-user4, 99.0-user5, 98.00-user6

        // before new buy order is added.
        assertThat(service.getExecutedQuantityFor("VOD.L", "user3"), equalTo(1500));
        assertThat(service.getExecutedQuantityFor("VOD.L", "user4"), equalTo(0));
        assertThat(service.getExecutedQuantityFor("VOD.L", "user5"), equalTo(0));
        assertThat(service.getExecutedQuantityFor("VOD.L", "user6"), equalTo(0));

        Order order = new Order("VOD.L", 1000, "95.00", Direction.SELL, "user7");

        service.process(order);

        assertThat(service.getExecutedQuantityFor("VOD.L", "user3"), equalTo(1500));
        assertThat(service.getExecutedQuantityFor("VOD.L", "user4"), equalTo(0));
        assertThat(service.getExecutedQuantityFor("VOD.L", "user5"), equalTo(1000)); // change expected
        assertThat(service.getExecutedQuantityFor("VOD.L", "user6"), equalTo(0));

        service.process(order);

        assertThat(service.getExecutedQuantityFor("VOD.L", "user3"), equalTo(1500));
        assertThat(service.getExecutedQuantityFor("VOD.L", "user4"), equalTo(0));
        assertThat(service.getExecutedQuantityFor("VOD.L", "user5"), equalTo(1000));
        assertThat(service.getExecutedQuantityFor("VOD.L", "user6"), equalTo(1000)); // change expected

        service.process(order);

        assertThat(service.getExecutedQuantityFor("VOD.L", "user3"), equalTo(2500)); // change expected
        assertThat(service.getExecutedQuantityFor("VOD.L", "user4"), equalTo(0));
        assertThat(service.getExecutedQuantityFor("VOD.L", "user5"), equalTo(1000));
        assertThat(service.getExecutedQuantityFor("VOD.L", "user6"), equalTo(1000));

        service.process(order);

        assertThat(service.getExecutedQuantityFor("VOD.L", "user3"), equalTo(2500));
        assertThat(service.getExecutedQuantityFor("VOD.L", "user4"), equalTo(1000)); // change expected
        assertThat(service.getExecutedQuantityFor("VOD.L", "user5"), equalTo(1000));
        assertThat(service.getExecutedQuantityFor("VOD.L", "user6"), equalTo(1000));

    }

    @Test
    public void testMatchForBuyOrder() {
        // 4 similar sell orders, APP.L qty=1500 priced at 99.00-user8, 96.00-user9, 98.00-user10, 100.0-user11

        // before new buy order is added.
        assertThat(service.getExecutedQuantityFor("APP.L", "user8"), equalTo(0));
        assertThat(service.getExecutedQuantityFor("APP.L", "user9"), equalTo(0));
        assertThat(service.getExecutedQuantityFor("APP.L", "user10"), equalTo(0));
        assertThat(service.getExecutedQuantityFor("APP.L", "user11"), equalTo(0));
        
        Order order = new Order("APP.L", 1500, "101.00", Direction.BUY, "user12");
        service.process(order);

        assertThat(service.getExecutedQuantityFor("APP.L", "user8"), equalTo(0));
        assertThat(service.getExecutedQuantityFor("APP.L", "user9"), equalTo(-1500));
        assertThat(service.getExecutedQuantityFor("APP.L", "user10"), equalTo(0));
        assertThat(service.getExecutedQuantityFor("APP.L", "user11"), equalTo(0));

        service.process(order);

        assertThat(service.getExecutedQuantityFor("APP.L", "user8"), equalTo(0));
        assertThat(service.getExecutedQuantityFor("APP.L", "user9"), equalTo(-1500));
        assertThat(service.getExecutedQuantityFor("APP.L", "user10"), equalTo(-1500));
        assertThat(service.getExecutedQuantityFor("APP.L", "user11"), equalTo(0));

        service.process(order);

        assertThat(service.getExecutedQuantityFor("APP.L", "user8"), equalTo(-1500));
        assertThat(service.getExecutedQuantityFor("APP.L", "user9"), equalTo(-1500));
        assertThat(service.getExecutedQuantityFor("APP.L", "user10"), equalTo(-1500));
        assertThat(service.getExecutedQuantityFor("APP.L", "user11"), equalTo(0));

        service.process(order);

        assertThat(service.getExecutedQuantityFor("APP.L", "user8"), equalTo(-1500));
        assertThat(service.getExecutedQuantityFor("APP.L", "user9"), equalTo(-1500));
        assertThat(service.getExecutedQuantityFor("APP.L", "user10"), equalTo(-1500));
        assertThat(service.getExecutedQuantityFor("APP.L", "user11"), equalTo(-1500));

    }

    @Test
    public void testAverageExecutedOrder() {
        BigDecimal averageExecutedOrderFor = service.getAverageExecutedPriceFor(RIC);
        assertThat(averageExecutedOrderFor, equalTo(PriceUtil.roundUp("100.60")));
    }

    @Test
    public void testExecuteQuantityFor() {
        Integer executedQuantityFor = service.getExecutedQuantityFor(RIC, USER);
        assertThat(executedQuantityFor, equalTo(-500));
    }

    @Test
    public void testOpenInterestFor() {
        Map<BigDecimal, Integer> openInterestFor = service.getOpenInterestFor(RIC, Direction.SELL);

        Map<BigDecimal, Integer> map1 = Maps.newHashMap(ImmutableMap.of(PriceUtil.roundUp(102.00), Integer.valueOf(1000),
                        PriceUtil.roundUp(100.00), Integer.valueOf(500), PriceUtil.roundUp(101.00), Integer.valueOf(1000)));
        assertThat(openInterestFor, equalTo(map1));
    }
    
    @After
    public void tearDown() {
    	service = null;
    }
}
