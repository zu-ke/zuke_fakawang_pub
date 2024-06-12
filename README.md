# fakawang
> 售卖卡密等类似商品，对接蓝兔支付平台或者易支付平台，自动收款到账，
并且通过邮件的方式发送卡密，保证安全

## 技术选型
> SpringBoot, MySQL, MybatisPlus, vue3, SpringBoot整合邮件服务

# Docker部署
## 前置工作

1. 修改application.yml文件中的**数据库连接参数**和**lantu.mchId**、**lantu.notifyUrl**、**lantu.key**参数(蓝兔支付参数)【其他部分如德鲁伊面板账户等自行修改，非强制】
3. maven插件先`clean`在`package`
4. 服务器提前安装docker环境
5. 前往目录并且创建文件夹(eg)
```
cd /home && mkdir fakawang && cd fakawang
```
6. 上传Dockerfile文件和fakawang.jar包到/home/fakawang/

## Dockerfile部署
1. 运行命令
```
docker build -t fakawang .
```
2. 运行命令
```
docker run -d --name fakawang -p 65507:8088 fakawang
```

## docker-compose部署
运行命令
```
docker-compose up -d
```
