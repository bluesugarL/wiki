package com.aiit.wiki.service;

import com.aiit.wiki.domain.Content;
import com.aiit.wiki.domain.Doc;
import com.aiit.wiki.domain.DocExample;
import com.aiit.wiki.mapper.ContentMapper;
import com.aiit.wiki.mapper.DocMapper;
import com.aiit.wiki.req.DocQueryReq;
import com.aiit.wiki.req.DocSaveReq;
import com.aiit.wiki.resp.DocQueryResp;
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
public class DocService {

    @Resource
    private DocMapper docMapper;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private SnowFlake snowFlake;

    private static final Logger logger = LoggerFactory.getLogger(DocService.class);

    public List<DocQueryResp> allList() {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);
        List<DocQueryResp> respList = CopyUtil.copyList(docList, DocQueryResp.class);
        return respList;
    }

    //参数不直接使用实体类doc
    public PageResp<DocQueryResp> list(DocQueryReq docReq) {

        DocExample docExample = new DocExample();
        //创建内部类
        DocExample.Criteria criteria = docExample.createCriteria();

        //pagehelper分页只会对遇到的第一个查询语句生效
        PageHelper.startPage(docReq.getPage(), docReq.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        logger.info("总行数：{}", pageInfo.getTotal());//
        logger.info("总页数：{}", pageInfo.getPages());


        List<DocQueryResp> respList = CopyUtil.copyList(docList, DocQueryResp.class);

        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;
    }

    /**
     * 保存
     */
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            //新增
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);

            //新增
            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
            //更新
            docMapper.updateByPrimaryKey(doc);
            //包含大字段的更新
            int count= contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (count==0){
                contentMapper.insert(content);
            }
        }
    }

    public void deleteBook(List<String> ids){
        DocExample docExample = new DocExample();
        //创建内部类
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }
}
