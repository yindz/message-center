# message-center

## 项目介绍
### 概述
- 独立的消息推送应用
- 实现了邮件推送、短信推送和APP消息推送
- 适配了极光推送、个推、阿里云短信、腾讯云短信、云片短信等第三方云服务，开箱即用
- 采用消息中间件或HTTP接口进行对接，便于与各类业务系统集成（包括但不限于Java/PHP/Python/JavaScript/Ruby/C#等语言开发的后端系统）

### 技术栈
- Spring Boot 2.1.x

### 编译打包
```shell script
mvn package -Pdev
mvn package -Ptest
mvn package -Pprod
```

## 使用说明
### 支持的短信服务商

#### 阿里云
- 官网: https://www.aliyun.com/product/sms

#### 腾讯云
- 官网：https://cloud.tencent.com/product/sms

#### 云片
- 官网：https://www.yunpian.com/

### 支持的APP消息推送服务商
#### 极光推送
- 官网: https://www.jiguang.cn/

#### 个推
- 官网: https://www.getui.com/cn/index.html

### 配置说明
#### 可能需要修改的参数介绍
| 参数名 | 参数用途 | 默认值 |
| ------ | --------- | -------- |
| msg.app-msg-pool-size | 用于app消息推送的线程池大小 | 4 |
| msg.sms-pool-size | 用于短信消息推送的线程池大小 | 4 |
| msg.max-sms-per-second | 每秒钟发送短信条数上限 | 10 |
| msg.email-pool-size | 用于电子邮件消息推送的线程池大小 | 4 |
| msg.jpush-enabled | 是否启用极光推送 | false |
| msg.jpush-app-key | 极光推送的appKey | 无 |
| msg.jpush-master-secret | 极光推送的密钥 | 无 |
| msg.getui-enabled | 是否启用个推 | false |
| msg.getui-app-id | 个推的appId | 无 |
| msg.getui-url | 个推的url | 无 |
| msg.getui-master-secret | 个推的密钥 | 无 |
| msg.getui-app-key | 个推的appKey | 无 |
| msg.qcloud-sms-enabled | 是否启用腾讯云短信 | false |
| msg.qcloud-sms-app-id | 腾讯云短信appId | 无 |
| msg.qcloud-sms-app-key | 腾讯云短信appKey | 无 |
| msg.aliyun-sms-enabled | 是否启用阿里云短信 | false |
| msg.aliyun-sms-region | 阿里云短信区域编码 | cn-hangzhou |
| msg.aliyun-sms-access-key | 阿里云短信accessKey | 无 |
| msg.aliyun-sms-access-secret | 阿里云短信accessSecret | 无 |
| msg.yunpian-sms-enabled | 是否启用云片短信 | false |
| msg.yunpian-sms-api-key | 云片apiKey | 无 |
| spring.mail.host | 邮件SMTP服务器IP地址或主机名 | 无 |
| spring.mail.port | 邮件SMTP服务器端口号 | 无 |
| spring.mail.username | 邮件SMTP服务器用户名 | 无 |
| spring.mail.password | 邮件SMTP服务器密码 | 无 |
| spring.mail.default-encoding | 邮件默认编码 | UTF-8 |
| spring.kafka.bootstrap-servers | Apache Kafka 主机IP地址及端口号 | 无 |
| spring.boot.admin.client.url | Spring Boot Admin 应用的URL | 无 |
| spring.boot.admin.client.username | Spring Boot Admin应用的认证用户名 | 无 |
| spring.boot.admin.client.password | Spring Boot Admin应用的认证密码 | 无 |
| server.port | 本应用HTTP端口号 | 12345 |
| server.tomcat.max-threads | 本应用内嵌tomcat线程数 | 16 |
| logging.path | 本应用日志文件的输出路径 | /tmp |

#### 注意事项
- 修改各个线程池大小会影响并发性能，实际应根据服务器硬件配置以及网络带宽等因素进行灵活调整
- 由于短信发送服务属于按量计费且受运营商管制，因此强烈建议设置 msg.max-sms-per-second 参数为一个合理的值，避免因调用方的失误造成短时间内发送大量短信
- 必须且只能开启1种app消息推送服务(msg.jpush-enabled / msg.getui-enabled 其中一项配置为 true)
- 必须且只能开启1种短信服务(msg.qcloud-sms-enabled / msg.aliyun-sms-enabled / msg.yunpian-sms-enabled 其中一项配置为 true)
- 本应用在启动后将以客户端身份向 Spring Boot Admin 服务端注册，请自行配置相应的URL、用户名、密码等信息

