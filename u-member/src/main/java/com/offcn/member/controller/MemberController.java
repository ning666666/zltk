package com.offcn.member.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.offcn.common.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.offcn.member.entity.MemberEntity;
import com.offcn.member.service.MemberService;
import com.offcn.common.utils.PageUtils;
import com.offcn.common.utils.R;



/**
 * 会员-会员表
 *
 * @author jialei
 * @email 1694601613@qq.com
 * @date 2022-08-15 19:24:44
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    //根据开始时间，结束时间，统计每日注册账号
    @RequestMapping("countAccountCreate")
    public R countAccountCreate(String beginTime, String endTime) {
        List<Map<String, Object>> mapList = memberService.countByDateTime(beginTime, endTime);
        return R.ok().put("mapList", mapList);
    }
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

 /*   //登录
    @PostMapping("/login")
    public R login(String username,String password){
        MemberEntity memberEntity = memberService.login(username, password);

        if(memberEntity!=null) {
            String token = JWTUtil.generateToken(memberEntity.getUserName());
            //生成refreshToken,2cfd1d01-8e71-43ed-9217-926275ccc001,bondHashOPS
            String refreshToken = UUID.randomUUID().toString().replace("-", "");
            stringRedisTemplate.opsForHash().put(refreshToken, "token", token);
            stringRedisTemplate.opsForHash().put(refreshToken, "username", username);
            //设置token的过期时间
            // import java.util.concurrent.TimeUnit(时间单位毫秒);
            stringRedisTemplate.expire(refreshToken, JWTUtil.REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

            return R.ok().put("token",token).put("refreshToken",refreshToken);

        }else {
            return R.error("账号密码错误");

        }
    }*/
// 登录
 @RequestMapping("/login")
 public R login(@RequestBody Map map){
     // 验证数据有效性
     // 调用service 验证是否登录成功
     String username = (String)map.get("username");
     String password = (String)map.get("password");
     System.out.println("ccc:" + username);
     System.out.println("ddd:" + password);
     MemberEntity memberEntity = memberService.login(username, password);

     System.out.println("ccc:" + memberEntity);

     if (memberEntity != null) {
         String token = JWTUtil.generateToken(memberEntity.getUserName());
         //生成refreshToken
         String refreshToken = UUID.randomUUID().toString().replace("-", "");
         stringRedisTemplate.opsForHash().put(refreshToken, "token", token);
         stringRedisTemplate.opsForHash().put(refreshToken, "username", username);
         //设置token的过期时间
         // import java.util.concurrent.TimeUnit;
         stringRedisTemplate.expire(refreshToken, JWTUtil.REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

         return R.ok().put("token", token).put("refreshToken", refreshToken);

     } else {
         return R.error("账号密码错误");

     }

 }
    public static void main(String[] args) {
        System.out.println("UUID.randomUUID().toString() = " + UUID.randomUUID().toString());
    }

    //刷新token
    @PostMapping("/refreshtoken")
    public R refreshToken(String refreshToken){
        //根据refreshToken从redis读取用户名
        String username= (String) stringRedisTemplate.boundHashOps(refreshToken).get("username");
        // import org.springframework.util.StringUtils;
        if(StringUtils.isEmpty(username)){
            return R.error("刷新token失败");
        }
        //根据用户名生成新的token
        String token = JWTUtil.generateToken(username);
        //更新token到redis
        stringRedisTemplate.boundHashOps(refreshToken).put("token",token);
        return R.ok().put("token",token).put("refreshToken",refreshToken);
    }
}
