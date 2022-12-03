#!/bin/sh
docker build . \
  --network host \
  --build-arg "all_proxy=http://127.0.0.1:7890" \
  --build-arg "http_proxy=http://127.0.0.1:7890" \
  --build-arg "https_proxy=http://127.0.0.1:7890" \
  --build-arg "no_proxy=localhost,127.0.0.1" \
  --build-arg "JDK_ZIP=jdk-8u341-linux-x64.tar.gz" \
  --build-arg "JDK_FOLDER=jdk1.8.0_341" \
  --build-arg "HADOOP_ZIP=hadoop-3.1.3.tar.gz" \
  --build-arg "HADOOP_FOLDER=hadoop-3.1.3" \
  --build-arg "HBASE_ZIP=hbase-2.3.2-bin.tar.gz" \
  --build-arg "HBASE_FOLDER=hbase-2.3.2" \
  -t bigdata
