package com.hunteron.crawler.handler.xicidaili;

import com.hunteron.crawler.configuration.Path;
import com.hunteron.crawler.configuration.RegularParser;
import com.hunteron.crawler.downloader.UrlConnectionUtil;
import com.hunteron.crawler.handler.ProcessTemplate;
import com.hunteron.crawler.util.MultiMatch;
import com.hunteron.crawler.util.ThreadSleep;
import joptsimple.internal.Strings;
import org.apache.logging.log4j.LogManager;

import java.util.HashMap;
import java.util.LinkedList;

public class XiciProcess extends ProcessTemplate {

    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(XiciProcess.class.getName());
    private static HashMap<String,HashMap<String,String>> config = RegularParser.getXML(Path.XICI);
    @Override
    public void handle() {
        while(true) {
            for (int i = 1; i <= 5; i++) {
                String urlCode = null;
                while(Strings.isNullOrEmpty(urlCode)){
                    urlCode = getHtmlCode("http://www.xicidaili.com/nn/" + i);
                }
                urlCode = urlCode.replaceAll("\\s", "");

                LinkedList<String> ipPortList = new LinkedList<>();
                ipPortList.addAll(MultiMatch.match(urlCode, config.get("producer").get("ipPort"), ""));
                for (String ipPort : ipPortList) {
                    ipPort = ipPort.replaceAll("</td><td>", ":");
                    if(cache.hget("XiciIP",ipPort) == null){
                        cache.lpush("raw_ip",ipPort);
                        cache.hset("XiciIP",ipPort,"");
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
