package com.example.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.EOFException;

@ControllerAdvice
public class MyExceptionHandler{

    @ExceptionHandler(value = EOFException.class)
    public void handleFileStream(EOFException e){

    }
}
