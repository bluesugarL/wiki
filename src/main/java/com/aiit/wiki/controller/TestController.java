//package com.aiit.wiki.controller;
//
//import com.aiit.wiki.domain.Test;
//import com.aiit.wiki.service.TestService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//@RestController
//public class TestController {
//
//    @Resource
//    private RedisTemplate redisTemplate;
//
//    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
//
//    @Value("${test.hello:NULL}")
//    private String findNp;
//    @Resource
//    private TestService testService;
//
//    @GetMapping("/hello")
//    public String hello() {
//        return "hello" + "\t" + findNp;
//    }
//
//    @PostMapping("/hi")
//    public String hi(String name) {
//        return "hi" + "\t" + name;
//    }
//    @GetMapping("/test/list")
//    public List<Test> list(){
//        return testService.list();
//    }
//    @RequestMapping("/redis/set/{key}/{value}")
//    public String set(@PathVariable Long key, @PathVariable String value) {
//        redisTemplate.opsForValue().set(key, value, 3600, TimeUnit.SECONDS);
//        logger.info("key: {}, value: {}", key, value);
//        return "success";
//    }
//
//    @RequestMapping("/redis/get/{key}")
//    public Object get(@PathVariable Long key) {
//        Object object = redisTemplate.opsForValue().get(key);
//        logger.info("key: {}, value: {}", key, object);
//        return object;
//    }
//}