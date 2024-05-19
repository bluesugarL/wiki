package com.aiit.wiki.service;

import com.aiit.wiki.domain.Ebook;
import com.aiit.wiki.domain.EbookExample;
import com.aiit.wiki.mapper.EbookMapper;
import com.aiit.wiki.req.EbookQueryReq;
import com.aiit.wiki.req.EbookSaveReq;
import com.aiit.wiki.resp.EbookQueryResp;
import com.aiit.wiki.resp.PageResp;
import com.aiit.wiki.util.CopyUtil;
import com.aiit.wiki.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;

    @Resource
    private SnowFlake snowFlake;

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
        if (!ObjectUtils.isEmpty(ebookReq.getCategoryId2())) {
            criteria.andCategory2IdEqualTo( ebookReq.getCategoryId2());
        }
        //pagehelper分页只会对遇到的第一个查询语句生效
        PageHelper.startPage(ebookReq.getPage(), ebookReq.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        logger.info("总行数：{}", pageInfo.getTotal());//
        logger.info("总页数：{}", pageInfo.getPages());


        List<EbookQueryResp> respList = CopyUtil.copyList(ebookList, EbookQueryResp.class);

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
            ebook.setId(snowFlake.nextId());
            System.out.println("雪花生成ID："+ebook.getId());//right
            ebookMapper.insert(ebook);
        } else {
            //更新
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }

    public void deleteBook(Long id){
        ebookMapper.deleteByPrimaryKey(id);
    }
}
