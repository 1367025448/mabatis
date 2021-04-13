package com.fq.config;

import com.fq.pojo.Configuration;
import com.fq.pojo.MapperStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XMLMapperBuild {

    private Configuration configuration;

    public XMLMapperBuild(Configuration configuration){
        this.configuration = configuration;
    }

    /**
     * 解析mapper.xml 代码注释请参照 XMLConfigBuild
     * @param inputStream mapper.xml
     */
    public void parse (InputStream inputStream) throws DocumentException {
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        //获取<mapper>的namespace属性
        String namespace = rootElement.attributeValue("namespace");

        //查找
        //获取select标签
        List<Element> selectList = rootElement.selectNodes("//select");
        //存入mapperStatementMap中
        setConfigurationMapperStatement(selectList,namespace);

        //更新
        List<Element> updateList = rootElement.selectNodes("//update");
        setConfigurationMapperStatement(updateList,namespace);

        //插入
        List<Element> insertList = rootElement.selectNodes("//insert");
        setConfigurationMapperStatement(insertList,namespace);

        //删除
        List<Element> deleteList = rootElement.selectNodes("//delete");
        setConfigurationMapperStatement(deleteList,namespace);
    }


    /**
     * 把<select>标签中的属性获取到并添加到Configuration 中的mapperStatementMap属性里
     * @param elements 获取标签属性
     * @param namespace 命名空间
     */
    public void setConfigurationMapperStatement(List<Element> elements,String namespace){
        for (Element element : elements) {
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String paramType = element.attributeValue("paramType");
            String sql = element.getTextTrim();
            String key = namespace + "." + id;
            MapperStatement mapperStatement = new MapperStatement();
            mapperStatement.setId(id);
            mapperStatement.setParamType(paramType);
            mapperStatement.setResultType(resultType);
            mapperStatement.setSql(sql);
            configuration.getMapperStatementMap().put(key , mapperStatement);
        }
    }

}
