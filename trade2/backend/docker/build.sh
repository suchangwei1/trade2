#!/bin/bash
basepath=$(cd `dirname $0`; pwd)
cp $basepath/../build/libs/backend.war $basepath/ROOT.war
tag="$1"
if [ "$tag" = "" ]; then
 tag="latest"
fi
host=""
image="backend:$tag"
sudo docker build -t $host$image $basepath
