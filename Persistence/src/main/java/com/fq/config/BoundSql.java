package com.fq.config;

import com.fq.utils.ParameterMapping;

import java.util.ArrayList;
import java.util.List;

public class BoundSql {

    //解析后的sql
    private String sql;

    //占位符里的内容
    private List<ParameterMapping> params = new ArrayList<>();

    public BoundSql(String sql, List<ParameterMapping> params) {
        this.sql = sql;
        this.params = params;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setParams(List<ParameterMapping> params) {
        this.params = params;
    }

    public String getSql() {
        return sql;
    }

    public List<ParameterMapping> getParams() {
        return params;
    }
}
