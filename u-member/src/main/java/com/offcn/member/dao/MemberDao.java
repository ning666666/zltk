package com.offcn.member.dao;

import com.offcn.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员-会员表
 * 
 * @author jialei
 * @email 1694601613@qq.com
 * @date 2022-08-15 19:24:44
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
