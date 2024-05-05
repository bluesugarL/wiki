package com.aiit.wiki.controlller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/hello")
    public String hello(){
        return "hello yuri";
    }

    @PostMapping("hi")
    public String hi(String name){
        return "hi"+"\t"+name;
    }
}
