package com.aiit.wiki.service;

import com.aiit.wiki.domain.Ebook;
import com.aiit.wiki.domain.EbookExample;
import com.aiit.wiki.mapper.EbookMapper;
import com.aiit.wiki.req.EbookReq;
import com.aiit.wiki.resp.EbookResp;
import com.aiit.wiki.util.CopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;

    //参数不直接使用实体类ebook
    public List<EbookResp> list(EbookReq ebookReq) {
        EbookExample ebookExample = new EbookExample();
        //创建内部类
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(ebookReq.getName())) {
            //查询到的结果不为空就按条件去查询对应电子书
            criteria.andNameLike("%" + ebookReq.getName() + "%");
        }
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        //创建一个泛型参数为EbookResp集合
        List<EbookResp> respList = new ArrayList<>();

        //遍历查询完结果的集合ebookList
        //for (Ebook ebook : ebookList) {
        //    //创建符合respList集合泛型参参数的对象
        //    //EbookResp ebookResp = new EbookResp();
        //    ////完成对象的复制
        //    //BeanUtils.copyProperties(ebook,ebookResp);
        //    /*
        //    使用自定义工具类 copyutil
        //    */
        //    EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);
        //    respList.add(ebookResp);
        //}
        respList = CopyUtil.copyList(ebookList, EbookResp.class);
        return respList;
    }
}
