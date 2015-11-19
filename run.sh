#!/bin/bash
PROJECT_PATH=./elemeServer
SRC_PATH=$PROJECT_PATH/src
BIN_PATH=$PROJECT_PATH/bin
JAR_PATH=$PROJECT_PATH

java -Xms2g -Xmx2g -Xss1g -server -classpath $BIN_PATH:$JAR_PATH/gson-2.3.1.jar:$JAR_PATH/netty-all-4.0.33.Final.jar:$JAR_PATH/jedis-2.7.3.jar:$JAR_PATH/mysql-connector-java-5.1.37-bin.jar:$JAR_PATH/commons-pool2-2.4.2.jar server.Server 
