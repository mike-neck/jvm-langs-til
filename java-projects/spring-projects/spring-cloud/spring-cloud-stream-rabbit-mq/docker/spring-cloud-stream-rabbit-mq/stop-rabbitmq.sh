#!/usr/bin/env bash

set -e

docker ps | grep rabbitmq | awk '{system("docker kill "$1)}'
