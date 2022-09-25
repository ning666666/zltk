package com.offcn.context.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.offcn.common.utils.PageUtils;
import com.offcn.common.utils.Query;

import com.offcn.context.dao.BannerDao;
import com.offcn.context.entity.BannerEntity;
import com.offcn.context.service.BannerService;
import org.springframework.util.StringUtils;


@Service("bannerService")
public class BannerServiceImpl extends ServiceImpl<BannerDao, BannerEntity> implements BannerService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //1、获取查询关键字
        String key = (String) params.get("key");
        //2、创建查询条件对象
        QueryWrapper<BannerEntity> queryWrapper = new QueryWrapper<>();
        //3、设置查询条件
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.eq("title", key);

        }
        IPage<BannerEntity> page = this.page(
                new Query<BannerEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

}