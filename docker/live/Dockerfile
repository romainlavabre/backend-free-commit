ARG JAVA_VERSION="21"
ARG UBUNTU_VERSION="24.04"

FROM ubuntu:${UBUNTU_VERSION} AS compiler
ARG JAVA_VERSION

# Install jdk to build java project
RUN apt update && apt install openjdk-${JAVA_VERSION}-jdk --no-install-recommends -y

ADD . /temp/.

# Build project
RUN cd /temp && ./mvnw clean package -Dmaven.test.skip=true --no-transfer-progress
# Rename .jar with a simple name
RUN cd /temp && cp target/*-SNAPSHOT.jar app.jar

# Final image
FROM ubuntu:${UBUNTU_VERSION}
ARG JAVA_VERSION

RUN apt update \
    # Install required dependencies
    && apt install wget openjdk-${JAVA_VERSION}-jre ca-certificates curl gnupg lsb-release --no-install-recommends -y \
    # Docker installation
    && install -m 0755 -d /etc/apt/keyrings \
    && curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc \
    && chmod a+r /etc/apt/keyrings/docker.asc \
    && echo \
             "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
             $(. /etc/os-release && echo "${UBUNTU_CODENAME:-$VERSION_CODENAME}") stable" | \
             tee /etc/apt/sources.list.d/docker.list > /dev/null \
    && apt update \
    && apt install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin -y \
    # Generate required directories
    && mkdir /ci && mkdir /ci/repository && mkdir /ci/build


COPY --from=compiler /temp/app.jar /app/app.jar

WORKDIR app
ENTRYPOINT ["java","-jar","/app/app.jar","--spring.profiles.active=local"]