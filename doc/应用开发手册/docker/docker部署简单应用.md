## docker部署.md [example](../../../fastboot-example/fastboot-web-example)

Dockerfile

```
FROM java:8-alpine
MAINTAINER Stylesmile<3239866994@@qq.com>
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
EXPOSE 18741
COPY target/fastboot-web-example.jar /opt/app.jar
ENTRYPOINT ["java", "-Xms128m -Xmx4g", "-jar", "/opt/app.jar"]
```

start.sh

```shell
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


```

# maven docker 插件 [example](../../../fastboot-example/fastboot-web-example-docker)

https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin

```docker
<plugin>
                <groupId>com.google.cloud.tools</groupId>
                <artifactId>jib-maven-plugin</artifactId>
                <version>3.2.1</version>
                <!--                <version>1.8.0</version>-->
                <configuration>
                    <!-- 如果设置为 true，Jib 会忽略 HTTPS 证书错误，并且可能会回退到 HTTP 作为最后的手段。强烈建议将此参数设置为false，因为 HTTP 通信未加密且对网络上的其他人可见，并且不安全的 HTTPS 并不比普通 HTTP 好-->
                    <allowInsecureRegistries>true</allowInsecureRegistries>
                    <!-- 拉取所需的基础镜像 - 这里用于运行springboot项目 -->
                    <from>
                        <image>java:8-alpine</image>
                        <!--<image>rukiy/alpine-openjdk11</image>-->
                        <!--<image>openjdk:8-jdk-alpine</image>-->
                        <!--<image>java:8u172-jre-alpine</image>-->
                    </from>
                    <!-- 最后生成的镜像配置 -->
                    <to>
                        <!-- push docer-hub官方仓库。用户名/镜像名：版本号， -->
                        <!--  <image>fastboot/fastboot</image>-->
                        <!-- 如果是私有容器镜像仓库，则使用容器的配置 前缀/命名空间/仓库名 -->
                        <!-- harbor 私服-->
                        <image>192.168.226.129/fastboot/fastboot</image>
                        <!-- 阿里云 私服-->
                        <!-- <image>registry.cn-hangzhou.aliyuncs.com/chenye-repository/fastboot</image>-->
                        <auth>
                            <!--在docker-hub或者阿里云上的账号和密码-->
                            <username>admin</username>
                            <password>Harbor12345</password>
                        </auth>
                        <tags>
                            <!--版本号-->
                            <tag>1.1.2</tag>
                        </tags>
                    </to>
                    <container>
                        <jvmFlags>
                            <jvmFlag>-Xms32m</jvmFlag>
                            <!--                            <jvmFlag>-Xmx4g</jvmFlag>-->
                            <!--                            <jvmFlag>-Duser.timezone=Asia/Shanghai</jvmFlag>-->
                        </jvmFlags>
                        <!--项目的入口类 -->
                        <mainClass>com.example.Application</mainClass>
                        <!--                        <useCurrentTimestamp>true</useCurrentTimestamp>-->
                        <ports>
                            <!--指定镜像端口 , 这里没用 docfile的操作-->
                            <port>8080</port>
                        </ports>
                        <environment>
                            <!--环境变量设置-->
                            <!-- <spring.profiles.active>prod</spring.profiles.active> -->
                            <TZ>Asia/Shanghai</TZ>
                        </environment>
                    </container>
                </configuration>
                <!--绑定到maven lifecicle-->
                <!--                <executions>-->
                <!--                    <execution>-->
                <!--                        <phase>package</phase>-->
                <!--                        <goals>-->
                <!--                            <goal>build</goal>-->
                <!--                        </goals>-->
                <!--                    </execution>-->
                <!--                </executions>-->
            </plugin>
```
