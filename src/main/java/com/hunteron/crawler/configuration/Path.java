package com.hunteron.crawler.configuration;

/**
 * 配置文件路径
 */
public enum Path {

    MYSQL("conf/mysql.xml"),
    REDIS("conf/redis.xml"),
    KAFKA("conf/kafka.xml"),
    PROXY("conf/proxy.xml"),
    CRAWLER("crawler.xml"),
    XICI("website/xicidaili.xml"),
    YOU("website/youdaili.xml"),
    NEWS_SINA("website/news_sina.xml");

    private final String value;
    Path(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}

