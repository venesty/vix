/*
 * Copyright 2013 Deutsche Bank. All rights reserved.
 * 
 * This software is the confidential and proprietary information of Deutsche Bank. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in accordance with the terms of the license agreement you entered
 * into with Deutsche Bank.
 */

package com.venesty.exchange.core.exception;

public class ProcessException extends RuntimeException {

    public ProcessException(String message) {
        super(message);
    }

    public ProcessException(Throwable cause) {
        super(cause);
    }

}
