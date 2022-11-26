package com.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

@Controller
@CrossOrigin("*")
public class IndexController {

    /**
     *  index
     * @return
     */
    @RequestMapping("/")
    public String login(){
        return "index.html";
    }

    @RequestMapping("/sysInfo")
    @ResponseBody
    public ApiResponse systemInfo(){
        try {
            File root = new File("/data");

            long totalSpace = root.getTotalSpace();
            long usableSpace = root.getUsableSpace();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total", totalSpace);
            jsonObject.put("free", usableSpace);
            jsonObject.put("used", (totalSpace-usableSpace));

            return ApiResponse.success("System Info", jsonObject);

        } catch (Exception e){
            return ApiResponse.fail("Unable to fetch system info");
        }


    }
}
