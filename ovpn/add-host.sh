#!/bin/bash

if ! grep -q "route-nopull" "/etc/openvpn/server/client-common.txt"; then
  echo "route-nopull" >> /etc/openvpn/server/client-common.txt
fi

echo "route $1 255.255.255.255" >> /etc/openvpn/server/client-common.txt