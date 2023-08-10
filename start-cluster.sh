#!/bin/sh

export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64/
export KAFKA_DIST=/home/dthibau/Formations/SpringKafka/MyWork/kafka-dist/kafka_2.13-3.2.1
export KAFKA_CLUSTER=/home/dthibau/Formations/SpringKafka/github/solutions/kafka-cluster
export KAFKA_LOGS=/home/dthibau/Formations/SpringKafka/MyWork/kafka-logs


$KAFKA_DIST/bin/kafka-server-start.sh -daemon $KAFKA_CLUSTER/broker-1/server.properties 
$KAFKA_DIST/bin/kafka-server-start.sh -daemon $KAFKA_CLUSTER/broker-2/server.properties 
$KAFKA_DIST/bin/kafka-server-start.sh -daemon $KAFKA_CLUSTER/broker-3/server.properties 
