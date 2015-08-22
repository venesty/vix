/*
 * Copyright 2013 Deutsche Bank. All rights reserved.
 * 
 * This software is the confidential and proprietary information of Deutsche Bank. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in accordance with the terms of the license agreement you entered
 * into with Deutsche Bank.
 */

package com.venesty.exchange.core.processor.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.venesty.exchange.core.processor.OrderProcessor;
import com.venesty.exchange.model.Order;
import com.venesty.exchange.util.OrderMother;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context-exchange.xml" })
public class OrderProcessorTest {

    @Autowired
    private OrderProcessor orderProcessor;

    @Before
    public void setUp() {
        this.orderProcessor.startProcessor();
    }

    @Test
    public void testMatches() {
        
        OrderMother.getOpenOrders();
        for (Order order : OrderMother.getOpenOrders()) {
        	orderProcessor.addNewOrder(order);
        }
    }

    @After
    public void tearDown() {
        this.orderProcessor.awaitCompletion();
    }

}
