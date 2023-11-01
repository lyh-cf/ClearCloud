package com.clearcloud.base.utils;


import com.clearcloud.base.model.StatusCodeEnum;
import io.jsonwebtoken.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

public class JwtUtil {
    //过期时间
    private static final long access_expire = 60 * 60 * 1000;
    private static final long refresh_expire = 2  * 60 * 60 * 1000;
    //秘钥
    private static final String secret = "lyh_wj_pp";

    //加密
    public static String createAccessToken(Integer userId) {
        JwtBuilder jwtBuilder = Jwts.builder();
        return jwtBuilder
                //头信息header
                .setHeaderParam("typ", "JWT")//Token类型
                .setHeaderParam("alg", "HS256")//加密算法
                //载荷payload,信息
                .claim("userId", "" + userId)
                //主题
                .setSubject("clear-cloud:access_token")
                //有效时间
                .setExpiration(new Date(System.currentTimeMillis() + access_expire))
                //ID
                .setId(UUID.randomUUID().toString())
                //signature签名
                .signWith(SignatureAlgorithm.HS256, secret)
                //用.拼接
                .compact();
    }

    public static String createRefreshToken(Integer userId) {
        JwtBuilder jwtBuilder = Jwts.builder();
        return jwtBuilder
                //头信息header
                .setHeaderParam("typ", "JWT")//Token类型
                .setHeaderParam("alg", "HS256")//加密算法
                //载荷payload,信息
                .claim("userId", "" + userId)
                //主题
                .setSubject("clear-cloud:refresh_token")
                //有效时间
                .setExpiration(new Date(System.currentTimeMillis() + refresh_expire))
                //ID
                .setId(UUID.randomUUID().toString())
                //signature签名
                .signWith(SignatureAlgorithm.HS256, secret)
                //用.拼接
                .compact();
    }

    public static Integer getUserId(HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // 从第8个字符开始截取，去掉"Bearer "前缀
            // 在这里对token进行进一步的处理或验证
            JwtParser jwtParser = Jwts.parser();
            Jws<Claims> claimsJws = jwtParser.setSigningKey(secret).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return Integer.valueOf((String)claims.get("userId"));
        }
        else throw new RuntimeException("Token获取失败");
    }

    public static String checkToken(String token) {
        //确保token此时一定不为空
        JwtParser jwtParser = Jwts.parser();
        try {
            Jws<Claims> claimsJws = jwtParser.setSigningKey(secret).parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            return StatusCodeEnum.EXPIRED_JWT_EXCEPTION_CODE.getMessage();//JWT过期
        } catch (Exception e) {
            return StatusCodeEnum.UNAUTHORIZED_EXCEPTION_CODE.getMessage();//解析异常
        }
        return null;
    }

}