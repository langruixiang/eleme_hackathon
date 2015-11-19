#!/bin/bash
ps aux | grep stress.py | head -n 1 | awk '{print $2}' | xargs kill -9
jps |grep Server |cut -f1 -d' ' |xargs kill -9
redis-cli flushall
./compile.sh 
./run.sh &
