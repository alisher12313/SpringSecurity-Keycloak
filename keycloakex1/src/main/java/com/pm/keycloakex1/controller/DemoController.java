package com.pm.keycloakex1.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping
    @PreAuthorize("hasAnyRole('client-user')")
    public String demo() {
        return "Hello World";
    }

    @GetMapping("/demo2")
    @PreAuthorize("hasAnyRole('client-admin')")
    public String demo2() {
        return "Demo2";
    }
}