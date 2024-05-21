package com.aiit.wiki.controller;

import com.aiit.wiki.req.UserQueryReq;
import com.aiit.wiki.req.UserSaveReq;
import com.aiit.wiki.resp.CommonResp;
import com.aiit.wiki.resp.PageResp;
import com.aiit.wiki.resp.UserQueryResp;
import com.aiit.wiki.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq userReq) {
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(userReq);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq req) {
        CommonResp resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        System.out.println("当前获取的id为："+id);//error
        userService.deleteBook(id);
        return resp;
    }
}