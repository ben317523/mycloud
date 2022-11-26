package com.example.controller;

import com.example.constant.Token;
import com.example.entity.Message;
import com.example.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("chatroom")
@CrossOrigin("*")
public class ChatroomController {
    @Autowired
    private FileService fileService;

    @RequestMapping("sendMessage")
    public String addMessage(@CookieValue(value = "token", required = false)String userToken,
                             @RequestParam(value = "name", required = false)String name,
                             @RequestParam("message") String message){
        //TODO check token
//        if (userToken != null && Token.token.equals(userToken))
//            System.out.println("valid token");
//        else
//            return "Unauthorized";

        List<Message> list = fileService.addMessage(name, message);


        return "success";
    }

    @RequestMapping("getAllMessages")
    public List<Message> getAllMessages(@CookieValue(value = "token", required = false)String userToken,
                                        @RequestParam("name")String name){
        //TODO check token
//        if (userToken != null && Token.token.equals(userToken))
//            System.out.println("valid token");
//        else
//            return null;

        List<Message> result = fileService.getAllMessage();

        return result;
    }

    @RequestMapping(value = "deleteMessage")
    public String deleteMessage(@CookieValue(value = "token", required = false)String userToken,
                                @RequestParam("name")String name,
                                @RequestParam("id")Integer id) {
//        if (userToken != null && Token.token.equals(userToken))
//            System.out.println("valid token");
//        else
//            return "unauthorized";

        boolean success = fileService.deleteMessage(id, name);

        if (success)
            return "success";
        else
            return "fail";
    }
}
