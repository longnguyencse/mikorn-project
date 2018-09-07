# README #

This README would normally document whatever steps are necessary to get your application up and running.

### What is this repository for? ###

* Quick summary:
This project contains source code of the integration between apache kafka, storm and cassandra, elasticsearch. The data input came from social network hocavalam.
Detail: receive massage contain gt_data infos from kafka, save gt_data to cassandra and elasticsearch.
* Version:
1.0-SNAPSHOT

### Preconditions
* install zookeeper, kafka, storm.

### How do I get set up? ###
* Deployment instructions:
1. mvn clean install
2. mvn package
3. to run: storm jar storm-kafka-client-1.0-SNAPSHOT-jar-with-dependencies.jar com.mikorn.ifp.storm.KafkaSpoutTopology