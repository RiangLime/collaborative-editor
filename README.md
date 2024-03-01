# QuickStart

## 1. 加载maven依赖
## 2. 配置数据库配置
```
默认配置链接10.1.1.106
可不用修改
```
## 3. 开启服务
```
package cn.rianglime.collaborativeeditor中的CollaborativeEditorApplication为主类
直接启动即可
```
## 4. 在网页版WebSocketClient端进行测试
以下为两个可供使用的工具页面
```
http://www.jsons.cn/websocket/
https://www.bejson.com/httputil/websocket/
```
### 4.1 填写地址并连接
```
ws://localhost:8094/editor
```
### 4.2 首次发送请求
首次发送请求后会根据文章ID进行分组
此步骤拟由前端在webSocket连接成功后调用Query进行远程副本初始化
由于netty的websocket的连接URL不能携带参数就用这种方式
目前库里只有两篇文章,请不要随意改动articleId的值[1/2]
```json
{"articleId":1, "isQuery":true}
```
### 4.3 发送请求
在位置为1  **operationType=1**  的地方  **插入**  hello
```json
{ "articleId":1, "isQuery":false, "operation":{ "operationType":1, "position":1, "content":"hello" } }
```

在位置为1  **operationType=2**  的地方  **删除**  hello

如果数据匹配不上将不进行操作
```json
{ "articleId":1, "isQuery":false, "operation":{ "operationType":1, "position":1, "content":"hello" } }
```
