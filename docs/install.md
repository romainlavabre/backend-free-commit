[BACK](../README.md)
___
# Installation

For installation, you need to four elements :
* Free Commit api (Required)
* A relational database (Required)
* A HTTP server / reverse poxy (Optional but recommended)
* Free Commit client (Optional)

To simplify, we offer 2 docker images (for the api and the client). <br>

### API

You can see [romainlavabre/free-commit-api](https://hub.docker.com/r/romainlavabre/free-commit-api)

### Client

You can see [romainlavabre/free-commit-client](https://hub.docker.com/r/romainlavabre/free-commit-client)

### Minimum resource requirement

* 2 CPU core
* 2 GO RAM

### Standalone image
There is no standalone docker image for full stack.
But our stack (with docker-compose)

> docker-compose.yaml
```yaml
version: '3.2'

services:
    service-reverse-proxy:
        image: library/traefik:latest
        ports:
            - 80:80
            - 443:443
        networks:
            - service-reverse-proxy
            - free-commit-api
            - free-commit-client
        volumes:
            - ${PWD}/traefik:/etc/traefik/
            - /var/docker-volume/service-reverse-proxy:/tlsKey
            - /var/run/docker.sock:/var/run/docker.sock
        restart: always
        container_name: service-reverse-proxy

    free-commit-api:
        image: romainlavabre/free-commit-api:latest
        environment:
            DATASOURCE_URL: ${DATASOURCE_URL}
            DATASOURCE_USERNAME: ${DATASOURCE_USERNAME}
            DATASOURCE_PASSWORD: ${DATASOURCE_PASSWORD}
            DATASOURCE_DIALECT: ${DATASOURCE_DIALECT}
            ENCRYPTION_KEY: ${ENCRYPTION_KEY}
            JWT_SECRET: ${JWT_SECRET}
            JWT_LIFE_TIME: ${JWT_LIFE_TIME}
            DEFAULT_ADMIN_USERNAME: ${DEFAULT_ADMIN_USERNAME}
            DEFAULT_ADMIN_PASSWORD: ${DEFAULT_ADMIN_PASSWORD}
            MAIL_HOST: ${MAIL_HOST}
            MAIL_PORT: ${MAIL_PORT}
            MAIL_FROM: ${MAIL_FROM}
            MAIL_PASSWORD: ${MAIL_PASSWORD}
            TWILIO_SID: ${TWILIO_SID}
            TWILIO_AUTH_TOKEN: ${TWILIO_AUTH_TOKEN}
            TWILIO_FROM: ${TWILIO_FROM}
        labels:
            - "traefik.enable=true"
            - "traefik.http.routers.free-commit-api.rule=Host(`free-commit.{domain}.{ext}`) && PathPrefix(`/api`)"
            - "traefik.docker.network=free-commit-api"
            - "traefik.http.middlewares.free-commit-api-replace-prefix.stripprefix.prefixes=/api"
            - "traefik.http.routers.free-commit-api.middlewares=free-commit-api-replace-prefix@docker"
            - "traefik.http.routers.free-commit-api.entrypoints=websecure"
            - "traefik.http.routers.free-commit-api.tls=true"
            - "traefik.http.services.free-commit-api.loadbalancer.server.port=8080"
        networks:
            - free-commit-api
            - database
        volumes:
            - /var/run/docker.sock:/var/run/docker.sock
        restart: always
        container_name: free-commit-api

    free-commit-client:
        image: romainlavabre/free-commit-client:latest
        environment:
            REACT_APP_API_URL: ${FREE_COMMIT_API_URL}
        networks:
            - free-commit-client
        labels:
            - "traefik.enable=true"
            - "traefik.http.routers.free-commit-client.rule=Host(`free-commit.{domain}.{ext}`)"
            - "traefik.docker.network=free-commit-client"
            - "traefik.http.routers.free-commit-client.entrypoints=websecure"
            - "traefik.http.routers.free-commit-client.tls=true"
            - "traefik.http.services.free-commit-client.loadbalancer.server.port=80"
        restart: always
        container_name: free-commit-client

    database:
        image: mysql/mysql-server:latest
        environment:
            MYSQL_ROOT_HOST: '%'
            MYSQL_ROOT_PASSWORD: ${DATASOURCE_PASSWORD}
            MYSQL_DATABASE: free-commit
        networks:
            - database
        volumes:
            - /var/docker-volume/database:/var/lib/mysql
        restart: on-failure
        container_name: database

networks:
    service-reverse-proxy:
        name: service-reverse-proxy
    free-commit-api:
        name: free-commit-api
    free-commit-client:
        name: free-commit-client
    database:
        name: database
```

> .env

```env
DATASOURCE_URL=jdbc:mysql://database:3306/free-commit
DATASOURCE_USERNAME=root
DATASOURCE_PASSWORD=?
DATASOURCE_DIALECT=org.hibernate.dialect.MySQL5InnoDBDialect
ENCRYPTION_KEY=?
JWT_SECRET=?
JWT_LIFE_TIME=7200
DEFAULT_ADMIN_USERNAME=?
DEFAULT_ADMIN_PASSWORD=?
MAIL_HOST=?
MAIL_PORT=?
MAIL_FROM=?
MAIL_PASSWORD=?
TWILIO_SID=?
TWILIO_AUTH_TOKEN=?
TWILIO_FROM=?
FREE_COMMIT_API_URL=https://free-commit.{host}.{ext}/api
```

```shell script
mkdir traefik
```

> traefik/traefik.yaml
```yaml
# Docker configuration backend
providers:
    docker:
        defaultRule: "Host(`{{ trimPrefix `/` .Name }}.docker.localhost`)"
        exposedByDefault: false

# API and dashboard configuration
api:
    insecure: false

entryPoints:
    web:
        address: ":80"
        http:
            redirections:
                entryPoint:
                    to: websecure
                    scheme: https
    websecure:
        address: ":443"

log:
    level: ERROR
```

### Protect your disk

By default, Free Commit will clean your file system, but additionally you can add a cron job
  
```shell script
sudo -i && crontab -e
>> 0 */6 * * * docker system prune -fa
``` 

___
[BACK](../README.md)
