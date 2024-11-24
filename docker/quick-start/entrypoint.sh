#!/bin/bash

traefik </dev/null &>/dev/null &
cd /client/frontend-free-commit
npm run start </dev/null &>/dev/null &

cd /app

/etc/init.d/mysql start

mysql --execute='CREATE DATABASE `free-commit`;'
mysql --execute="CREATE USER 'admin'@'%' IDENTIFIED WITH mysql_native_password BY 'admin'; GRANT ALL PRIVILEGES ON *.* TO 'admin'@'%' WITH GRANT OPTION; FLUSH PRIVILEGES;"

./mvnw spring-boot:run -Dspring-boot.run.profiles=local

