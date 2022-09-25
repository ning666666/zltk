package com.offcn.question.service.impl;

import com.offcn.question.entity.QuestionEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.offcn.common.utils.PageUtils;
import com.offcn.common.utils.Query;

import com.offcn.question.dao.TypeDao;
import com.offcn.question.entity.TypeEntity;
import com.offcn.question.service.TypeService;


@Service("typeService")
public class TypeServiceImpl extends ServiceImpl<TypeDao, TypeEntity> implements TypeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        /*IPage<TypeEntity> page = this.page(
                new Query<TypeEntity>().getPage(params),
                new QueryWrapper<TypeEntity>()
        );*/
        String key = (String)params.get("key");
        //创建条件构造器对象
        QueryWrapper<TypeEntity> wrapper = new QueryWrapper<>();
        if(StringUtils.isEmpty(key)){
            //往条件构造器对象中添加条件
            wrapper.like("TYPE",key);
        }
        //继承ServiceImpl的分页查询，得到分页对象，再使用分页工具类获取分页数据
        IPage<TypeEntity> page = this.page(
                new Query<TypeEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    @Override
    public List<TypeEntity> findAll() {
        return this.list();
    }

}