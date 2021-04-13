package com.fq.sqlSession;

import java.util.List;

public interface SqlSession {

    <E> List<E> selectAll(String statementId, Object...obj) throws Exception;

    <T> T  selectOne(String s, Object...obj) throws Exception;

    <T> T getMapper(Class<?> mapperClass);

    Integer update(String statementId,Object...obj) throws Exception;

    Integer insert(String statementId,Object...obj) throws Exception;

    Integer delete(String statementId,Object...obj) throws Exception;
}
