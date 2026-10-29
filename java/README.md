# 面包多Pay Java SDK

面向个人的支付集成平台 [面包多Pay](https://mbd.pub) 的 Java SDK。

## 安装

### Maven

```xml
<dependency>
    <groupId>com.mbdpay</groupId>
    <artifactId>mbdpay-sdk</artifactId>
    <version>0.1.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'com.mbdpay:mbdpay-sdk:0.1.0'
```

### 手动编译

```bash
git clone https://github.com/yangming/mbdpay-java.git
cd mbdpay-java
mvn clean package
```

## 使用

### 初始化

```java
import com.mbdpay.Client;

Client client = new Client("your_app_id", "your_app_key");
```

### 微信 JSAPI 支付

```java
Map<String, Object> result = client.wxJsapi(
    "用户openid",       // openid
    "商品描述",          // description
    100,                // amountTotal 金额，单位：分
    "https://callback", // callbackUrl
    "ORDER_001"         // outTradeNo（可为null）
);
// result 包含: appId, timeStamp, nonceStr, package, signType, paySign
```

### 微信 H5 支付

```java
Map<String, Object> result = client.wxH5("商品描述", 100, "ORDER_001");
// result 包含: h5_url
```

### 支付宝支付

```java
Map<String, Object> result = client.alipay(
    "https://callback", // url
    "商品描述",          // description
    100,                // amountTotal
    "ORDER_001",        // outTradeNo（可为null）
    null                // callbackUrl（可为null）
);
// result 包含: body (HTML表单)
```

### 退款

```java
Map<String, Object> result = client.refund("面包多订单号");
```

### 查询订单

```java
Map<String, Object> result = client.searchOrder("你的订单号");
```

### 获取用户 OpenID 跳转链接

```java
String url = client.getOpenidRedirectUrl("https://your-site.com/openid_callback");
```

## 签名算法

参数按 key 排序后拼接为 `key1=value1&key2=value2`，末尾追加 `&key=APP_KEY`，取 MD5。

文档：https://doc.mbd.pub/api/qian-ming-suan-fa

## License

MIT
