package com.aiit.wiki.service;

import com.aiit.wiki.domain.Ebook;
import com.aiit.wiki.domain.EbookExample;
import com.aiit.wiki.mapper.EbookMapper;
import com.aiit.wiki.req.EbookQueryReq;
import com.aiit.wiki.req.EbookSaveReq;
import com.aiit.wiki.resp.EbookQueryResp;
import com.aiit.wiki.resp.PageResp;
import com.aiit.wiki.util.CopyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;

    private static final Logger logger = LoggerFactory.getLogger(EbookService.class);

    //参数不直接使用实体类ebook
    public PageResp<EbookQueryResp> list(EbookQueryReq ebookReq) {

        EbookExample ebookExample = new EbookExample();
        //创建内部类
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(ebookReq.getName())) {
            //查询到的结果不为空就按条件去查询对应电子书
            criteria.andNameLike("%" + ebookReq.getName() + "%");
        }
        //pagehelper分页只会对遇到的第一个查询语句生效
        PageHelper.startPage(ebookReq.getPage(), ebookReq.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        logger.info("总行数：{}", pageInfo.getTotal());//
        logger.info("总页数：{}", pageInfo.getPages());

        //创建一个泛型参数为EbookResp集合
        List<EbookQueryResp> respList = new ArrayList<>();

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
        respList = CopyUtil.copyList(ebookList, EbookQueryResp.class);

        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;
    }

    /**
     * 保存
     */
    public void save(EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            //新增
        } else {
            //更新
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }
}
