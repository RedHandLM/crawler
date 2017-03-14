package com.hunteron.crawler.dao.kafka;


import com.alibaba.fastjson.JSONObject;
import com.hunteron.crawler.configuration.Path;
import com.hunteron.crawler.configuration.RegularParser;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.*;

/**
 * Kafka 生产者
 */
public class KafkaProducer{

    private static HashMap<String,HashMap<String, String>> config = RegularParser.getXML(Path.KAFKA);
    private static ProducerConfig producerConfig = null;
    private static String topic = config.get("topic-producer").get("topic");

    public KafkaProducer() {
        Properties props = new Properties();
        props.putAll(config.get("kafka-producer"));
        producerConfig = new ProducerConfig(props);
    }

    public void sendMsg(TreeMap<String,Object> map) {

        map.put("topic",topic);
        Object msgObj = JSONObject.toJSON(map);
        Producer<String, String> producer = new Producer<>(producerConfig);
        List<KeyedMessage<String, String>> messageList = new ArrayList<>();
        messageList.add(new KeyedMessage<String, String>(topic, msgFormat(msgObj.toString())));
        producer.send(messageList);
    }

    private String msgFormat(String msg){
        msg = "{\"consumeUseTime\":-1,\"consumerPid\":0,\"consumerTid\":0,\"producePid\":0,\"produceTid\":0,\"produceTime\":0,\"receiveUseTime\":-1,\"topic\":\"store.level.up\",\"value\":\"" +
                msg.replace("\"", "\\\"") +
                "\"}";
        return msg;
    }

    public static void main(String[] args) {

        for(int i=0;i<10;i++) {
            TreeMap<String, Object> map = new TreeMap<>();
            map.put("storeId", "");
            map.put("merchantId", "");
            map.put("fromLv", "");
            map.put("fromLvName", "");
            map.put("toLv", "");
            map.put("toLvName", "");
            KafkaProducer kafkaProducer = new KafkaProducer();
            kafkaProducer.sendMsg(map);
        }
    }
}