package com.pm.ex5.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/demo")
    public String demo(){
        return "Demo";
    }

    @GetMapping("/demo2")
    public String demo2(){
        return "Demo2";
    }
}
