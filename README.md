# go-cqhttp-java
一个基于go-cqhttp开发的java版本QQ机器人（ws协议）

第一步：从 https://github.com/Mrs4s/go-cqhttp 获取QQ机器人的go版本框架并build，或者从 https://github.com/Mrs4s/go-cqhttp/releases 直接获取编译版本

第二步：启动框架，选择ws协议，建议本项目和go-cqhttp在同一台服务器运行，然后取消go-cqhttp端口的公网访问权限

第三步：修改本项目的ws链接，java启动 APP.main

api文档 https://docs.go-cqhttp.org/event/