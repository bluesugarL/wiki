package com.aiit.wiki.controlller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${test.hello:NULL}")
    private String findNp;

    @GetMapping("/hello")
    public String hello(){
        return "hello"+"\t"+findNp;
    }

    @PostMapping("hi")
    public String hi(String name){
        return "hi"+"\t"+name;
    }
}