### 消息格式说明
#### APP消息格式
```json
{
  "messageId": "1234567891",
  "messageType": "app",
  "receiverList": ["asdfghjkl", "poiuytre", "zxcvbnm"],
  "title": "通知标题",
  "content": "内容",
  "extras": {
    "userId": 9527,
    "orderId":"9875455214575"
  },
  "callbackUrl": "http://192.168.2.101:8080/appMsgCallback"
}
```
其中：
- messageId为全局唯一的消息标识
- messageType固定为app
- receiver为设备在推送平台的别名（需要app端遵照服务商的约定进行绑定）
- extras为额外的业务参数（需与app端约定格式和参数值）
- callbackUrl为结果回调地址

#### 短信格式
```json
{
  "messageId": "1234567892",
  "messageType": "sms",
  "receiver": "手机号",
  "templateId": "10001",
  "signName": "中国移不动",
  "templateParams": {
    "clientName": "张无忌",
    "value": "100"
  },
  "callbackUrl": "http://192.168.2.101:8080/smsMsgCallback"
}
```
其中：
- messageId为全局唯一的消息标识
- messageType固定为sms
- receiver为接收者手机号码
- templateId为短信模板编号
- signName为短信署名（上文已介绍）
- templateParams为额外的业务参数（一般为短信推送服务商平台上定义的短信内容变量）
- callbackUrl为结果回调地址

#### 邮件格式
```json
{
  "messageId":"1234567893",
  "messageType":"email",
  "receiver":"john@example.com",
  "subject":"这里是邮件标题",
  "htmlContent":true,
  "content":"这里是邮件正文",
  "attachmentList": ["/app/files/资料1.pdf", "/app/files/资料2.doc"],
  "callbackUrl": "http://192.168.2.101:8080/emailMsgCallback"
}
```
其中：
- messageId为全局唯一的消息标识
- messageType固定为email
- receiver为接收者邮箱地址
- subject: 邮件标题
- htmlContent: 邮件正文是否为HTML格式
- content: 邮件正文
- attachmentList: 邮件附件的完整路径，允许有多个
- callbackUrl为结果回调地址

### APP消息推送集成（两种方式）
- 将待推送的消息JSON字符串存入Kafka队列中，topic名称：app-msg-topic
- 发起 HTTP POST 请求到接口 http://IP:PORT/message/submitAppMsg，Content-type: application/json; charset=utf-8

### 短信推送集成
#### 注意事项
- 短信配置中的参数 signName 为短信署名，必须先在服务商后台提交审核通过后再填入
- 短信模板需要在服务商后台提交审核通过后，才能获得模板编号
- 其它参数可通过服务商的后台查询得到
- 其它事项，需要查询服务商的接入文档

#### 对接（两种方式）
- 将待推送的消息JSON字符串存入Kafka队列中，topic名称：sms-msg-topic
- 发起 HTTP POST 请求到接口 http://IP:PORT/message/submitSms，Content-type: application/json; charset=utf-8

### 邮件推送集成（两种方式）
- 将待推送的消息JSON字符串存入Kafka队列中，topic名称：email-msg-topic
- 发起 HTTP POST 请求到接口 http://IP:PORT/message/submitEmail，Content-type: application/json; charset=utf-8

### 结果回调
#### 说明
三种类型的消息（短信/app/邮件）在推送完毕后都支持回调。消息的生产者可以根据回调数据来做后续的业务处理。
message-center 将使用 HTTP POST 方式将结果数据推送到相对应的 callbackUrl。

#### 回调参数格式
```json
{
  "messageId": "1234567890",
  "success": false,
  "errorMsg": "网络连接超时"
}
```
- messageId 为唯一的消息标识，字符串
- success 为推送成功标识，布尔型
- errorMsg 推送失败时会返回具体原因

#### 如果不需要回调，直接把 callbackUrl 留空即可