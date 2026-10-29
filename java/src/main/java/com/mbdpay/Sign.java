package com.mbdpay;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 面包多Pay签名算法实现
 * 文档: https://doc.mbd.pub/api/qian-ming-suan-fa
 */
public class Sign {

    /**
     * 对参数进行签名
     *
     * @param attributes 请求参数（不含sign）
     * @param appKey     面包多Pay app_key
     * @return MD5签名字符串
     */
    public static String sign(Map<String, Object> attributes, String appKey) {
        List<String> keys = new ArrayList<>(attributes.keySet());
        Collections.sort(keys);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            if (i > 0) {
                sb.append("&");
            }
            String key = keys.get(i);
            sb.append(key).append("=").append(attributes.get(key));
        }
        sb.append("&key=").append(appKey);

        return md5Hex(sb.toString());
    }

    private static String md5Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : digest) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }
}
