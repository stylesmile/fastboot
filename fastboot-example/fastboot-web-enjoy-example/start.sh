#/bin/bash
## 服务端fastboot 启动脚本
# docker 服务别名
SERVER_NAME='fastboot-server'
# 镜像名称
SERVER_IMAGE='fastboot/fastboot-server:latest'
# 日志输入位置
LOG_PATH=/var/logs/$SERVER_NAME
# 外部yml配置文件位置
# YML_PATH=$BATH_PATH/knowledge/server/conf
# 端口   此处端口需要和配置文件中的端口保持一致
PORT="8080"
docker build -t $SERVER_IMAGE .
C_ID=$(docker ps -aq -f name=$SERVER_NAME)
stop(){
         docker stop $C_ID
         docker rm $C_ID
         docker rmi $C_ID
}

if [ -n "$C_ID" ]; then
 	echo "start stop $SERVER_NAME"
	stop
fi

#docker run -p $PORT:$PORT --name -v $LOG_PATH:/opt/fastboot $SERVER_NAME -e SPRING_PROFILES_ACTIVE="dev" --restart=always  -d $SERVER_IMAGE
docker run -p $PORT:$PORT --name $SERVER_NAME -e SPRING_PROFILES_ACTIVE="dev" --restart=always -d $SERVER_IMAGE

