package com.employee.service.app.Listener;

import com.employee.service.app.service.RetryCountService;
import org.springframework.core.annotation.Order;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.stereotype.Component;

@Component
public class RetryCountListener implements RetryListener {

    @Order(1)
    private final RetryCountService retryCountService;

    public RetryCountListener(RetryCountService retryCountService) {
        this.retryCountService = retryCountService;
    }

    public  <T, E extends Throwable> void onSuccess(RetryContext context, RetryCallback<T, E> callback, T result) {
        retryCountService.resetRetryCount();
        System.out.println("onSuccess");
    }

    public  <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        int retryCount = retryCountService.incrementRetryCount();
        System.out.println("onError");
        if (retryCount >= 3) {
            retryCountService.resetRetryCount();
            throw new IllegalStateException("Retry count exceeded. Aborting retry.");
        }
    }
}

