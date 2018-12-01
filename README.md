# message-center

## 项目介绍
消息推送

## 软件架构
基于SpringBoot和ActiveMQ。实现了短信推送和APP消息推送。

## 使用说明
### 支持的短信服务商
#### 云片
- ID: YunpianSmsService
- 官网: https://www.yunpian.com/

#### 阿里云大鱼
- ID: AliyunSmsService
- 官网: https://www.aliyun.com/product/sms

#### 腾讯云短信
- ID: QcloudSmsService
- 官网: https://cloud.tencent.com/product/sms

#### 服务商ID列表查询
- 完整的服务商ID列表可通过以下URL查询： http://localhost:12345/provider/smsProviderList 

### 支持的APP消息推送服务商
### 极光推送
- ID: JPushService
- 官网: https://www.jiguang.cn/

### 个推
- ID: GetuiPushService
- 官网: https://www.getui.com/cn/index.html

#### 服务商ID列表查询
- 完整的服务商ID列表可通过以下URL查询： http://localhost:12345/provider/appMsgProviderList

### APP消息推送接入
将待推送的消息存入ActiveMQ队列中，队列名称须与配置文件中的 msg.queueName 保持一致。
消息为JSON文本，格式如下：
```json
{
  "messageId":"123456789",
  "messageType":"app",
  "receiver":"asdfghjkl",
  "title":"通知标题",
  "content":"内容",
  "provider":"JPushService",
  "extras": {
    "userId":9527,
    "orderId":"9875455214575"
  }
}
```
其中：
- messageId为全局唯一的消息标识
- messageType固定为app
- receiver为设备在推送平台的别名（需要app端遵照服务商的约定进行绑定）
- provider为推送服务商的ID（上文已介绍）
- extras为额外的业务参数（需与app端约定格式和参数值）

### 短信推送接入
#### 注意事项
阿里大鱼和腾讯云短信配置中的参数 signName 为短信署名，必须先在服务商后台提交审核通过后再填入。其它参数可通过服务商的后台查询得到。
短信模板也需要在服务商后台提交审核通过后，才能获得模板编号。其它事项，需要查询服务商的接入文档。

#### 对接
将待推送的消息存入ActiveMQ队列中，队列名称须与配置文件中的 sms.queueName 保持一致。
消息为JSON文本，格式如下：
```json
{
  "messageId":"123456789",
  "messageType":"sms",
  "receiver":"手机号",
  "content":"内容",
  "provider":"QcloudSmsService",
  "extras": {
    "templateCode":"123456789",
    "orderId":"9875455214575"
  }
}
```
其中：
- messageId为全局唯一的消息标识
- messageType固定为sms
- receiver为接收者手机号码
- content为短信内容（仅限于支持直接推送文字的服务商；对于强制采用模板编号的服务商，该字段无意义，留空即可）
- provider为短信推送服务商的ID（上文已介绍）
- extras为额外的业务参数（一般为短信推送服务商平台上定义的短信模板编号和短信内容变量等）
