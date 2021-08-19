package com.example.springMybatis.mapper;


import com.example.springMybatis.entity.Dict;

import java.util.List;

public interface DictMapper {
    public Dict findById(int id);

    public List<Dict> findAll();
}
