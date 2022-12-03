# HNetDisk

## Quick Start

- Clone this Reposity
  `git clone git@github.com:trdthg/hdrive.git`
- Install docker
- prepare [jdk8-341](https://www.oracle.com/cn/java/technologies/javase/javase8u211-later-archive-downloads.html), [hadoop-3.1.3](https://hadoop.apache.org/release/), [hbase-2.3.0](https://archive.apache.org/dist/hbase/)
  - download xxx.tar.gz and put them to the _zip_ folder
  - replace ARG in the Dockerfile to your own
  > maybe other versions will work, but there are no guarantee here.
- run `./docker-build.sh`
- run `./docker-run.sh`

## cmd

- check network: `ifconfig` (provided by net-tools)
- check ports open: `netstat -tunlp`
- check hadoop and hbasp: `jps`