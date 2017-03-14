package com.hunteron.crawler.handler.youdaili;

import com.hunteron.crawler.configuration.Path;
import com.hunteron.crawler.configuration.RegularParser;
import com.hunteron.crawler.downloader.UrlConnectionUtil;
import com.hunteron.crawler.handler.ProcessTemplate;
import com.hunteron.crawler.util.MultiMatch;
import com.hunteron.crawler.util.ThreadSleep;
import org.apache.logging.log4j.LogManager;

import java.util.HashMap;
import java.util.LinkedList;

public class YouProcess extends ProcessTemplate {

    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(YouProcess.class.getName());
    private static HashMap<String,HashMap<String,String>> config = RegularParser.getXML(Path.YOU);

    @Override
    public void handle() {
        while (true) {
            String listCode = getHtmlCode("http://www.youdaili.net/Daili/http/");
            if (listCode == null){
                continue;
            }
            LinkedList<String> listUrl ;
            listUrl = MultiMatch.match(listCode,config.get("producer").get("url"));
            for (String urlList:listUrl){
                if(cache.getSet(urlList,"") == null){
                    String code = getHtmlCode(urlList);
                    if (code == null){
                        cache.del(urlList);
                        continue;
                    }
                    LinkedList<String> ipPortList = new LinkedList<>();
                    ipPortList.addAll(MultiMatch.match(code, config.get("producer").get("ipPort")));
                    for (String ipPort : ipPortList) {
                        if(cache.hget("XiciIP",ipPort) == null){
                            cache.lpush("raw_ip",ipPort);
                            cache.hset("XiciIP",ipPort,"");
                        }
                    }
                }
            }
            logger.info("所有列表爬取完毕，休眠1个小时");
            ThreadSleep.sleep(3600);
        }
    }
    private String getHtmlCode(String url){
        ThreadSleep.sleep(30);
        return UrlConnectionUtil.getHtmlCode(url,config,config.get("producer").get("coding"));
    }
}
