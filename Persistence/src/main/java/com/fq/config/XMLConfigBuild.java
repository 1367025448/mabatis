package com.fq.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.fq.pojo.Configuration;
import com.fq.pojo.Resource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class XMLConfigBuild {

    private Configuration configuration;

    public XMLConfigBuild(){
        this.configuration = new Configuration();
    }

    /**
     *解析sqlMapConfig.xml
     */
    public Configuration parseConfig (InputStream inputStream) throws DocumentException {
        //使用dom4j对内存的xml解析
        Document document = new SAXReader().read(inputStream);
        //获取根节点属性<configuration>
        Element rootElement = document.getRootElement();
        //找到节点下的property属性//表示只要是子节点都可以找到
        List<Element> elementList = rootElement.selectNodes("//property");
        Properties properties = new Properties();
        for (Element element : elementList) {
            //获取property的name value 并使用Properties保存key value
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.setProperty(name,value);
        }
         getConfiguration(properties);


        //创建XMLMapperBuild对象解析内存中的mapper.xml
        XMLMapperBuild xmLmapperBuild = new XMLMapperBuild(configuration);
        //获取<mapper>节点
        List<Element> mapperList = rootElement.selectNodes("//mapper");
        for (Element element : mapperList) {
            String resource = element.attributeValue("resoure");
            InputStream in = Resource.getResourceAsStream(resource);
            xmLmapperBuild.parse(in);
        }


        return configuration;
    }


    /**
     * 添加连接池 并给Configuration赋值
     */
    public void getConfiguration(Properties properties){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(properties.getProperty("driverClass"));
        druidDataSource.setUrl(properties.getProperty("url"));
        druidDataSource.setUsername(properties.getProperty("username"));
        druidDataSource.setPassword(properties.getProperty("password"));
        configuration.setDataSource(druidDataSource);
    }

}
