package com.example.distance.service;


import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RequestCounterService {

    private static final AtomicInteger requestCount = new AtomicInteger(0);
    private RequestCounterService(){

    }

    public static synchronized void incrementCount() {
        requestCount.incrementAndGet();
    }

    public static synchronized int getCount() {
        return requestCount.get();
    }
}
