# kubernetes 安装
```aidl
# 关闭防火墙
systemctl stop firewalld
systemctl disable firewalld

# 查看hostname并修改

hostname   # 查看本机hostname
hostnamectl set-hostname k8s-master # 把本机名设置成k8s-master
hostnamectl status  # 查看修改结果
echo "127.0.0.1 $(hostname)" >> /etc/hosts  # 修改hosts文件

# 关闭selinux(linux的安全机制)
sed -i 's/enforcing/disabled/' /etc/selinux/config
setenforce 0

# 关闭swap(关闭内存交换)
swapoff -a
sed -ri 's/.*swap.*/#&/' '/etc/fstab'
free -m  # 检查，确保swap里面没有东西

# 配置桥接流量
cat <<EOF | sudo tee /etc/modules-load.d/k8s.conf
br_netfilter
EOF

cat <<EOF | sudo tee /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF

# 生效
sudo sysctl --system
```

## 安装
```
# 配置k8s的yum源
cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=0
gpgcheck=0
repo_gpgcheck=0
gpg_key=http://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg http://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF


#  卸载旧版本
yum remove -y kubelet kubeadm kubectl

# 查看可以安装的版本
yum list kubelet --showduplicates | sort -r   

# 安装kubernetes
yum install -y kubelet kubeadm kubectl

# 设置开机启动kubelet
systemctl enable kubelet

# 启动kubelet
systemctl start kubelet

# 查看kubelet状态
systemctl status kubelet  # kubelet进入无限死循环状态
```