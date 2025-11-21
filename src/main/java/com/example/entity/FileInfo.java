package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {
    private String fileName;
    private Long size;
    private Boolean completed = true;
    private LocalDateTime lastUpdate;

    public FileInfo(String fileName){
        this.fileName = fileName;
    }

    public FileInfo(String fileName, Long size){
        this.fileName = fileName;
        this.size = size;
    }
}
