package com.hunteron.crawler.dao.kafka;

import com.hunteron.crawler.configuration.Path;
import com.hunteron.crawler.configuration.RegularParser;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Kafka 消费者
 */
public class KafkaConsumer implements Runnable {

    private static HashMap<String,HashMap<String, String>> config = RegularParser.getXML(Path.KAFKA);
    private static ConsumerConnector consumer;
    private String topic;

    public KafkaConsumer(String topic) {
        Properties props = new Properties();
        props.putAll(config.get("kafka-consumer"));
        ConsumerConfig consumerConfig = new ConsumerConfig(props);
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(consumerConfig);
        this.topic = topic;
    }
    @Override
    public void run() {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(config.get("kafka").get("thread-num")));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        ExecutorService executorService = Executors.newFixedThreadPool(new Integer(config.get("kafka").get("thread-num")));
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
        for (final KafkaStream stream : streams) {
            executorService.execute(new KafkaConsumerThread(stream));
        }
    }
}