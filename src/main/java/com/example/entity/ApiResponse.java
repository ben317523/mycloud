package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
    private Integer code;
    private String msg;
    private Object data;

    public static ApiResponse success(String msg, Object data){
        return new ApiResponse(0, msg, data);
    }

    public static ApiResponse fail(String msg){
        return new ApiResponse(-1, msg, null);
    }
}
