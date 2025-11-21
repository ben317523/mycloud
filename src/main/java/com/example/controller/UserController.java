package com.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.ApiResponse;
import com.example.entity.FileInfo;
import com.example.entity.User;
import com.example.entity.UserInfo;
import com.example.util.JwtUtil;
import com.example.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin("*")
public class UserController {

    @Autowired
    private FileService fileService;

    @Autowired
    private JwtUtil jwtUtil;

    private static int maxUsersNo = 50;

    @RequestMapping(value = "register")
    @ResponseBody
    public ApiResponse register(@RequestParam("name")String name,
                           @RequestParam("password")String password,
                           @RequestParam("passwordConfirm")String passwordConfirm) {
        if (!password.equals(passwordConfirm) ) return ApiResponse.fail("password not match");

        boolean userExist = MainController.users.stream().anyMatch(user -> user.getUserName().equals(name));
        if (userExist) return ApiResponse.fail("User already exists");

        if (MainController.users.size() < maxUsersNo) {
            MainController.users.add(new User(MainController.users.size(), name, password));
            File path = new File("/data/files/" + name);
            path.mkdir();
            File path2 = new File("/root/OneDrive/data/files/" + name);
            path2.mkdir();
            return ApiResponse.success("Success", null);
        } else {
            return ApiResponse.fail("Max No of Users");
        }
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @CrossOrigin("*")
    public ApiResponse index(@RequestParam("name")String name,
                             @RequestParam("password")String password) {

        User user = null;
        for (User t : MainController.users) {
            if (t == null)
                continue;
            if (t.getUserName().equals(name) && t.getPassword().equals(password))
                user = t;
        }
        if (user != null) {
            JSONObject jsonObject = new JSONObject();
            String jwt = jwtUtil.generateToken(user.getUserName());
            jsonObject.put("token", jwt);

            return ApiResponse.success("login successful", jsonObject);
        }
        else
            return ApiResponse.fail("Login fail! User does not exist!");
    }


    @RequestMapping(value = "/changePassword",method = RequestMethod.POST)
    public String changePassword(@RequestParam("name")String name,
                                 @RequestParam("password")String password,
                                 @RequestParam("new_password")String newPassword,
                                 @RequestParam("new_password_confirm")String newPasswordConfirm) {
        for (User u : MainController.users) {
            if (u != null){
                if (u.getUserName().equals(name)
                        && u.getPassword().equals(password) && newPassword.equals(newPasswordConfirm)){
                    u.setPassword(newPassword);
                    return "redirect:login.html";
                }
            }
        }
        return "redirect:fail.html";
    }

    @RequestMapping("/getUserInfo")
    @ResponseBody
    public ApiResponse getUserInfo(@RequestParam("name")String uname,
                                      @RequestHeader(value = "Authorization") String bearerToken) {
        if (bearerToken == null) return ApiResponse.fail("Unauthorized");
        String tokenOnly = bearerToken.split("\\s+")[1];
        if (!jwtUtil.validateToken(tokenOnly)) {
            return ApiResponse.fail("Unauthorized");
        }

        UserInfo data = new UserInfo();
        data.setName(uname);
        data.setPublicNo(fileService.getAllFiles().size());
        data.setPrivateNo(fileService.getUserFiles(uname).size());
        List<FileInfo> userFiles = fileService.getUserFiles(uname);
        long totalSpace = 0;
        for (FileInfo f : userFiles) {
            File file = new File("/data/files/"+uname+"/"+f.getFileName());
            if (file.isFile())
                totalSpace += file.length();
            else
                totalSpace += (file.getTotalSpace() -file.getFreeSpace());
        }
        data.setSpaceUsed((double)totalSpace);
        return ApiResponse.success("success", data);
    }


    @RequestMapping("/getUserFiles")
    @CrossOrigin("*")
    @ResponseBody
    public ApiResponse getUserFiles(@RequestParam("name")String name,
                                     @RequestHeader(value = "Authorization") String bearerToken) {
        if (bearerToken == null) return ApiResponse.fail("Unauthorized");
        String tokenOnly = bearerToken.split("\\s+")[1];
        if (!jwtUtil.validateToken(tokenOnly)) return ApiResponse.fail("Unauthorized");
        try {
            File folder = new File("/data/files/"+name);
            if (!folder.exists())
                return ApiResponse.fail("folder does not exist");

            return ApiResponse.success("user private files", fileService.getUserFiles(name));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ApiResponse.fail("unable to receive user files");
    }
}
