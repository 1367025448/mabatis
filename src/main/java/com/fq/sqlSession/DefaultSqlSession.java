package com.fq.sqlSession;

import com.fq.pojo.Configuration;
import com.fq.pojo.MapperStatement;

import java.lang.reflect.*;
import java.sql.SQLSyntaxErrorException;
import java.util.List;

public class DefaultSqlSession implements SqlSession{

    private Configuration configuration;

    private SimpleExecutor simpleExecutor = new SimpleExecutor();

    public DefaultSqlSession(Configuration configuration){
        this.configuration = configuration;
    }
    @Override
    public <E> List<E> selectAll(String statementId, Object... obj) throws Exception {
        MapperStatement mapperStatement = configuration.getMapperStatementMap().get(statementId);
        List<Object> list = simpleExecutor.query(configuration, mapperStatement, obj);
        return (List<E>) list;
    }

    @Override
    public <T> T selectOne(String statementId, Object... obj) throws Exception {
        List<Object> objects = selectAll(statementId, obj);
        if(objects.size()==1){
            return (T) objects.get(0);
        }else if(objects.size()==0){
            throw new SQLSyntaxErrorException("数据库没有你要查找的数据");
        }else {
            throw new RuntimeException("返回结果过多");
        }
    }



    @Override
    public Integer update(String statementId, Object... obj) throws Exception {
        MapperStatement mapperStatement = configuration.getMapperStatementMap().get(statementId);
        Integer state = simpleExecutor.update(configuration,mapperStatement,obj);
        return state;
    }

    @Override
    public Integer insert(String statementId, Object... obj) throws Exception {
        MapperStatement mapperStatement = configuration.getMapperStatementMap().get(statementId);
        Integer state = simpleExecutor.insert(configuration,mapperStatement,obj);
        return state;
    }

    @Override
    public Integer delete(String statementId, Object... obj) throws Exception {
        MapperStatement mapperStatement = configuration.getMapperStatementMap().get(statementId);
        Integer state = simpleExecutor.delete(configuration,mapperStatement,obj);
        return state;
    }

    /**
     *
     * @param mapperClass 传过来的接口
     * @param <T>
     * @return
     */
    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 底层都还是去执行JDBC代码 //根据不同情况，来调用selctList或者selectOne
                // 准备参数 1：statmentid :sql语句的唯一标识：namespace.id= 接口全限定名.方法名
                //获取方法名
                String methodName = method.getName();
                //获取类名
                String className = method.getDeclaringClass().getName();
                String statementId = className + "." + methodName;

                if(methodName.contains("update")){
                    Object o = update(statementId,args);
                    return o;
                }
                else if(methodName.contains("select")){
                    //判断方法返回的类型是否带泛型
                    Type genericReturnType = method.getGenericReturnType();
                    if (genericReturnType instanceof ParameterizedType) {
                        List<Object> objects = selectAll(statementId,args);
                        return objects;
                    }
                    Object o = selectOne(statementId, args);
                    return o;
                }else if(methodName.contains("insert")){
                   Object o =  insert(statementId,args);
                   return o;
                }else if(methodName.contains("delete")){
                    Object o =  delete(statementId,args);
                    return o;
                }
                return null;
            }
        });
        return (T) proxyInstance;
    }

}
