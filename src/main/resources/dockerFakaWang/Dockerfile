# 基础镜像
FROM openjdk:17-jdk-slim

# 设定时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 添加应用的jar包到容器中
COPY fakawang.jar /app.jar

# 暴露应用的端口，根据需要进行调整
EXPOSE 8088

# 设置容器启动时执行的命令
ENTRYPOINT ["java", "-jar", "/app.jar"]
