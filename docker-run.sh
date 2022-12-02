#!/bin/sh
docker run -it \
  -p 9870:9870 \
  -p 9000:9000 \
  -p 16010:16010 \
  bigdata
