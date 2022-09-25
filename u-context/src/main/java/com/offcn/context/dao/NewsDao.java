package com.offcn.context.dao;

import com.offcn.context.entity.NewsEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 内容-资讯表
 * 
 * @author jialei
 * @email 1694601613@qq.com
 * @date 2022-08-15 19:10:17
 */
@Mapper
public interface NewsDao extends BaseMapper<NewsEntity> {
	
}
