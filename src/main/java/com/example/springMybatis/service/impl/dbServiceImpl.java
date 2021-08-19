package com.example.springMybatis.service.impl;

import com.example.springMybatis.entity.Dict;
import com.example.springMybatis.mapper.DictMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.springMybatis.service.dbService;

import java.util.List;

@Service
public class dbServiceImpl implements dbService{
    @Autowired
    private DictMapper dictMapper;

    @Override
    public Dict findById(int id){
        return (dictMapper.findById(id));
    }

    @Override
    public List<Dict> findAllDict(){
        return (dictMapper.findAll());
    }
}
