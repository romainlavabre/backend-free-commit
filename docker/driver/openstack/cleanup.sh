#!/bin/bash

if [ -f "/remote-cleanup.sh" ]; then
    eval `ssh-agent` && ssh-add /root/server
    . /remote-cleanup.sh "$(cat ip.txt)"
fi

openstack server delete $(printenv FREE_COMMIT_EXECUTOR_ID)
openstack keypair delete "$(printenv FREE_COMMIT_EXECUTOR_ID)"

