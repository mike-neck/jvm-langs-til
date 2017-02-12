#!/usr/bin/env bash

set -e

docker ps -a | grep rabbit-mq | awk '{system("docker rm "$1)}'
docker run -d --name rabbit-mq -p 5672:5672 -p 15672:15672 rabbitmq:3.6.6-management-alpine
