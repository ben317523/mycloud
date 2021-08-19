package com.example.springMybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    public Integer id;
    public String userName;
    public String password;
}
