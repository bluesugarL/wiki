package com.aiit.wiki.controller;

import com.aiit.wiki.req.CategoryQueryReq;
import com.aiit.wiki.req.CategorySaveReq;
import com.aiit.wiki.resp.CategoryQueryResp;
import com.aiit.wiki.resp.CommonResp;
import com.aiit.wiki.resp.PageResp;
import com.aiit.wiki.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/list")
    public CommonResp list(@Valid CategoryQueryReq categoryReq) {
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.list(categoryReq);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq req) {
        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        System.out.println("当前获取的id为："+id);//error
        categoryService.deleteBook(id);
        return resp;
    }
}
