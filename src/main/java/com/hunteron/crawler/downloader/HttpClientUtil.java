package com.hunteron.crawler.downloader;

import com.hunteron.crawler.dao.cache.Cache;
import com.hunteron.crawler.dao.cache.RedisCache;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {

    //通用下载
    public static String download(String url,String encode){
        HttpClientUtil httpClient = new HttpClientUtil();
        HttpGet httpGet = new HttpGet(url);
        httpClient.config(httpGet);
        return httpClient.download(httpGet,encode);
    }
    //通用配置
    public void config(HttpRequestBase httpRequestBase) {
        httpRequestBase.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpRequestBase.setHeader("Accept-Encoding", "gzip, deflate, sdch, br");
        httpRequestBase.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        httpRequestBase.setHeader("Cache-Control", "max-age=0");
        httpRequestBase.setHeader("Connection", "keep-alive");
        httpRequestBase.setHeader("Upgrade-Insecure-Requests", "1");
        httpRequestBase.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(30000)
                .setConnectTimeout(30000)
                .setSocketTimeout(30000)
                .build();
        httpRequestBase.setConfig(requestConfig);
    }
    public String download(HttpGet httpGet,String encode) {
        CloseableHttpClient httpClient = HttpClientInit.httpClient();
        return download(httpClient, httpGet,encode);
    }
    public String proxyDownload(HttpGet httpGet,String encode) {
        Cache cache = new RedisCache();
        String[] ipPort = HttpProxy.getIP().split(":");
        HttpHost proxy = new HttpHost(ipPort[0],Integer.parseInt(ipPort[1]));
        CloseableHttpClient httpClient = HttpClientInit.httpClient(proxy);
        String code = download(httpClient, httpGet,encode);
        if(code != null) {
            cache.rpush("true_ip",ipPort);
        }else{
            cache.rpush("clean_ip",ipPort);
        }
        return download(httpClient, httpGet,encode);
    }
    public String download(CloseableHttpClient httpClient, HttpGet httpGet,String encode) {
        HttpEntity entity = null;
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet, HttpClientContext.create());
            entity = response.getEntity();
            return EntityUtils.toString(entity,encode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            EntityUtils.consumeQuietly(entity);
        }
    }
}
