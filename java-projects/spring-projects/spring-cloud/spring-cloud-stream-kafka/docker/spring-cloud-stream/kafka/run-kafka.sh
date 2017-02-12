#!/usr/bin/env bash

set -eu

docker run -d --name kafka -p 2181:2181 -p 9092:9092 mikeneck/kafka
