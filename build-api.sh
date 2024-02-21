#!/bin/bash

docker build -t romainlavabre/free-commit-api:"$1" -t romainlavabre/free-commit-api:latest -f ./docker/live/Dockerfile .

docker push romainlavabre/free-commit-api:"$1"
docker push romainlavabre/free-commit-api:latest

git tag -a "$1" -m "$1" && git push --tags
