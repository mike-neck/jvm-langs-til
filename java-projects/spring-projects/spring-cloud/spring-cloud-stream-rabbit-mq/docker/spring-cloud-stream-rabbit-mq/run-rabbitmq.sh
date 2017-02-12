#!/usr/bin/env bash

docker run -d --name rabbit-mq -p 5672:5672 rabbitmq:3.6.6-alpine
