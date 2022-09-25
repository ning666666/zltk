package com.offcn.question.dao;

import com.offcn.question.entity.TypeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 题目-题目类型表
 * 
 * @author jialei
 * @email 1694601613@qq.com
 * @date 2022-08-15 19:20:33
 */
@Mapper
public interface TypeDao extends BaseMapper<TypeEntity> {
	
}
