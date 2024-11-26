#!/bin/bash

docker build -t romainlavabre/free-commit-api:"$1" -f ./docker/live/Dockerfile .

docker push romainlavabre/free-commit-api:"$1"

