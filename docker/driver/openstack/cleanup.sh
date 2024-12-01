#!/bin/bash

openstack server delete $(printenv FREE_COMMIT_EXECUTOR_ID)
openstack keypair delete "$(printenv FREE_COMMIT_EXECUTOR_ID)"

