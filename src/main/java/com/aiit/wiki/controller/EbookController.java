package com.aiit.wiki.controller;

import com.aiit.wiki.req.EbookQueryReq;
import com.aiit.wiki.req.EbookSaveReq;
import com.aiit.wiki.resp.CommonResp;
import com.aiit.wiki.resp.EbookQueryResp;
import com.aiit.wiki.resp.PageResp;
import com.aiit.wiki.service.EbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(EbookQueryReq ebookReq) {
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(ebookReq);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody EbookSaveReq req) {
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }

}
