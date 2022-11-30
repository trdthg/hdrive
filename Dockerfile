FROM ubuntu:18.04

WORKDIR /root

# install some nessary pkg
RUN apt update -y
RUN apt install sudo zsh curl git vim net-tools openssh-server -y

# # config zsh
RUN sh -c "$(curl -L https://github.com/deluan/zsh-in-docker/releases/download/v1.1.3/zsh-in-docker.sh)" -- \
  -t linuxonly \
  -a 'SPACESHIP_PROMPT_ADD_NEWLINE="false"' \
  -a 'SPACESHIP_PROMPT_SEPARATE_LINE="false"' \
  -p git \
  -p https://github.com/zsh-users/zsh-autosuggestions \
  -p https://github.com/zsh-users/zsh-completions

# install java and hadoop
WORKDIR /opt
COPY ./zip/hadoop-3.1.3.tar.gz .
COPY ./zip/jdk-8u341-linux-x64.tar.gz .

ENV JAVA_HOME=/opt/jdk1.8.0_341
ENV HADOOP_HOME=/opt/hadoop-3.1.3
ENV PATH=$PATH:$HOME/bin:$JAVA_HOME/bin:$HADOOP_HOME/bin
RUN tar -zxf hadoop-3.1.3.tar.gz && rm hadoop-3.1.3.tar.gz \
  && tar -zxf jdk-8u341-linux-x64.tar.gz && rm jdk-8u341-linux-x64.tar.gz
WORKDIR /root

# copy to a absolute path to override
COPY ./configs/core-site.xml ${HADOOP_HOME}/etc/hadoop/
COPY ./configs/hdfs-site.xml ${HADOOP_HOME}/etc/hadoop/
COPY ./configs/mapred-site.xml ${HADOOP_HOME}/etc/hadoop/

#
# ENV doesn't on ssh, so the following echo is nessary
#
# ENV HDFS_NAMENODE_USER=root
# ENV HDFS_DATANODE_USER=root
# ENV HDFS_SECONDARYNAMENODE_USER=root
# ENV YARN_RESOURCEMANAGER_USER=root
# ENV YARN_NODEMANAGER_USER=root
RUN echo "JAVA_HOME=/opt/jdk1.8.0_341\nHDFS_NAMENODE_USER=root\nHDFS_DATANODE_USER=root\nHDFS_SECONDARYNAMENODE_USER=root\nYARN_RESOURCEMANAGER_USER=root\nYARN_NODEMANAGER_USER=root" >> ${HADOOP_HOME}/etc/hadoop/hadoop-env.sh

# add hadoop user, but hadoop can run on root
# RUN useradd -rm -d /home/hadoop -s /bin/zsh -g root -G sudo -u 1001 -p "$(openssl passwd -1 hadoop)" hadoop
# USER hadoop
# WORKDIR /home/hadoop

RUN echo "PasswordAuthentication yes" >> /etc/ssh/sshd_config
RUN ssh-keygen -P '' -f ~/.ssh/id_rsa
RUN cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
RUN chmod 0600 ~/.ssh/authorized_keys
# RUN ssh-keyscan localhost >> ~/.ssh/known_hosts
# RUN echo "hadoop:hadoop" | chpasswd

ENTRYPOINT service ssh restart \
  && hdfs namenode -format \
  && /opt/hadoop-3.1.3/sbin/start-dfs.sh \
  && zsh
