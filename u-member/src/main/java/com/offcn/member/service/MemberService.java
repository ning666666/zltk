package com.offcn.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.offcn.common.utils.PageUtils;
import com.offcn.member.entity.MemberEntity;

import java.util.List;
import java.util.Map;

/**
 * 会员-会员表
 *
 * @author jialei
 * @email 1694601613@qq.com
 * @date 2022-08-15 19:24:44
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @param beginTime 用户注册echarts图表显示
     * @param endTime
     * @return
     */
    List<Map<String, Object>> countByDateTime(String beginTime, String endTime);

    /**
     * @param username 登录
     * @param password
     * @return
     */
    MemberEntity login(String username,String password);
}

