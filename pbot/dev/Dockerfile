FROM openjdk:17.0.2
MAINTAINER tengjiqi@gmail.com
ENV TZ=Asia/Shanghai
ENV APP_NAME=pbot

# 传递 SpringBoot 启动参数 和 JVM参数
ENV PARAMS=""
ENV JVMOPTIONS=""

COPY pbot.jar /pbot.jar
EXPOSE 7777

ENTRYPOINT ["sh","-c","java $JVMOPTIONS -jar /pbot.jar $PARAMS"]