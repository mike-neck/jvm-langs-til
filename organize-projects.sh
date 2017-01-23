#!/usr/bin/env bash

set -e

echo "rootProject.name = '${PWD##*/}'" > settings.gradle

find . -type d -maxdepth 4 | \
  grep -e "\./[a-z]\+\-projects" | \
  grep -v "/src/\?" | \
  grep -v "/build/\?" | \
  sed \
    -e "s/^.\{2\}/include \'\:/g" \
    -e "s/\//\:/g" \
    -e "s/\([a-z0-9A-Z]\)$/\1\'/g" >> settings.gradle

cat settings.gradle | \
  grep -v 'rootProject.name' | \
  sed \
    -e "s/'//g" \
    -e "s/include/project/g"
