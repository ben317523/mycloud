package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    public Integer id;
    public String userName;
    public String password;
    public String utoken;
    public User(){}
    public User(Integer id,String userName,String password){
        this.id=id;
        this.userName=userName;
        this.password=password;
    }
}
