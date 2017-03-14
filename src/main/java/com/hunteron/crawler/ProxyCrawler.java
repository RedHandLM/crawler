package com.hunteron.crawler;

import com.hunteron.crawler.configuration.Path;
import com.hunteron.crawler.configuration.RegularParser;
import com.hunteron.crawler.handler.CrawlerThread;
import com.hunteron.crawler.util.LogLoad;
import org.apache.logging.log4j.LogManager;

import java.util.HashMap;

public class ProxyCrawler {

    private static HashMap<String,HashMap<String,String>> config = RegularParser.getXML(Path.CRAWLER);
    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(Crawler.class.getName());

    public static void main(String[] args) {
        //加载日志配置
        LogLoad.log4j2();
        //启动爬虫
        for(String site:config.get("proxy").keySet()) {
            String website = config.get("proxy").get(site);
            logger.info("正则启动爬虫，proxy:" + website);
            new Thread(new CrawlerThread(website)).start();
        }
    }
}
