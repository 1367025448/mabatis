package com.fq;

import com.fq.dao.UserDao;
import com.fq.pojo.Resource;
import com.fq.pojo.User;
import com.fq.sqlSession.SqlSession;
import com.fq.sqlSession.SqlSessionFactory;
import com.fq.sqlSession.SqlSessionFactoryBuild;

import java.io.InputStream;
import java.util.List;

public class test {

    public static void main(String[] args) throws Exception {
        InputStream resourceAsStream = Resource.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuild sqlSessionFactoryBuild = new SqlSessionFactoryBuild();
        SqlSessionFactory factory = sqlSessionFactoryBuild.build(resourceAsStream);
        SqlSession sqlSession = factory.openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        userDao.selectAll();
        User user = new User();
        user.setId(1);
        userDao.delete(user);



//        User object = sqlSession.selectOne("com.fq.pojo.User.selectByUsername",user);
//        List<Object>  objects = sqlSession.selectAll("com.fq.pojo.User.selectAll");
//        System.out.println(objects);
    }
}
