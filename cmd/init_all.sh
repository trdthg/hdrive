#!/bin/sh

if [ -f "./init_hdfs.sh" ]; then
  "./init_hdfs.sh"
fi

if [ -f "./init_hbase.sh" ]; then
  "./init_hbase.sh"
fi

echo "bar"
