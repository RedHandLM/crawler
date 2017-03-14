package com.hunteron.crawler.downloader;

import com.hunteron.crawler.configuration.Path;
import com.hunteron.crawler.configuration.RegularParser;
import com.hunteron.crawler.dao.cache.Cache;
import com.hunteron.crawler.dao.cache.RedisCache;
import com.hunteron.crawler.util.ThreadSleep;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * Redis IP Pool
 *
 * row_ip 原始IP代理池
 * clean_ip 清洗raw_ip
   经过初步验证，能够成功代理的IP
 * true_ip 可用IP
   连续10次代理成功，以及在使用中未失败的IP
 * false_ip 非可用IP
   连续3次尝试都失败，丢到false_ip队列
   注1：此处IP来源于clean_ip,可用性很强，通过验证可以进一步使用
   注2：未通过验证丢回clean_ip
 */
public class HttpProxy {

    private static Logger logger = LogManager.getLogger(HttpProxy.class.getName());
    private static HashMap<String,HashMap<String,String>> config = RegularParser.getXML(Path.PROXY);
    private static Cache cache = new RedisCache();

    private static final String CHECK_URL = "http://mtest.hunteron.com:6962/im-outside/crawler/host";
    private static final String CHECK_CODING = "utf-8";

    private static final String LOCAL_IP = "61.152.166.18";
    private static final int CHECK_RATE = 10;

    //启动代理
    public static void proxy(){
        String ipPort = getIP();
        while(!isSuccess(ipPort)){
            cache.rpush("clean_ip",ipPort);
            ipPort = getIP();
            ThreadSleep.sleep(1);
        }
        logger.info("当前正使用代理，ipPort:" + ipPort);
        cache.rpush("true_ip",ipPort);
    }

    //校验代理
    public static void check(){
        if (System.currentTimeMillis() % CHECK_RATE == 0) {
            String code = UrlConnectionUtil.getHtmlCode(CHECK_URL, RegularParser.getXML(Path.PROXY), CHECK_CODING);
            if (code != null && code.trim().equals(LOCAL_IP)) {
                logger.warn("代理失败，切换代理... ...");
                HttpProxy.proxy();
            }
        }
    }

    //从缓存中获取可用的IP
    public static String getIP(){
        String trueIP = cache.lpop("true_ip");
        if(trueIP == null){
            trueIP = cache.lpop("clean_ip");
        }
        if(trueIP == null){
            throw new IllegalStateException("代理IP池为空");
        }
        return trueIP;
    }

    //使用代理，并校验
    public static boolean isSuccess(String ipPort){
        try {
            //使用代理，并校验
            String[] value = ipPort.split(":");
            System.getProperties().setProperty("http.proxyHost", value[0]);
            System.getProperties().setProperty("http.proxyPort", value[1]);
            String code = UrlConnectionUtil.getHtmlCode(CHECK_URL, config, CHECK_CODING);
            String currentIP = code.trim();
            return currentIP.equals(value[0]);
        }catch (Exception e){
            return false;
        }
    }
}