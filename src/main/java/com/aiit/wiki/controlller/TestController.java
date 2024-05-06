package com.aiit.wiki.controlller;

import com.aiit.wiki.domain.Test;
import com.aiit.wiki.service.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TestController {

    @Value("${test.hello:NULL}")
    private String findNp;
    @Resource
    private TestService testService;
    @GetMapping("/hello")
    public String hello() {
        return "hello" + "\t" + findNp;
    }

    @PostMapping("/hi")
    public String hi(String name) {
        return "hi" + "\t" + name;
    }
    @GetMapping("/test/list")
    public List<Test> list(){
        return testService.list();
    }
}
