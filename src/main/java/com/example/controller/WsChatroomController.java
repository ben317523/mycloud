package com.example.controller;

import com.example.entity.Message;
import com.example.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
public class WsChatroomController {

    @Autowired
    private FileService fileService;

    @MessageMapping("/chat")
//    @SendTo({"/chat"})
//    @CrossOrigin("*")
    public Message sendMsg(Message msg){
        fileService.addMessage(msg.getName(), msg.getMessage());
        return msg;
    }

    @MessageMapping("/chat/delete")
    public Message deleteMsg(Message msg){
        fileService.deleteMessage(msg.getId(), msg.getName());
        return new Message(msg.getId(), "","");
    }

}
