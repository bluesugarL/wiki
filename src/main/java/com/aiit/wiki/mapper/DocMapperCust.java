package com.aiit.wiki.mapper;

import org.apache.ibatis.annotations.Param;

public interface DocMapperCust {
    public void increateViewCount(@Param("id") Long id);
    public void increateVoteCount(@Param("id") Long id);
    public void updateEbookInfo();
}
