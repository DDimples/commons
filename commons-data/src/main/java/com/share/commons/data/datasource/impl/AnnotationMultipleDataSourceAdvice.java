package com.share.commons.data.datasource.impl;

import com.share.commons.data.datasource.AbstractDataSourceAdvice;
import com.share.commons.data.datasource.DataSourceSwitcher;
import com.share.commons.data.datasource.annotation.DataSource;

import java.lang.reflect.Method;



public class AnnotationMultipleDataSourceAdvice extends AbstractDataSourceAdvice{
    public void before(Method method, Object[] args, Object target) throws Throwable {
        DataSource dataSource = method.getAnnotation(DataSource.class);
        //System.out.println("--------method="+method.getName()+";dataSource="+(dataSource!=null ? dataSource.value():null));
        String datasourceKey=dataSource!=null ? dataSource.value():null;
        DataSourceSwitcher.setDataSource(datasourceKey);
    }

    public void afterReturning(Object arg0, Method method, Object[] args, Object target) throws Throwable {
        DataSourceSwitcher.clearDataSource();
    }

    public void afterThrowing(Method method, Object[] args, Object target, Exception ex) throws Throwable {
        DataSourceSwitcher.clearDataSource();
    }
}
  