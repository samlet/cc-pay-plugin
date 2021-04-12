package com.bluecc.pay;

import java.util.Map;

public class ServiceFail extends Exception {
    public ServiceFail(String message, Map<String, Object> serviceResult) {
        super(message);
        this.serviceResult = serviceResult;
    }

    public Map<String, Object> getServiceResult() {
        return serviceResult;
    }

    private final Map<String, Object> serviceResult;
}
