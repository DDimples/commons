package com.share.commons.data.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {
    private String defaultDataSourceKey;
    
    @Override
    protected Object determineCurrentLookupKey() {
        String dataSource = DataSourceSwitcher.getDataSource();
        if(dataSource != null){
            return dataSource;
        }
        return defaultDataSourceKey;
    }

    public String getDefaultDataSourceKey() {
        return defaultDataSourceKey;
    }

    public void setDefaultDataSourceKey(String defaultDataSourceKey) {
        this.defaultDataSourceKey = defaultDataSourceKey;
    }
}
