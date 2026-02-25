package com.buyi.common.oauth.util;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * @description AccessToken处理
 */
@Slf4j
public class AccessTokenUtils {

    /**
     * 随机数分隔符
     */
    public static final String RDM_SEP = "#";


    /**
     * AccessToken 签名
     *
     * @param claims
     * @param privateKey
     * @return
     */
    public static JwtClaimsSet.Builder signAccessToken(JwtClaimsSet.Builder claims, String privateKey) {
        return signAccessToken(claims, privateKey, null);
    }

    /**
     * 构建toekm加密信息
     *
     * @param claims
     * @param external
     * @return
     */
    public static JwtClaimsSet.Builder signAccessToken(JwtClaimsSet.Builder claims, String privateKey, String external) {
        String uuIdStr = UUID.randomUUID().toString();
        long currentTimeMillis = System.currentTimeMillis();

        String rdmSource = uuIdStr + RDM_SEP + claims.build().getExpiresAt().getEpochSecond() + RDM_SEP + currentTimeMillis;
        String rdmTarget = RSAUtils.encryptByPriKey(rdmSource, privateKey);
        String signSource = uuIdStr + RDM_SEP + currentTimeMillis;
        String signature = "";
        try {
            signature = RSAUtils.sign(signSource.getBytes(), Base64Decoder.decode(privateKey));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        claims.claim("rdm", rdmTarget);
        claims.claim("sign", signature);
        if (StrUtil.isNotBlank(external)) {
            claims.claim("external", RSAUtils.encryptByPriKey(external, privateKey));
        }

        return claims;
    }

    /**
     * AccessToken 验签
     *
     * @param authorizationToken
     * @param publicKey
     * @return 是否校验成功
     */
    public static boolean verifyAccessToken(String authorizationToken, String publicKey) {
        String accessToken = authorizationToken.replace("Bearer ", "");
        JWT jwtToken = JWTUtil.parseToken(accessToken);
        JWTPayload jwtPayload = jwtToken.getPayload();

        String rdm = jwtPayload.getClaim("rdm") + "";
        String sign = jwtPayload.getClaim("sign") + "";
        //解密
        String rdmSource = "";
        try {
            rdmSource = RSAUtils.decryptByPubKey(rdm, publicKey);
        } catch (Exception e) {
            log.info("rdm解密失败，rdm=" + rdm);
        }
        String[] rdmData = rdmSource.split(RDM_SEP);
        long exp = Long.parseLong(rdmData[1]);
        long now = Instant.now().getEpochSecond();
        if (exp < now) {
            log.info("accessToken已过期，exp=" + exp);
            return false;
        }
        String signSource = rdmData[0] + RDM_SEP + rdmData[2];
        //验签
        boolean verifyResult = false;
        try {
            verifyResult = RSAUtils.verify(signSource.getBytes(), Base64Decoder.decode(sign), Base64Decoder.decode(publicKey));
        } catch (Exception e) {
            log.info("accessToken验签异常，accessToken=" + accessToken, e);
        }
        if (!verifyResult) {
            log.info("accessToken验签不通过，accessToken=" + accessToken);
            return false;
        }

        return true;
    }

    @SneakyThrows
    public static JSONObject getJwtPayload() {
        JSONObject jsonObject = null;
        try {
            String payload = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("payload");
            if (StrUtil.isNotBlank(payload)) {
                jsonObject = JSONUtil.parseObj(URLDecoder.decode(payload, StandardCharsets.UTF_8.name()));
            }
            return jsonObject;
        } catch (NullPointerException e) {
            return null;
        }
    }

    @SneakyThrows
    public static String getUserId() {
        if (Objects.isNull(getJwtPayload())) {
            return "";
        }
        return getJwtPayload().getStr("userId");
    }

    @SneakyThrows
    public static String getTenantId() {
        String tenantId = "";
        try {
            if (RequestContextHolder.getRequestAttributes() != null)
                tenantId = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("tenantId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tenantId;
    }

    public static void main(String[] args) throws Exception {
        String str = "hello world!";
        String bytes = RSAUtils.encryptByPriKey(str, RSAUtils.privateKeyBase64Default);
        System.out.println("私钥加密后的内容 " + bytes);
        String bytes1 = RSAUtils.decryptByPubKey(bytes, RSAUtils.publicKeyBase64Default);
        System.out.println("公钥解密后的内容 " + bytes1);


        String s = RSAUtils.encryptByPubKey(str, RSAUtils.publicKeyBase64Default);
        System.out.println("公钥加密后的内容 " + s);
        String bytes2 = RSAUtils.decryptByPriKey(s, RSAUtils.privateKeyBase64Default);
        System.out.println("私钥解密后的内容 " + bytes2);


    }


}
