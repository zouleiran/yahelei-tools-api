package com.yahelei.utils;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class CustomerContextHolder extends AbstractRoutingDataSource {

    public static final String  DATA_SOURCE_PROD_HR= "dataSourceProdHr";

    public static final String  DATA_SOURCE_DEFAULT = "dataSourceDefault";

    public static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setDataSourceType(String dataSourceType){
        contextHolder.set(dataSourceType);
    }

    public static String getDataSourceType(){
        return (String) contextHolder.get();
    }
    public static void clearDataSourceType(){
        contextHolder.remove();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return CustomerContextHolder.getDataSourceType();
    }
}
