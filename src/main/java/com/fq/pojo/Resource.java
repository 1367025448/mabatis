package com.fq.pojo;

import java.io.InputStream;
/**
 * author:qiang.feng
 * time: 2021年4月1日21:00:44
 */
public class Resource {

    //使用类加载器动态加载文件 把文件保存在内存中
    public static InputStream getResourceAsStream(String path){
       return Resource.class.getClassLoader().getResourceAsStream(path);
    }

}
