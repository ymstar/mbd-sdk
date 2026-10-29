# 面包多Pay 多语言 SDK & 插件

面向个人的支付集成平台 [面包多Pay](https://mbd.pub) 的多语言 SDK 和应用插件集合。

## 开发者激励计划

| 方向 | 奖励 | 要求 |
|------|------|------|
| 语言 SDK 开发 | **100 元/个** | 率先开发、可运行、无明显安全问题 |
| 应用场景/插件开发 | **200 元/个**（限 20 个） | 开发并开源、可运行、无明显安全问题 |

详情：https://doc.mbd.pub/kai-fa-zhe-zhi-chi/kai-fa-zhe-ji-li-ji-hua

## 本项目成果

### 语言 SDK（各已独立开源）

| 语言 | 仓库 | 包管理器 | 测试 |
|------|------|---------|------|
| Ruby | [ymstar/mbdpay-ruby](https://github.com/ymstar/mbdpay-ruby) | RubyGems | ✅ |
| Rust | [ymstar/mbdpay-rust](https://github.com/ymstar/mbdpay-rust) | crates.io | ✅ |
| C# (.NET) | [ymstar/mbdpay-csharp](https://github.com/ymstar/mbdpay-csharp) | NuGet | ✅ |
| TypeScript | [ymstar/mbdpay-ts](https://github.com/ymstar/mbdpay-ts) | npm | ✅ |
| Java | 本仓库 `java/` | Maven | ✅ |

### WordPress 支付插件（独立开源）

| 插件 | 仓库 | 安装 |
|------|------|------|
| WordPress | [ymstar/mbdpay-wordpress](https://github.com/ymstar/mbdpay-wordpress) | [下载 ZIP](https://github.com/ymstar/mbdpay-wordpress/releases/latest) |

## 平台已有 SDK（非本项目）

| 语言 | 开发者 | 地址 |
|------|--------|------|
| PHP | 白宦成 | https://github.com/bestony/mbdpay |
| Python | shaweb | https://github.com/shaoxyz/mbd_pay |
| Go | datewu | https://github.com/datewu/mbdpay |
| Node.js | oott123 | https://github.com/oott123/node-mbdpay |

## API 概览

- **API域名**: `https://api.mianbaoduo.com`
- **签名算法**: 参数按key排序 → 拼接为`key1=value1&key2=value2` → 追加`&key=APP_KEY` → MD5
- **接口列表**:
  - 微信JSAPI支付: `POST /release/wx/prepay`
  - 微信H5支付: `POST /release/wx/prepay` (channel=h5)
  - 支付宝支付: `POST /release/alipay/pay`
  - 退款: `POST /release/main/refund`
  - 查询订单: `POST /release/main/search_order`
  - 获取OpenID: `GET https://mbd.pub/openid`

## License

MIT
