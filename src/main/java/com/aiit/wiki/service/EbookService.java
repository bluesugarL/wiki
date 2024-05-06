package com.aiit.wiki.service;

import com.aiit.wiki.domain.Ebook;
import com.aiit.wiki.domain.EbookExample;
import com.aiit.wiki.mapper.EbookMapper;
import com.aiit.wiki.req.EbookReq;
import com.aiit.wiki.resp.EbookResp;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq ebookReq){
        EbookExample ebookExample = new EbookExample();
        //创建内部类
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%"+ebookReq.getName()+"%");
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        //创建一个泛型参数为EbookResp集合
        List<EbookResp> respList=new ArrayList<>();
        //遍历查询完结果的集合ebookList
        for (Ebook ebook : ebookList) {
            //创建符合respList集合泛型参参数的对象
            EbookResp ebookResp = new EbookResp();
            //完成对象的复制
            BeanUtils.copyProperties(ebook,ebookResp);
            respList.add(ebookResp);
        }
        return respList;
    }
}
