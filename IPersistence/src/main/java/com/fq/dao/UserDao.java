package com.fq.dao;

import com.fq.pojo.User;

import java.util.List;

public interface UserDao {


    List<User> selectAll();

    User selectOne(User user);

     Integer update(User user);

     Integer insert(User user);

     Integer delete(User user);


}
