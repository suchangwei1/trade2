#!/bin/bash

basePath=$(pwd)/..
host="47.88.173.58"
project="backend"
tomcat="/mnt/tomcat_backend"

scp $basePath/$project/build/libs/ROOT.war root@$host:$tomcat/webapps/ROOT.war
ssh root@$host "kill \$(ps -ef | grep $tomcat | grep -v grep | awk '{print \$2}')"
#ssh root@$host "$tomcat/bin/shutdown.sh"
sleep 3s
ssh root@$host "rm -r /mnt/tomcat_backend/webapps/ROOT"
ssh root@$host "$tomcat/bin/startup.sh"