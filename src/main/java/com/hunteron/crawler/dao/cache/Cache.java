package com.hunteron.crawler.dao.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Cache {

    String get(String key);

    String set(String key, String value);

    Long incr(String key);

    String getSet(String key, String value);

    Long setnx(String key, String value);

    List<String> mget(String... keys);

    Long hset(String key, String field, String value);

    Long hsetnx(String key, String field, String value);

    String hget(String key, String field);

    Map<String, String> hgetAll(String key);

    Set<String> keys(String reg);

    boolean exists(String key);

    Long expire(String key, int seconds);

    Long del(String key);

    Double incrByFloat(String key, double value);

    Long lpush(final String key, final String... strings);

    Long rpush(final String key, final String... strings);

    Long llen(final String key);

    String lpop(final String key);

    String rpop(final String key);

    String lindex(final String key, final long index);

}
