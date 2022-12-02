#!/bin/sh
CONTAINER_FIRST_STARTUP="CONTAINER_FIRST_STARTUP"
if [ ! -e /$CONTAINER_FIRST_STARTUP ]; then
  service ssh restart
  # echo "PasswordAuthentication yes" >>/etc/ssh/sshd_config
  ssh-keygen -P '' -f ~/.ssh/id_rsa
  cat ~/.ssh/id_rsa.pub >>~/.ssh/authorized_keys
  chmod 0600 ~/.ssh/authorized_keys
  touch ~/.ssh/known_hosts
  ssh-keyscan -H 127.0.0.1 >>~/.ssh/known_hosts
  ssh-keyscan -H localhost >>~/.ssh/known_hosts

  touch /$CONTAINER_FIRST_STARTUP
fi
