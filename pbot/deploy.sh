#!/bin/bash
# 部署守护程序

echo "================== 关闭老应用 =================="
docker stop pbot
echo "================== 删除老容器 =================="
docker container rm pbot
echo "================== 删除老镜像 =================="
docker rmi -f tjqq/pbot:latest
echo "================== 重新构建 JAR =================="
mvn clean package -DskipTests
echo "================== 重新构建 Docker =================="
docker build -t tjqq/pbot:latest . || exit
echo "================== 准备启动 PBot =================="
docker run -d \
       --restart=always \
       --net=host \
       --name pbot \
       -p 7777:7777 \
       -e PARAMS="--spring.profiles.active=production" \
       -e JVMOPTIONS="-Xms64M -Xmx64M -server" \
       -v ~/powerjob-data/pbot:/root \
       tjqq/pbot:latest
