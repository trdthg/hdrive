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
WORKDIR /opt

ARG HADOOP_ZIP=hadoop-3.1.3.tar.gz
ARG JDK_ZIP=jdk-8u341-linux-x64.tar.gz
ARG HBASE_ZIP=hbase-2.3.2-bin.tar.gz
ARG JDK_FOLDER=jdk1.8.0_341
ARG HADOOP_FOLDER=hadoop-3.1.3
ARG HBASE_FOLDER=hbase-2.3.2

COPY ./zip/${HADOOP_ZIP} .
COPY ./zip/${JDK_ZIP} .
COPY ./zip/${HBASE_ZIP} .
RUN tar -zxf ${HADOOP_ZIP} && rm ${HADOOP_ZIP} \
  && tar -zxf ${JDK_ZIP} && rm ${JDK_ZIP} \
  && tar -zxf ${HBASE_ZIP} && rm ${HBASE_ZIP}

# config path and overrive some config file
WORKDIR /root
ENV JAVA_HOME=/opt/${JDK_FOLDER}
ENV HADOOP_HOME=/opt/${HADOOP_FOLDER}
ENV HBASE_HOME=/opt/${HBASE_FOLDER}
ENV PATH=$PATH:$HOME/bin:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HBASE_HOME/bin

# copy to a absolute path to override
COPY ./configs/hadoop/etc/hadoop/core-site.xml ${HADOOP_HOME}/etc/hadoop/
COPY ./configs/hadoop/etc/hadoop/hdfs-site.xml ${HADOOP_HOME}/etc/hadoop/
COPY ./configs/hadoop/etc/hadoop/mapred-site.xml ${HADOOP_HOME}/etc/hadoop/
COPY ./configs/hbase/conf/hbase-site.xml ${HBASE_HOME}/conf/

#
# only ENV doesn't work on ssh, so the following echo is necessary
#
RUN echo "\
  JAVA_HOME=${JAVA_HOME}\n\
  HDFS_NAMENODE_USER=root\n\
  HDFS_DATANODE_USER=root\n\
  HDFS_SECONDARYNAMENODE_USER=root\n\
  YARN_RESOURCEMANAGER_USER=root\n\
  YARN_NODEMANAGER_USER=root\n" >> ${HADOOP_HOME}/etc/hadoop/hadoop-env.sh
RUN echo "\
  export JAVA_HOME=${JAVA_HOME}\n\
  export HBASE_CLASSPATH=${HADOOP_HOME}/etc/hadoop\n\
  export HBASE_MANAGES_ZK=true\n\
  export HBASE_DISABLE_HADOOP_CLASSPATH_LOOKUP=true\n" >> ${HBASE_HOME}/conf/hbase-env.sh

# add hadoop user, but hadoop can run on root
# RUN useradd -rm -d /home/hadoop -s /bin/zsh -g root -G sudo -u 1001 -p "$(openssl passwd -1 hadoop)" hadoop
# USER hadoop
# WORKDIR /home/hadoop

# initialize hadoop
RUN hdfs namenode -format

# run some init command
COPY ./cmd ./cmd

ENTRYPOINT ./cmd/init_once.sh \
  && service ssh restart \
  && ${HADOOP_HOME}/sbin/start-dfs.sh \
  && ${HBASE_HOME}/bin/start-hbase.sh \
  && cd cmd && ./init_all.sh && cd .. \
  && jps \
  && zsh
