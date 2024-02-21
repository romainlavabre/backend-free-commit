#!/bin/bash

docker build -t romainlavabre/free-commit-api:"$1" -t romainlavabre/free-commit-api:standalone-latest -f ./docker/live/Dockerfile-standalone .

docker push romainlavabre/free-commit-api:standalone-"$1"
docker push romainlavabre/free-commit-api:standalone-latest
