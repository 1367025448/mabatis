package com.fq.pojo;
/**
 * author:qiang.feng
 * time: 2021年4月1日21:00:44
 */
public class MapperStatement {

    //Id标识
    private String id;

    //返回值类型
    private String resultType;

    //参数类型
    private String paramType;

    public void setId(String id) {
        this.id = id;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getId() {
        return id;
    }

    public String getResultType() {
        return resultType;
    }

    public String getParamType() {
        return paramType;
    }

    public String getSql() {
        return sql;
    }

    //sql语句
    private String sql;


}
