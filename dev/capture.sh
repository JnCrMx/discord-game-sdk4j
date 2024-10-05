#!/bin/bash

mv /run/user/1000/discord-ipc-0 /run/user/1000/discord-ipc-0.original
socat -v UNIX-LISTEN:/run/user/1000/discord-ipc-0,mode=777,reuseaddr,fork UNIX-CONNECT:/run/user/1000/discord-ipc-0.original 2>&1
mv /run/user/1000/discord-ipc-0.original /run/user/1000/discord-ipc-0
