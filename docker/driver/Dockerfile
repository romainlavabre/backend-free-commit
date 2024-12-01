FROM ubuntu:latest

RUN apt update && apt install python3 python3-dev python3-pip -y
RUN pip3 install python-openstackclient --break-system-packages


ENTRYPOINT ["./entrypoint.sh"]