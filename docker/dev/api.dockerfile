FROM openjdk:11.0.14
ARG USER_HOST
ARG UID_HOST
RUN apt-get update \
    && apt-get install wkhtmltopdf -y \
    && mkdir /var/tmp/pdf-builder
RUN groupadd -r "${USER_HOST}" \
    && useradd -r -md "/home/${USER_HOST}" -g "${USER_HOST}" "${USER_HOST}" \
    && usermod -u ${UID_HOST} ${USER_HOST} \
    && groupmod -g ${UID_HOST} ${USER_HOST} \
    && chown ${USER_HOST} /var/tmp/pdf-builder \
    && chgrp ${USER_HOST} /var/tmp/pdf-builder
USER "${USER_HOST}"
WORKDIR app
ENTRYPOINT ["./mvnw","spring-boot:run","-Dspring-boot.run.profiles=local"]