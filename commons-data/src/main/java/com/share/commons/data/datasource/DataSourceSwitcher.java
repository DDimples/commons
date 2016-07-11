package com.share.commons.data.datasource;


import com.share.commons.util.StringUtil;

public class DataSourceSwitcher {
//    private final static Logger log = LogUtil.getApplicationLogger();
    
    @SuppressWarnings("rawtypes")
    private static final ThreadLocal contextHolder = new ThreadLocal();

    @SuppressWarnings("unchecked")
    public static void setDataSource(String dataSource) {
        if(StringUtil.isNotEmpty(dataSource)){
            //log.info("Setting datasource to:" + dataSource);
        		contextHolder.set(dataSource);
        }
    }
    
    public static String getDataSource() {
        String currDataSource = (String) contextHolder.get();
        //log.info("Current datasource is:" + currDataSource);
        return currDataSource;
    }

    public static void clearDataSource() {
        contextHolder.remove();
    }
}
