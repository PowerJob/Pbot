#!/bin/bash
# 部署守护程序
read -r -p "native image?(Y or N):" native
echo "================== 关闭老应用 =================="
docker stop pbot
echo "================== 删除老容器 =================="
docker container rm pbot
echo "================== 删除老镜像 =================="
docker rmi -f tjqq/pbot:latest
echo "================== 重新构建 Docker =================="
if [ "$native" = "y" ] || [  "$native" = "Y" ]; then
  docker build -f Dockerfile_native -t tjqq/pbot:latest . || exit
else
  docker build -t tjqq/pbot:latest . || exit
fi
echo "================== 准备启动 PBot =================="
docker run -d \
       --restart=always \
       --net=host \
       --name pbot \
       -m 128M \
       --memory-swap 256M \
       -p 7777:7777 \
       -e PARAMS="--spring.profiles.active=production" \
       -e JVMOPTIONS="-server" \
       -v ~/powerjob-data/pbot:/root \
       -v ~/.m2:~/.m2 \
       tjqq/pbot:latest

#-m,--memory                  内存限制，格式是数字加单位，单位可以为 b,k,m,g。最小为 4M
#--memory-swap                内存+交换分区大小总限制。格式同上。必须必-m设置的大
#--memory-reservation         内存的软性限制。格式同上
#--oom-kill-disable           是否阻止 OOM killer 杀死容器，默认没设置
#--oom-score-adj              容器被 OOM killer 杀死的优先级，范围是[-1000, 1000]，默认为 0
#--memory-swappiness          用于设置容器的虚拟内存控制行为。值为 0