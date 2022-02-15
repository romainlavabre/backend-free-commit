FROM openjdk:11.0.14 as compiler
WORKDIR temp
COPY . /temp/.
RUN ./mvnw clean package -Dmaven.test.skip=true --no-transfer-progress
RUN cp target/*-SNAPSHOT.jar app.jar

FROM openjdk:11-jre
COPY --from=compiler /temp/app.jar /app/app.jar

RUN apt-get update \
    && apt-get install ca-certificates \
                           curl \
                           gnupg \
                           lsb-release -y \
    && curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg \
    && echo \
         "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \
         focal stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null \
    && apt-get update && apt-get install docker-ce docker-ce-cli containerd.io -y \
    && mkdir /ci \
    && mkdir /ci/repository \
    && mkdir /ci/build

WORKDIR app
ENTRYPOINT ["java","-jar","/app/app.jar","--spring.profiles.active=local"]
