#!/bin/bash

basePath=$(pwd)/..
host="47.88.173.58"
project="newfront"

scp $basePath/$project/build/distributions/$project.tar root@$host:/mnt
ssh root@$host "kill \$(ps -ef | grep /mnt/$project | grep -v grep | awk '{print \$2}')"
ssh root@$host "rm -r /mnt/$project"
ssh root@$host "tar xvf /mnt/$project.tar -C /mnt"
ssh root@$host "nohup /mnt/$project/bin/$project > /mnt/logs/$project.log 2>&1 &"