#!/bin/bash


assertLastCmdSuccess() {
    if [ "$?" != "0" ]; then
        echo "$1" && exit 2000
    fi
}

clean_up(){
    LAST_EXIT_CODE="$?"

    openstack server delete "$(printenv FREE_COMMIT_EXECUTOR_ID)"
    openstack keypair delete "$(printenv FREE_COMMIT_EXECUTOR_ID)"

    exit $LAST_EXIT_CODE
}

trap 'clean_up' EXIT

echo "Step @preparing-server"

ssh-keygen -f /root/server

eval `ssh-agent`
ssh-add /root/server
openstack keypair create --public-key /root/server.pub "$(printenv FREE_COMMIT_EXECUTOR_ID)"

if [ ! -z "$(printenv VOLUME_ID)" ]; then
    echo "Build node from volume"
    echo "[DEBUG] Running openstack server create --flavor $(printenv FLAVOR_NAME) --network $(printenv NETWORK_NAME) --key-name $(printenv FREE_COMMIT_EXECUTOR_ID) --security-group $(printenv SECURITY_GROUP_NAME) --volume $(printenv VOLUME_ID) $(printenv FREE_COMMIT_EXECUTOR_ID)"
    openstack server create --flavor "$(printenv FLAVOR_NAME)" --network "$(printenv NETWORK_NAME)" --key-name "$(printenv FREE_COMMIT_EXECUTOR_ID)" --security-group "$(printenv SECURITY_GROUP_NAME)" --volume "$(printenv VOLUME_ID)" "$(printenv FREE_COMMIT_EXECUTOR_ID)"
else
    echo "Build node from image $(printenv IMAGE_NAME)"
    echo "[DEBUG] Running openstack server create --image $(printenv IMAGE_NAME) --flavor $(printenv FLAVOR_NAME) --network $(printenv NETWORK_NAME) --key-name $(printenv FREE_COMMIT_EXECUTOR_ID) --security-group $(printenv SECURITY_GROUP_NAME) $(printenv FREE_COMMIT_EXECUTOR_ID)"
    openstack server create --image "$(printenv IMAGE_NAME)" --flavor "$(printenv FLAVOR_NAME)" --network "$(printenv NETWORK_NAME)" --key-name "$(printenv FREE_COMMIT_EXECUTOR_ID)" --security-group "$(printenv SECURITY_GROUP_NAME)" "$(printenv FREE_COMMIT_EXECUTOR_ID)"
fi

echo "Step @waiting-for-IPV4-and-active"

while [ true ]; do
    IS_ACTIVE=$(openstack server show "$(printenv FREE_COMMIT_EXECUTOR_ID)" | grep "ACTIVE")
    IP_TMP=$(openstack server show "$(printenv FREE_COMMIT_EXECUTOR_ID)" | grep "addresses")
    IP=$(echo $IP_TMP | grep -oP "\d+\.\d+\.\d+\.\d+")

    echo $IS_ACTIVE

    if [ ! -z "$IP" ]; then
        echo "[INFO] Server IP is $IP"
    fi

    if [ ! -z "$IS_ACTIVE" ]; then
        break
    fi

    sleep 5
done

echo "$IP" > ip.txt

ssh -q ubuntu@"$IP" exit

while [ "$?" != "0" ]; do
    sleep 3
    ssh -q ubuntu@"$IP" exit
done

if [ -z "$(printenv VOLUME_ID)" ]; then
    echo "Step @docker-installation"

    scp /install.sh ubuntu@"$IP":/home/ubuntu/install.sh
    ssh ubuntu@"$IP" "cd /home/ubuntu && . install.sh"
fi

echo "Step @run-main-task"
scp /launch.sh ubuntu@"$IP":/home/ubuntu/launch.sh
scp /main-entrypoint.sh ubuntu@"$IP":/home/ubuntu/entrypoint.sh
scp /main-Dockerfile ubuntu@"$IP":/home/ubuntu/Dockerfile
ssh ubuntu@"$IP" "cd /home/ubuntu && . launch.sh"

