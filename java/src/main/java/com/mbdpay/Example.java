package com.mbdpay;

import java.util.Map;

/**
 * 面包多Pay Java SDK 使用示例
 *
 * 运行前请替换为你自己的 app_id 和 app_key
 * 获取地址: https://mbd.pub/dev
 */
public class Example {

    public static void main(String[] args) {
        String appId = "your_app_id";
        String appKey = "your_app_key";

        Client client = new Client(appId, appKey);

        // === 1. 获取用户 openid 跳转链接 ===
        System.out.println("=== 获取 openid 跳转链接 ===");
        String url = client.getOpenidRedirectUrl("https://your-site.com/openid_callback");
        System.out.println("跳转URL: " + url);
        System.out.println();

        // === 2. 微信 JSAPI 支付 ===
        System.out.println("=== 微信 JSAPI 支付 ===");
        try {
            Map<String, Object> result = client.wxJsapi(
                "用户openid",
                "测试商品",
                100,  // 1元 = 100分
                "https://your-site.com/pay/callback",
                "ORDER_001"
            );
            System.out.println("JSAPI参数: " + result);
        } catch (MbdPayException e) {
            System.out.println("错误: " + e.getMessage());
        }
        System.out.println();

        // === 3. 微信 H5 支付 ===
        System.out.println("=== 微信 H5 支付 ===");
        try {
            Map<String, Object> result = client.wxH5(
                "测试商品",
                100,
                "ORDER_002"
            );
            System.out.println("H5支付链接: " + result.get("h5_url"));
        } catch (MbdPayException e) {
            System.out.println("错误: " + e.getMessage());
        }
        System.out.println();

        // === 4. 支付宝支付 ===
        System.out.println("=== 支付宝支付 ===");
        try {
            Map<String, Object> result = client.alipay(
                "https://your-site.com/pay/callback",
                "测试商品",
                100,
                "ORDER_003",
                null
            );
            String body = String.valueOf(result.get("body"));
            System.out.println("支付宝表单HTML: " + body.substring(0, Math.min(body.length(), 100)) + "...");
        } catch (MbdPayException e) {
            System.out.println("错误: " + e.getMessage());
        }
        System.out.println();

        // === 5. 查询订单 ===
        System.out.println("=== 查询订单 ===");
        try {
            Map<String, Object> result = client.searchOrder("ORDER_001");
            System.out.println("订单信息: " + result);
        } catch (MbdPayException e) {
            System.out.println("错误: " + e.getMessage());
        }
        System.out.println();

        // === 6. 退款 ===
        System.out.println("=== 退款 ===");
        try {
            Map<String, Object> result = client.refund("面包多订单号");
            System.out.println("退款结果: " + result);
        } catch (MbdPayException e) {
            System.out.println("错误: " + e.getMessage());
        }
    }
}
