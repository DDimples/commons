package com.share.commons.data.datasource;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

public abstract class AbstractDataSourceAdvice implements MethodBeforeAdvice, AfterReturningAdvice,ThrowsAdvice{
    public void afterReturning(Object arg0, Method method, Object[] args, Object target) throws Throwable {
        DataSourceSwitcher.clearDataSource();
    }

    public void afterThrowing(Method method, Object[] args, Object target, Exception ex) throws Throwable {
        DataSourceSwitcher.clearDataSource();
    }
}

