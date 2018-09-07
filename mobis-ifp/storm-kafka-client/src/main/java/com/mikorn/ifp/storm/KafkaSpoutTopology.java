package com.mikorn.ifp.storm;

import com.mikorn.ifp.storm.bolt.CassandraWriterBolt;
import com.mikorn.ifp.storm.bolt.ElasticSearchBolt;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.*;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;

public class KafkaSpoutTopology
{
// ------------------------------ FIELDS ------------------------------

    private final static String KAFKA_HOST = "localhost:2181";
    private final static String NIMBUS_HOST = "192.168.1.152";
    private final static String KAFKA_TOPIC = "demo";

    private final BrokerHosts brokerHosts;

// --------------------------- CONSTRUCTORS ---------------------------

    private KafkaSpoutTopology(String kafkaZookeeper)
    {
        brokerHosts = new ZkHosts(kafkaZookeeper);
    }

// --------------------------- main() method ---------------------------

    public static void main(String[] args) throws Exception
    {
        KafkaSpoutTopology kafkaSpoutTopology = new KafkaSpoutTopology(KAFKA_HOST);
        Config config = new Config();
//        config.put(Config.TOPOLOGY_TRIDENT_BATCH_EMIT_INTERVAL_MILLIS, 2000);
        config.put(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS, 30);

        StormTopology stormTopology = kafkaSpoutTopology.buildTopology();
        if (args != null && args.length > 0)
        {
            String name = args[0];
            config.put(Config.NIMBUS_HOST, NIMBUS_HOST); //YOUR NIMBUS'S IP
            config.put(Config.NIMBUS_THRIFT_PORT, 6627);    //int is expected here
            config.setNumWorkers(20);
            config.setMaxSpoutPending(5000);
            StormSubmitter.submitTopology(name, config, stormTopology);
        }
        else
        {
            config.setNumWorkers(2);
            config.setMaxTaskParallelism(Runtime.getRuntime().availableProcessors());
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("kafka-storm-cassandra", config, stormTopology);
        }
    }

    private StormTopology buildTopology()
    {
        SpoutConfig kafkaConfig = new SpoutConfig(brokerHosts, KAFKA_TOPIC, "/tmp/kafka-logs", "storm-integration");
        kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
//        kafkaConfig.startOffsetTime = System.currentTimeMillis();
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("gt_data", new KafkaSpout(kafkaConfig), 10);
        builder.setBolt("write-to-cassandra", new CassandraWriterBolt()).shuffleGrouping("gt_data");
        builder.setBolt("write-to-elasticsearch", new ElasticSearchBolt()).shuffleGrouping("gt_data");
        return builder.createTopology();
    }
}
