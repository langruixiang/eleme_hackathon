#!/bin/bash
while [ 1 = 1 ]
do
    py.test tests/ 
    redis-cli flushall
done
