package com.pm.ex7.controller;

import com.pm.ex7.configuration.security.Demo4ConditionEvaluator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DemoController {

    @GetMapping("/demo")
    @PreAuthorize("hasAnyAuthority('read')")
    public String demo() {
        return "demo";
    }

    @GetMapping("/demo2")
    @PreAuthorize("hasAnyAuthority('read', 'write')")
    public String demo2() {
        return "demo2";
    }

    @GetMapping("/demo3/{smth}")
    @PreAuthorize("""
        #something == authentication.name or
        hasAnyAuthority('read')
        """)
    public String demo3(@PathVariable(name = "smth") String something) {
        return "demo 3";
    }

    @GetMapping("/demo4/{username}")
    @PreAuthorize("@demo4ConditionEvaluator.hasAnyAuthorityReadAndCorrectUsername(#username, authentication)")
    public String demo4(@PathVariable(name = "username") String username) {
        return "Welcome " + username;
    }

    @GetMapping("/demo5")
    @PostAuthorize("returnObject == 'demo 5'")
    //executed after a service is done its job, use when restrict access to the value
    //never use postAuthorize with methods that change data
    public String demo5() {
        return "demo5";
    }

    @GetMapping("/demo6")
    @PreFilter("filterObject.contains('a')")
    public String demo6(@RequestBody List<String> values) {
        return "demo6";
    }

    @GetMapping("/demo7")
    @PostFilter("filterObject.contains('a')")
    public List<String> demo7() {
        return List.of("aabb", "sadsa");
    }
}
