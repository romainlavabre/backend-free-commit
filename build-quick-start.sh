#!/bin/bash

docker build -t romainlavabre/free-commit-api:quick-start-"$1" -t romainlavabre/free-commit-api:quick-start-latest -f ./docker/quick-start/Dockerfile .

docker push romainlavabre/free-commit-api:quick-start-"$1"
docker push romainlavabre/free-commit-api:quick-start-latest