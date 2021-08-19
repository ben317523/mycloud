package com.example.springMybatis.mapper;

import com.example.springMybatis.entity.User;

import java.util.List;

public interface UserMapper {
    public List<User> getAll();
    public User findUser(String name,String password);
}
