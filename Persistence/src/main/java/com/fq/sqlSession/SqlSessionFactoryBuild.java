package com.fq.sqlSession;

import com.fq.config.XMLConfigBuild;
import com.fq.pojo.Configuration;
import org.dom4j.DocumentException;

import java.io.InputStream;

/**
 * author:qiang.feng
 * time:2021年4月1日21:17:07
 */
public class SqlSessionFactoryBuild {

    public SqlSessionFactory build (InputStream in) throws DocumentException {
        //创建XMLConfigBuild对象解析内存中的sqlMapConfig.xml
        XMLConfigBuild xmlConfigBuild = new XMLConfigBuild();
        Configuration configuration = xmlConfigBuild.parseConfig(in);

        //创建SqlSessionFactory
        SqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);


        return defaultSqlSessionFactory;
    }


}
