FROM openjdk:17.0.2
# 维护者
MAINTAINER tengjiqi@gmail.com
# 设置时区
ENV TZ=Asia/Shanghai
ENV APP_NAME=pbot
# 传递 SpringBoot 启动参数 和 JVM参数
ENV PARAMS=""
ENV JVMOPTIONS=""
# 将应用 jar 包拷入 docker
COPY target/pbot-*.jar /pbot.jar
# 暴露端口（HTTP + AKKA）
EXPOSE 8081 27777
# 启动应用
ENTRYPOINT ["sh","-c","java $JVMOPTIONS -jar /pbot.jar $PARAMS"]
