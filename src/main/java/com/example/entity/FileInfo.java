package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {
    private String fileName;
    private Long size;
    private Boolean completed = true;

    public FileInfo(String fileName){
        this.fileName = fileName;
    }

    public FileInfo(String fileName, Long size){
        this.fileName = fileName;
        this.size = size;
    }
}
