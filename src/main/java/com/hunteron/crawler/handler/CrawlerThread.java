package com.hunteron.crawler.handler;

import com.hunteron.crawler.configuration.Path;
import com.hunteron.crawler.configuration.RegularParser;
import com.hunteron.crawler.model.Param;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class CrawlerThread implements Runnable {

    private String website;
    private static HashMap<String, HashMap<String, String>> config = RegularParser.getXML(Path.CRAWLER);

    public CrawlerThread(String website) {
        this.website = website;
    }
    @Override
    public void run() {
        //消费者线程池
        if(config.get(website).get("DetailProcess") != null) {
            LinkedBlockingQueue<Param> paramQueue = new LinkedBlockingQueue<>();
            int detailThread = Integer.parseInt(config.get(website).get("DetailThread"));
            ExecutorService ex = Executors.newFixedThreadPool(detailThread);
            DetailProcessTransit detailProcess = new DetailProcessTransit(config.get(website).get("DetailProcess"), paramQueue);
            for (int i = 0; i < detailThread; i++) {
                ex.execute(detailProcess);
            }
            //生产者
            ProcessTemplate process = ProcessTransit.getClass(config.get(website).get("ListProcess"));
            process.handle(paramQueue);
        }else{
            ProcessTemplate producer = ProcessTransit.getClass(config.get(website).get("process"));
            producer.handle();
        }

    }
}
