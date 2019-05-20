#!/bin/bash
basepath=$(cd `dirname $0`; pwd)
cd $basepath
git pull
gradle build -x test
