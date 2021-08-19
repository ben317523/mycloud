package com.example.springMybatis.service;
import com.example.springMybatis.entity.Dict;

import java.util.List;

public interface dbService {
    public Dict findById(int id);
    public List<Dict> findAllDict();
}
