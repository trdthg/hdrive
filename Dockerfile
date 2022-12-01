FROM ubuntu:18.04

WORKDIR /root

# install some necessary pkg
RUN apt update -y
RUN apt install sudo zsh curl git vim net-tools openssh-server -y

# config zsh
RUN sh -c "$(curl -L https://github.com/deluan/zsh-in-docker/releases/download/v1.1.3/zsh-in-docker.sh)" -- \
  -t linuxonly \
  -a 'SPACESHIP_PROMPT_ADD_NEWLINE="false"' \
  -a 'SPACESHIP_PROMPT_SEPARATE_LINE="false"' \
  -p git \
  -p https://github.com/zsh-users/zsh-autosuggestions \
  -p https://github.com/zsh-users/zsh-completions

# install java and hadoop
ARG HADOOP_ZIP=hadoop-3.1.3.tar.gz
ARG JDK_ZIP=jdk-8u341-linux-x64.tar.gz

WORKDIR /opt
COPY ./zip/${HADOOP_ZIP} .
COPY ./zip/${JDK_ZIP} .
RUN tar -zxf ${HADOOP_ZIP} && rm ${HADOOP_ZIP} \
  && tar -zxf ${JDK_ZIP} && rm ${JDK_ZIP}

# config path and overrive some config file
WORKDIR /root
ENV JAVA_HOME=/opt/jdk1.8.0_341
ENV HADOOP_HOME=/opt/hadoop-3.1.3
ENV PATH=$PATH:$HOME/bin:$JAVA_HOME/bin:$HADOOP_HOME/bin

# copy to a absolute path to override
COPY ./configs/core-site.xml ${HADOOP_HOME}/etc/hadoop/
COPY ./configs/hdfs-site.xml ${HADOOP_HOME}/etc/hadoop/
COPY ./configs/mapred-site.xml ${HADOOP_HOME}/etc/hadoop/

#
# ENV doesn't work on ssh, so the following echo is necessary
#
# ENV HDFS_NAMENODE_USER=root
# ENV HDFS_DATANODE_USER=root
# ENV HDFS_SECONDARYNAMENODE_USER=root
# ENV YARN_RESOURCEMANAGER_USER=root
# ENV YARN_NODEMANAGER_USER=root
RUN echo "JAVA_HOME=${JAVA_HOME}\nHDFS_NAMENODE_USER=root\nHDFS_DATANODE_USER=root\nHDFS_SECONDARYNAMENODE_USER=root\nYARN_RESOURCEMANAGER_USER=root\nYARN_NODEMANAGER_USER=root" >> ${HADOOP_HOME}/etc/hadoop/hadoop-env.sh

# add hadoop user, but hadoop can run on root
# RUN useradd -rm -d /home/hadoop -s /bin/zsh -g root -G sudo -u 1001 -p "$(openssl passwd -1 hadoop)" hadoop
# USER hadoop
# WORKDIR /home/hadoop

# add ssh support, hadoop need no password ssh to localhost to start dfs and yarn service
RUN echo "PasswordAuthentication yes" >> /etc/ssh/sshd_config
RUN ssh-keygen -P '' -f ~/.ssh/id_rsa
RUN cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
RUN chmod 0600 ~/.ssh/authorized_keys
# RUN ssh-keyscan localhost >> ~/.ssh/known_hosts
# RUN echo "hadoop:hadoop" | chpasswd

# initialize hadoop
RUN hdfs namenode -format

# run some init command
COPY ./cmd ./cmd

ENTRYPOINT service ssh restart \
  && ${HADOOP_HOME}/sbin/start-dfs.sh \
  && cd cmd && ./init_all.sh && cd .. \
  && zsh
