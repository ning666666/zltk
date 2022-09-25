package com.offcn.context.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.offcn.common.utils.PageUtils;
import com.offcn.context.entity.BannerEntity;

import java.util.Map;

/**
 * 内容-横幅广告表
 *
 * @author jialei
 * @email 1694601613@qq.com
 * @date 2022-08-15 19:10:17
 */
public interface BannerService extends IService<BannerEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

