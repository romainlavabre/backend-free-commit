#!/bin/bash

if [[ "$EUID" -ne 0 ]]; then
	echo "This installer needs to be run with superuser privileges."
	exit
fi

# Store the absolute path of the directory where the script is located
script_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

for ((i = 0 ; i < $1 ; i++)); do
    if [ -f "client-$i.ovpn" ]; then
        echo "client-$i already exists, skipping..."
        continue
    fi

	unsanitized_client="client-$i"
    client=$(sed 's/[^0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_-]/_/g' <<< "$unsanitized_client")
    while [[ -z "$client" || -e /etc/openvpn/server/easy-rsa/pki/issued/"$client".crt ]]; do
        echo "$client: invalid name."
        read -p "Name: " unsanitized_client
        client=$(sed 's/[^0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_-]/_/g' <<< "$unsanitized_client")
    done
    cd /etc/openvpn/server/easy-rsa/
    ./easyrsa --batch --days=3650 build-client-full "$client" nopass
    # Build the $client.ovpn file, stripping comments from easy-rsa in the process
    grep -vh '^#' /etc/openvpn/server/client-common.txt /etc/openvpn/server/easy-rsa/pki/inline/private/"$client".inline > "$script_dir"/"$client".ovpn
    echo
    echo "$client added. Configuration available in:" "$script_dir"/"$client.ovpn"
done