#FROM adoptopenjdk/openjdk8
FROM java:8-alpine
MAINTAINER Stylesmile<3239866994@@qq.com>
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
EXPOSE 18741
COPY target/fastboot-web-example.jar /opt/app.jar
#ENTRYPOINT ["java", "-Xms128m", "-jar", "/opt/app.jar"]
ENTRYPOINT ["java", "-Xms128m", "-jar", "/opt/app.jar"]