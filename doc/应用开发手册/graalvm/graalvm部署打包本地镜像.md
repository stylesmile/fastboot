# 快速开始 graalvm本地镜像

GraalVM ce Java11 Linux [下载地址](https://github.com/graalvm/graalvm-ce-builds/releases/tag/vm-22.2.0)

## 解压

tar -xzf graalvm-ce-java11-linux-amd64-22.2.0.tar.gz

## 配置环境变量

export JAVA_HOME=[GraalVM根目录的路径]
export PATH=$PATH:$JAVA_HOME/bin

## 让修改后的环境变量生效

source /etc/profile

## 验证是否生效

java -version

安装 native-image

```
gu install native-image
```

国内网络下载比较慢建议进行离线安装
native-image [离线版下载地址](https://github.com/graalvm/graalvm-ce-builds/releases/tag/vm-22.2.0)

## 离线安装

gu -L install [native-image离线jar包的全路径]

## 查看已安装列表

gu list

## 编译 jar 包

native-image -jar [jar包全路径] [编译后的文件名称]

## 执行编译后的文件

```
./[编译后的文件名称]
```
