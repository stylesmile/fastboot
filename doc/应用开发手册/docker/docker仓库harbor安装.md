# harbor 安装
## 下载 
https://github.com/goharbor/harbor/releases
  解压
  目录结构如下
```
├── common
         └── config
             ├── core
                      ├── app.conf
                      ├── certificates
                      └── env
             ├── db
                      └── env
             ├── jobservice
                      ├── config.yml
                      └── env
             ├── log
                      ├── logrotate.conf
                      └── rsyslog_docker.conf
             ├── nginx
                      ├── conf.d
                      └── nginx.conf
             ├── portal
                      └── nginx.conf
             ├── registry
                      ├── config.yml
                      ├── passwd
                      └── root.crt
             ├── registryctl
                      ├── config.yml
                      └── env
             └── shared
                 └── trust-certificates
├── common.sh
├── docker-compose.yml
├── harbor.v2.5.3.tar.gz
├── harbor.yml
├── harbor.yml.tmpl
├── install.sh
├── LICENSE
└── prepare
```
## 编辑harbor.yml文件（不存在，直接复制harbor.yml.tmpl）




  harbor 中已经含有了 docker registry ，因此，无需再安装 docker registry 。
  cp harbor.yml.tmpl harbor.yml
  vim harbor.yml
## 配置
  修改 harbor.yml 配置文件

  端口 port（其实，不改也行，它就会占用 80 端口）
  修改 hostname，改为 harbor 所在机器的 ip 。
  https 部分注释掉。
  记住你的 hostname:port 未来很多地方都要用到它们的组合。比如它是 192.172.0.16:9527

下载安装 docker-compose
执行 install.sh，运行 docker-compose up
为 docker server 添加新配置
# cat /etc/docker/daemon.json
{
...
"insecure-registries": ["192.172.0.16:9527", "0.0.0.0"]
}
重启 docker server 。

浏览器访问 http://192.172.0.16:9527 。用户名 admin，密码 Harbor12345

创建项目（例如，test）未来会用到

为镜像打标签
docker tag [OPTIONS] IMAGE[:TAG] [REGISTRYHOST/][USERNAME/]NAME[:TAG]
docker tag xyz:v1.0 192.172.0.16:9527/test/xyz:v1.0
这里出现了 hostname:port ，也出现了你创建的项目。

docker Cli 中登录 harbor
推送前需要先登录。

docker login 192.172.0.16:9527
输入用户名，密码

docker push

docker push 192.172.0.16:9527/test/xyz:v1.0
