#!/bin/bash

echo "@preparing-server"

if [ ! -z "$VOLUME_ID" ]; then
    openstack server create --flavor "$FLAVOR_NAME" --network "$NETWORK_NAME" --key-name "$KEY_NAME" --security-group "$SECURITY_GROUP_NAME" --volume "$VOLUME_ID" "$(printenv FREE_COMMIT_EXECUTOR_ID)"
else
    openstack server create --flavor "$FLAVOR_NAME" --network "$NETWORK_NAME" --key-name "$KEY_NAME" --security-group "$SECURITY_GROUP_NAME" "$(printenv FREE_COMMIT_EXECUTOR_ID)"
fi

echo "@main"

echo "@deleting-server"
openstack server delete $(printenv FREE_COMMIT_EXECUTOR_ID)

