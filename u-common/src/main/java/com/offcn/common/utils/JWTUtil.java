package com.offcn.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Date;

//用于小程序登录及验证生成令牌（签名）
public class JWTUtil {
    public static final String SECRET_KEY = "u-member-offcn123"; //秘钥
    public static final long TOKEN_EXPIRE_TIME = 5 * 60 * 1000; //token过期时间
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 10 * 60 * 1000; //refreshToken过期时间
    private static final String ISSUER = "offcn"; //签发人

    //生成签名
    public static String generateToken(String username){
        Date now=new Date();
        //指定加密算法，加密规则
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY); //算法

        String token = JWT.create()
                .withIssuer(ISSUER)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + TOKEN_EXPIRE_TIME)) //签名过期时间
                .withClaim("username", username)
                .sign(algorithm);
        return token;
    }

    //验证（解密）签名
    public static boolean verify(String token){
        try {
            //指定加密算法
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY); //算法
            //创建签名校验对象
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            //校验token
            verifier.verify(token);
            return true;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (JWTVerificationException e) {
            e.printStackTrace();
        }
        return false;
    }

    //从token获取用户名
    public static String getUsername(String token){
        try {
            String username = JWT.decode(token).getClaim("username").asString();
            return username;
        } catch (JWTDecodeException e) {
            e.printStackTrace();
        }

        return "";
    }
}
