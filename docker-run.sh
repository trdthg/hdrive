#!/bin/sh

# 9870 : hadoop web interface
# 9000 : hadoop api interface

# 16000: hmaster port
# 16010: hbase web interface
# 16020 | 16030: region server port

# 2181 : hbase api interface
docker run -it \
  -h 55e19a41e9ef \
  -p 9000:9000 \
  -p 9870:9870 \
  -p 2181:2181 \
  -p 16000:16000 \
  -p 16010:16010 \
  -p 16020:16020 \
  -p 16030:16030 \
  bigdata
