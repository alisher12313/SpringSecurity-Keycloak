package com.pm.ex1.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class DemoController {

    @GetMapping("/hello")
    public ResponseEntity<?> greetings(){
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }
}
