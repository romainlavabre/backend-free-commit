#!/bin/bash

sudo apt update && sudo apt install openvpn -y

echo "[DEBUG] Using $(cat /home/ubuntu/client-used)"

echo "sudo openvpn --config /home/ubuntu/client.ovpn" > sub-con-vpn.sh
chmod +x sub-con-vpn.sh
nohup /home/ubuntu/sub-con-vpn.sh >client-vpn.log 2>&1 </dev/null &
sleep 1
echo "Current IP address is $(curl ipinfo.io/ip)"
