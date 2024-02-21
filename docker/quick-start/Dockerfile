FROM ubuntu:latest

RUN mkdir /app

COPY . /app/.

RUN cp /app/docker/quick-start/entrypoint.sh entrypoint.sh
RUN cp /app/docker/quick-start/api/application-local.properties /app/src/main/resources/application-local.properties
RUN mkdir /etc/traefik && cp /app/docker/quick-start/traefik/traefik.yaml /etc/traefik/traefik.yaml
RUN mkdir /ci && mkdir /ci/repository && mkdir /ci/build

RUN apt-get update
RUN apt-get install git -y
RUN mkdir "/client" && cd /client && git clone https://github.com/romainlavabre/frontend-free-commit.git
RUN apt-get install ca-certificates gnupg curl wget nodejs npm -y
RUN npm install -g n && n 16.0.0
RUN curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | apt-key add - \
    && echo "deb https://dl.yarnpkg.com/debian/ stable main" | tee /etc/apt/sources.list.d/yarn.list \
    && apt update \
    && apt install yarn
RUN cd /client/frontend-free-commit && yarn && touch .env && echo "REACT_APP_API_URL=http://localhost:9595" > .env
RUN wget https://github.com/traefik/traefik/releases/download/v2.11.0/traefik_v2.11.0_linux_amd64.tar.gz

RUN apt-get install lsb-release mysql-server wget openjdk-17-jdk git -yq
RUN tar -xf traefik_v2.11.0_linux_amd64.tar.gz -C /usr/local/bin
RUN curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
RUN echo \
         "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \
         focal stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null
RUN apt-get update && apt-get install docker-ce docker-ce-cli containerd.io -yq

USER root
ENTRYPOINT ["./entrypoint.sh"]
