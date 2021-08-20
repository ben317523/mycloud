package com.example.springMybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfo {
    private String name;
    private Integer publicNo;
    private Integer privateNo;
    private Long spaceUsed;

    public UserInfo() {

    }
}
