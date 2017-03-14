package com.hunteron.crawler.handler;

import com.hunteron.crawler.dao.cache.Cache;
import com.hunteron.crawler.dao.cache.RedisCache;
import com.hunteron.crawler.model.Param;

public abstract class DetailProcessTemplate {
	public Cache cache = new RedisCache();
	public abstract void handle(Param param);
}
