#!/usr/bin/env bash

set -eu

./gradlew organizeProject && ./gradlew makeSrcDir
