FROM ubuntu:latest

RUN apt update && apt install python3 python3-dev python3-pip openssh-client curl wget git -y
RUN pip3 install python-openstackclient --break-system-packages

RUN mkdir /root/.ssh && echo "Host *\n\tStrictHostKeyChecking no" > /root/.ssh/config
ADD entrypoint.sh /entrypoint.sh
ADD cleanup.sh /cleanup.sh
ADD install.sh /install.sh
RUN chmod +x entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]