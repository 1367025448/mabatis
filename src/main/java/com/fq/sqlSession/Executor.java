package com.fq.sqlSession;

import com.fq.pojo.Configuration;
import com.fq.pojo.MapperStatement;

import java.sql.SQLException;
import java.util.List;

public interface Executor {

    public <E> List<E> query(Configuration configuration, MapperStatement mapperStatement,Object...obj) throws ClassNotFoundException, Exception;

    Integer update(Configuration configuration, MapperStatement mapperStatement, Object[] obj) throws SQLException, Exception;

    Integer insert(Configuration configuration, MapperStatement mapperStatement, Object[] obj) throws SQLException, Exception;

    Integer delete(Configuration configuration, MapperStatement mapperStatement, Object[] obj) throws SQLException, Exception;
}
