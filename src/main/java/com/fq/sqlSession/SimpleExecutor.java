package com.fq.sqlSession;

import com.fq.config.BoundSql;
import com.fq.pojo.Configuration;
import com.fq.pojo.MapperStatement;
import com.fq.utils.GenericTokenParser;
import com.fq.utils.ParameterMapping;
import com.fq.utils.ParameterMappingTokenHandler;

import javax.sql.DataSource;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor {
    @Override
    public <E> List<E> query(Configuration configuration, MapperStatement mapperStatement, Object... obj) throws Exception {
         //获取连接
        Connection connection = getConnection(configuration);
        //2.获取MapperStatement对象存的sql信息
        String sql = mapperStatement.getSql();
        BoundSql parseSql = getParseSql(sql);
        //获取预处理对象
        PreparedStatement preparedStatement = connection.prepareStatement(parseSql.getSql());
        //4.获取参数类型
            //类全限定名
        String paramType = mapperStatement.getParamType();
            //获取类的字节码
        Class<?> paramClass = getParamClass(paramType);

        List<ParameterMapping> params = parseSql.getParams();
        for (int i = 0; i < params.size() ; i++) {
            ParameterMapping parameterMapping = params.get(i);
            String content = parameterMapping.getContent();
            Field declaredField = paramClass.getDeclaredField(content);
            declaredField.setAccessible(true);
            Object o = declaredField.get(obj[0]);
            //设置参数
            preparedStatement.setObject(i+1,o);
        }
        //5.执行sql
        ResultSet resultSet = preparedStatement.executeQuery();
        //封装结果集
            //获取返回值类型
        String resultType = mapperStatement.getResultType();
        Class<?> resultClass = getParamClass(resultType);
        ArrayList<Object> objects = new ArrayList<>();
        while (resultSet.next()){
            Object o = resultClass.newInstance();
            //获取元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i);
                Object value = resultSet.getObject(columnName);
                //内省
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o,value);
            }
            objects.add(o);
        }
        close(preparedStatement,connection,resultSet);
        return (List<E>) objects;
    }

    @Override
    public Integer update(Configuration configuration, MapperStatement mapperStatement, Object[] obj) throws Exception {
        return common(configuration,mapperStatement,obj);
    }

    @Override
    public Integer insert(Configuration configuration, MapperStatement mapperStatement, Object[] obj) throws Exception {
        return common(configuration,mapperStatement,obj);
    }

    @Override
    public Integer delete(Configuration configuration, MapperStatement mapperStatement, Object[] obj) throws Exception {
        return common(configuration,mapperStatement,obj);
    }

    private Class<?> getParamClass(String paramType) throws ClassNotFoundException {
        //判断是否为null
        if(paramType != null){
           return Class.forName(paramType);
        }
        return null;
    }

    //解析占位符#{}
    private BoundSql getParseSql(String sql) {
        //创建解析器
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        //把占位符转换为?
        String parseSql = genericTokenParser.parse(sql);
        //保存解析后的占位符里的内容
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        //用类保存sql信息
        BoundSql boundSql = new BoundSql(parseSql, parameterMappings);
        return boundSql;
    }

    /**
     * 获取connection
     */
    public Connection getConnection(Configuration configuration) throws SQLException {
        //注册驱动
        DataSource dataSource = configuration.getDataSource();
        //创建连接
        return dataSource.getConnection();

    }


    /**
     * 关流
     */
    public void close(Statement statement,Connection connection,ResultSet resultSet) throws SQLException {
        statement.close();
        connection.close();
        if(resultSet != null){
            resultSet.close();
        }
    }


    /**
     * update insert 共同方法
     */

    public Integer common(Configuration configuration, MapperStatement mapperStatement, Object... obj) throws Exception {
        Connection connection = getConnection(configuration);
        String sql = mapperStatement.getSql();
        BoundSql parseSql = getParseSql(sql);
        PreparedStatement preparedStatement = connection.prepareStatement(parseSql.getSql());
        String paramType = mapperStatement.getParamType();
        Class<?> paramClass = getParamClass(paramType);
        List<ParameterMapping> params = parseSql.getParams();
        for (int i = 0; i < params.size() ; i++) {
            ParameterMapping parameterMapping = params.get(i);
            String content = parameterMapping.getContent();
            Field declaredField = paramClass.getDeclaredField(content);
            declaredField.setAccessible(true);
            Object o = declaredField.get(obj[0]);
            //设置参数
            preparedStatement.setObject(i+1,o);
        }
        //5.执行sql
        Integer state = preparedStatement.executeUpdate();
        if(state != 1){
            throw new SQLSyntaxErrorException("你的sql有误");
        }
        close(preparedStatement,connection,null);
        return state;
    }
}
