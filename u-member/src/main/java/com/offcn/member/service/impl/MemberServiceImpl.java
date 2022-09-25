package com.offcn.member.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.offcn.common.utils.PageUtils;
import com.offcn.common.utils.Query;

import com.offcn.member.dao.MemberDao;
import com.offcn.member.entity.MemberEntity;
import com.offcn.member.service.MemberService;
import org.springframework.util.StringUtils;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //1、获取查询关键字
        String key = (String) params.get("key");
        //2、创建查询条件对象
        QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<>();
        //3、设置查询条件
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.eq("user_name", key).or().like("nickname", key);

        }
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }
    @Autowired
    private MemberDao memberDao;

    @Override
    public List<Map<String, Object>> countByDateTime(String beginTime, String endTime) {
        QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<MemberEntity>().select("DISTINCT(DATE_FORMAT(create_time,'%Y-%m-%d')) AS NAME ,COUNT(id) AS VALUE").between("create_time", beginTime + " 00:00:00", endTime + " 23:59:59").groupBy("name");
        List<Map<String, Object>> mapList = memberDao.selectMaps(queryWrapper);
        //System.out.println("mapList = " + mapList);
        //mapList = [{VALUE=1, NAME=2020-12-31}, {VALUE=2, NAME=2021-01-06}, {VALUE=2, NAME=2021-01-07}]
        return mapList;
    }

    @Override
    public MemberEntity login(String username, String password) {
        MemberEntity memberEntity = this.getOne(new QueryWrapper<MemberEntity>().eq("user_name", username).eq("PASSWORD", password));
        return memberEntity;
    }

}