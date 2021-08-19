package com.example.springMybatis.controller;

import com.example.springMybatis.entity.Dict;
import com.example.springMybatis.service.dbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@RestController
//@RequestMapping("/dict")
// public class dbController {
//     //@Autowired
//     //private dbService dbs;
//
//     @GetMapping("/getDict")
//     public Dict getDict(@RequestParam("id") int id){
//         Dict d = dbs.findById(id);
//         return d;
//     }
//
//     @GetMapping("getAll")
//     public List<Dict> getAll(){
//         return dbs.findAllDict();
//     }
// }
