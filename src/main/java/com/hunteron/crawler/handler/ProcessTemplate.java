package com.hunteron.crawler.handler;

import com.hunteron.crawler.dao.cache.Cache;
import com.hunteron.crawler.dao.cache.RedisCache;
import com.hunteron.crawler.model.Param;
import com.hunteron.crawler.util.SingleMatch;

import java.util.concurrent.LinkedBlockingQueue;

public class ProcessTemplate {

    public static Cache cache = new RedisCache();

    public void handle(LinkedBlockingQueue<Param> paramQueue) {

    }

    public void handle() {

    }

    public int getMatchInt(String code,String reg){
        return Integer.parseInt(SingleMatch.match(code,reg));
    }
}
