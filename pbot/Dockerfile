FROM openjdk:17.0.2
# 维护者
MAINTAINER tengjiqi@gmail.com
# 安装 maven
RUN curl -O https://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.9.0/binaries/apache-maven-3.9.0-bin.tar.gz
RUN tar -zxvf apache-maven-3.9.0-bin.tar.gz && mv apache-maven-3.9.0 /opt/powerjob-maven && rm -rf apache-maven-3.9.0-bin.tar.gz
# 替换 maven 配置文件
RUN rm -rf /opt/powerjob-maven/conf/settings.xml
COPY maven_settings.xml /opt/powerjob-maven/conf/settings.xml
ENV PATH=/opt/powerjob-maven/bin:$PATH
# 设置时区
ENV TZ=Asia/Shanghai
ENV APP_NAME=pbot
# 传递 SpringBoot 启动参数 和 JVM参数
ENV PARAMS=""
ENV JVMOPTIONS=""
# 拷贝源码
COPY . .
# 编译
RUN mvn clean package -DskipTests
EXPOSE 7777
# 启动应用
ENTRYPOINT ["sh","-c","java $JVMOPTIONS -jar /app/target/pbot-1.0.0.jar $PARAMS"]
