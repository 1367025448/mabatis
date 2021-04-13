package com.fq.pojo;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * author:qiang.feng
 * time: 2021年4月1日21:00:44
 */
public class Configuration {

    //数据源对象信息
    private DataSource dataSource;

    /**
     * key:statementId : 由namespace 和 标签id组成
     * value:封装好的MapperStatement对象
     */
    private Map<String,MapperStatement> mapperStatementMap = new HashMap<>();

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Map<String, MapperStatement> getMapperStatementMap() {
        return mapperStatementMap;
    }

    public void setMapperStatementMap(Map<String, MapperStatement> mapperStatementMap) {
        this.mapperStatementMap = mapperStatementMap;
    }
}
