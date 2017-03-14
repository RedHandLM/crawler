package com.hunteron.crawler;

import com.hunteron.crawler.configuration.Path;
import com.hunteron.crawler.configuration.RegularParser;
import com.hunteron.crawler.downloader.HttpProxy;
import com.hunteron.crawler.handler.CrawlerThread;
import com.hunteron.crawler.util.LogLoad;
import org.apache.logging.log4j.LogManager;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Crawler Main
 */
public class Crawler {

    private static HashMap<String,HashMap<String,String>> config = RegularParser.getXML(Path.CRAWLER);
    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(Crawler.class.getName());

    public static void main(String[] args) {

        //加载日志配置
        LogLoad.log4j2();
        proxy();
        //启动爬虫
        for(String site:config.get("website").keySet()) {
            String website = config.get("website").get(site);
            logger.info("启动爬虫，website:" + website);
            new Thread(new CrawlerThread(website)).start();
        }
    }
    private static void proxy(){
        //每隔30分钟，切换一次代理
        Timer timer = new Timer();
        timer.schedule( new TimerTask(){
            public void run() {
                HttpProxy.proxy();
            }
        }, 1000, 900*1000);
    }
}