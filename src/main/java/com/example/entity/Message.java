package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
    private Integer id;
    private String name;
    private String message;

    public Message(){}

    public Message(String name, String message){
        this.name = name;
        this.message = message;
    }

}
