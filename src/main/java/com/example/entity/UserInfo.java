package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfo {
    private String name;
    private Integer publicNo;
    private Integer privateNo;
    private Double spaceUsed;

    public UserInfo() {

    }
}
