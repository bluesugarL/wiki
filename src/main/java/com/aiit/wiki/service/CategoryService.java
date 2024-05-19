package com.aiit.wiki.service;

import com.aiit.wiki.domain.Category;
import com.aiit.wiki.domain.CategoryExample;
import com.aiit.wiki.mapper.CategoryMapper;
import com.aiit.wiki.req.CategoryQueryReq;
import com.aiit.wiki.req.CategorySaveReq;
import com.aiit.wiki.resp.CategoryQueryResp;
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
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    public List<CategoryQueryResp> allList() {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        List<CategoryQueryResp> respList = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        return respList;
    }

    //参数不直接使用实体类category
    public PageResp<CategoryQueryResp> list(CategoryQueryReq categoryReq) {

        CategoryExample categoryExample = new CategoryExample();
        //创建内部类
        CategoryExample.Criteria criteria = categoryExample.createCriteria();

        //pagehelper分页只会对遇到的第一个查询语句生效
        PageHelper.startPage(categoryReq.getPage(), categoryReq.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        logger.info("总行数：{}", pageInfo.getTotal());//
        logger.info("总页数：{}", pageInfo.getPages());


        List<CategoryQueryResp> respList = CopyUtil.copyList(categoryList, CategoryQueryResp.class);

        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;
    }

    /**
     * 保存
     */
    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req, Category.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            //新增
            category.setId(snowFlake.nextId());
            System.out.println("雪花生成ID："+category.getId());//right
            categoryMapper.insert(category);
        } else {
            //更新
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    public void deleteBook(Long id){
        categoryMapper.deleteByPrimaryKey(id);
    }
}
