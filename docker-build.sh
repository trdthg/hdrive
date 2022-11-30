#!/bin/sh
docker build . \
  --network host \
  --build-arg "all_proxy=http://127.0.0.1:7890" \
  --build-arg "http_proxy=http://127.0.0.1:7890" \
  --build-arg "https_proxy=http://127.0.0.1:7890" \
  --build-arg "no_proxy=localhost,127.0.0.1" \
  -t bigdata
