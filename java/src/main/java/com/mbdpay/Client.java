package com.mbdpay;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 面包多Pay客户端
 *
 * <p>使用示例:
 * <pre>{@code
 * Client client = new Client("your_app_id", "your_app_key");
 *
 * // 微信JSAPI支付
 * Map<String, Object> result = client.wxJsapi(
 *     "用户openid", "商品描述", 100, "https://callback.url", "订单号");
 * }</pre>
 */
public class Client {

    private static final String DEFAULT_DOMAIN = "https://api.mianbaoduo.com";
    private static final int DEFAULT_TIMEOUT = 15000;
    private static final String OPENID_BASE_URL = "https://mbd.pub/openid";

    private final String appId;
    private final String appKey;
    private final String domain;
    private final int timeout;
    private final Gson gson;

    /**
     * 创建客户端
     *
     * @param appId  面包多Pay app_id
     * @param appKey 面包多Pay app_key
     */
    public Client(String appId, String appKey) {
        this(appId, appKey, DEFAULT_DOMAIN, DEFAULT_TIMEOUT);
    }

    /**
     * 创建客户端
     *
     * @param appId   面包多Pay app_id
     * @param appKey  面包多Pay app_key
     * @param domain  API域名
     * @param timeout 请求超时时间（毫秒）
     */
    public Client(String appId, String appKey, String domain, int timeout) {
        this.appId = appId;
        this.appKey = appKey;
        this.domain = domain;
        this.timeout = timeout;
        this.gson = new Gson();
    }

    /**
     * 获取用户openid的跳转URL
     * 文档: https://doc.mbd.pub/api/huo-qu-yong-hu-openid
     *
     * @param targetUrl 回调URL
     * @return 跳转URL
     */
    public String getOpenidRedirectUrl(String targetUrl) {
        return OPENID_BASE_URL + "?app_id=" + urlEncode(appId)
                + "&target_url=" + urlEncode(targetUrl);
    }

    /**
     * 微信JSAPI支付
     * 文档: https://doc.mbd.pub/api/wei-xin-zhi-fu
     *
     * @param openid       用户openid
     * @param description  商品描述
     * @param amountTotal  订单金额（单位：分）
     * @param callbackUrl  支付完成后跳转的URL
     * @param outTradeNo   外部系统订单号（可为null）
     * @return JSAPI支付参数
     */
    public Map<String, Object> wxJsapi(String openid, String description,
                                        int amountTotal, String callbackUrl,
                                        String outTradeNo) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("openid", openid);
        params.put("description", description);
        params.put("amount_total", amountTotal);
        params.put("callback_url", callbackUrl);
        if (outTradeNo != null) {
            params.put("out_trade_no", outTradeNo);
        }
        return post("/release/wx/prepay", params);
    }

    /**
     * 微信H5支付
     * 文档: https://doc.mbd.pub/api/wei-xin-h5-zhi-fu
     *
     * @param description 商品描述
     * @param amountTotal 订单金额（单位：分）
     * @param outTradeNo  外部系统订单号（可为null）
     * @return 包含h5_url的响应
     */
    public Map<String, Object> wxH5(String description, int amountTotal,
                                     String outTradeNo) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("channel", "h5");
        params.put("description", description);
        params.put("amount_total", amountTotal);
        if (outTradeNo != null) {
            params.put("out_trade_no", outTradeNo);
        }
        return post("/release/wx/prepay", params);
    }

    /**
     * 支付宝支付
     * 文档: https://doc.mbd.pub/api/zhi-fu-bao-zhi-fu
     *
     * @param url         支付后跳转URL
     * @param description 商品描述
     * @param amountTotal 订单金额（单位：分）
     * @param outTradeNo  外部系统订单号（可为null）
     * @param callbackUrl 回调URL（可为null）
     * @return 包含body(HTML表单)的响应
     */
    public Map<String, Object> alipay(String url, String description,
                                       int amountTotal, String outTradeNo,
                                       String callbackUrl) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("url", url);
        params.put("description", description);
        params.put("amount_total", amountTotal);
        if (outTradeNo != null) {
            params.put("out_trade_no", outTradeNo);
        }
        if (callbackUrl != null) {
            params.put("callback_url", callbackUrl);
        }
        return post("/release/alipay/pay", params);
    }

    /**
     * 退款
     * 文档: https://doc.mbd.pub/api/tui-kuan
     *
     * @param orderId 订单号
     * @return 退款结果
     */
    public Map<String, Object> refund(String orderId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("order_id", orderId);
        return post("/release/main/refund", params);
    }

    /**
     * 查询订单
     * 文档: https://doc.mbd.pub/api/ding-dan-cha-xun
     *
     * @param outTradeNo 外部系统订单号
     * @return 订单信息
     */
    public Map<String, Object> searchOrder(String outTradeNo) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("out_trade_no", outTradeNo);
        return post("/release/main/search_order", params);
    }

    // ==================== 内部方法 ====================

    private Map<String, Object> buildRequest(Map<String, Object> params) {
        Map<String, Object> body = new LinkedHashMap<>(params);
        body.put("app_id", appId);
        body.values().removeIf(java.util.Objects::isNull);
        String sign = Sign.sign(body, appKey);
        body.put("sign", sign);
        return body;
    }

    private Map<String, Object> post(String path, Map<String, Object> params) {
        Map<String, Object> body = buildRequest(params);
        String jsonBody = gson.toJson(body);

        try {
            URI uri = new URI(domain + path);
            HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            BufferedReader reader;
            if (responseCode >= 200 && responseCode < 300) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            } else {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String responseBody = response.toString();
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> result = gson.fromJson(responseBody, type);

            if (result != null && result.containsKey("error") && result.get("error") != null) {
                throw new MbdPayException(String.valueOf(result.get("error")));
            }

            return result;
        } catch (IOException e) {
            throw new MbdPayException("HTTP请求失败: " + e.getMessage(), e);
        } catch (java.net.URISyntaxException e) {
            throw new MbdPayException("URL格式错误: " + e.getMessage(), e);
        }
    }

    private static String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
