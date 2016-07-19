package com.share.commons.data.datasource.impl;

import com.share.commons.data.datasource.AbstractDataSourceAdvice;
import com.share.commons.data.datasource.DataSourceSwitcher;

import java.lang.reflect.Method;


public class SingleMasterSlaveDataSourceAdvice extends AbstractDataSourceAdvice {

    private String masterDataSource;
    private String slaveDataSource;

    public String getMasterDataSource() {
        return masterDataSource;
    }

    public void setMasterDataSource(String masterDataSource) {
        this.masterDataSource = masterDataSource;
    }

    public String getSlaveDataSource() {
        return slaveDataSource;
    }

    public void setSlaveDataSource(String slaveDataSource) {
        this.slaveDataSource = slaveDataSource;
    }


    public void before(Method method, Object[] args, Object target) throws Throwable {
        String methodName = method.getName().toLowerCase();
        if (methodName.startsWith("add")
                || methodName.startsWith("create")
                || methodName.startsWith("save")
                || methodName.startsWith("edit")
                || methodName.startsWith("update")
                || methodName.startsWith("delete")
                || methodName.startsWith("remove")) {

            DataSourceSwitcher.setDataSource(this.masterDataSource);
        } else {
            DataSourceSwitcher.setDataSource(this.slaveDataSource);
        }
    }


}
