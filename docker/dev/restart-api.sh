#!/bin/bash

docker container restart api && sleep 2 && docker container logs api -f