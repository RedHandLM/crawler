package com.hunteron.crawler.dao.kafka;

import com.hunteron.crawler.util.SingleMatch;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Kafka 线程实现
 */
public class KafkaConsumerThread  implements Runnable {

    private KafkaStream<byte[], byte[]> stream;
    private static Logger logger = LogManager.getLogger(KafkaConsumerThread.class.getName());

    KafkaConsumerThread(KafkaStream<byte[], byte[]> stream) {
        this.stream = stream;
    }

    @Override
    public void run() {
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        while (it.hasNext()) {
            long startTime = System.currentTimeMillis();
            MessageAndMetadata<byte[], byte[]> mam = it.next();
            /*RuleTransit rule = new RuleTransit();
            String msg = new String(mam.message()).replace("\\\"", "\"");
            if(StringUtils.isNotBlank(msg) && getProduceTime(msg) > 1476692580000L) {
                rule.transit(msg);
            }*/
            long endTime = System.currentTimeMillis();
            logger.info((endTime - startTime) + "ms");
        }
    }

    private long getProduceTime(String msg){
        Long produceTime = 0L;
        try {
            produceTime = Long.parseLong(SingleMatch.getString(msg, "produceTime"));
        }catch (Exception e){
            logger.error("produceTime类型转换出错:" + msg);
        }
        return produceTime;
    }
}