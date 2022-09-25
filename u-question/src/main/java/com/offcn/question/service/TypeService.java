package com.offcn.question.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.offcn.common.utils.PageUtils;
import com.offcn.question.entity.TypeEntity;

import java.util.List;
import java.util.Map;

/**
 * 题目-题目类型表
 *
 * @author jialei
 * @email 1694601613@qq.com
 * @date 2022-08-15 19:20:33
 */
public interface TypeService extends IService<TypeEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取全部分类
    List<TypeEntity> findAll();
}

