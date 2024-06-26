package com.aiit.wiki.controller;

import com.aiit.wiki.req.DocQueryReq;
import com.aiit.wiki.req.DocSaveReq;
import com.aiit.wiki.resp.CommonResp;
import com.aiit.wiki.resp.DocQueryResp;
import com.aiit.wiki.resp.PageResp;
import com.aiit.wiki.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {

    @Resource
    private DocService docService;

    @GetMapping("/all/{ebookId}")
    public CommonResp allList(@PathVariable Long ebookId) {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.allList(ebookId);
        System.out.println(list.toString());
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/list")
    public CommonResp list(@Valid DocQueryReq docReq) {
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(docReq);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req) {
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{idStr}")
    public CommonResp delete(@PathVariable String idStr) {
        CommonResp resp = new CommonResp<>();
        if (idStr.isEmpty()) {
            resp.setSuccess(false);
        } else {
            List<String> list = Arrays.asList(idStr.split(","));
            docService.deleteBook(list);
        }
        return resp;

    }

    @GetMapping("/find-content/{id}")
    public CommonResp findContent(@PathVariable Long id) {
        CommonResp<String> resp = new CommonResp<>();
        String content = docService.findContent(id);
        resp.setContent(content);
        return resp;
    }
    @GetMapping("/vote/{id}")
    public CommonResp vote(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        docService.vote(id);
        return resp;
    }
}
