package com.hunteron.crawler.dao.cache;

import com.hunteron.crawler.configuration.Path;
import com.hunteron.crawler.configuration.RegularParser;
import com.hunteron.crawler.util.ArithUtil;
import org.apache.logging.log4j.LogManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisCache implements Cache {

    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(RedisCache.class.getName());
    private static JedisPool pool = SingletonRedis.getJedisConnection();
    private static JedisPool getPool(){
        return pool;
    }
    private static HashMap<String,HashMap<String,String>> config = RegularParser.getXML(Path.REDIS);
    private static int dbNum = Integer.parseInt(config.get("redis").get("dbnum"));

    public RedisCache(){

    }
    public RedisCache(int dbNum){
        this.dbNum = dbNum;
    }
    /**
     * 内部方法接口,用于闭包回调jedis具体调用
     * @param <E>
     * @param <T>
     */
    private interface Function<E,T>{
        T callback(E e);
    }

    /**
     * 装饰器封装获取连接池对象
     * fianlly 回收连接对象
     * @param fun   闭包回调,等待具体实现jedis调用
     * @param <T>   泛型返回值
     * @return jedis
     */
    private <T> T execute(Function<Jedis,T> fun) {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();
            jedis.select(dbNum);
            return fun.callback(jedis);
        } catch (JedisConnectionException e ){
            logger.error("Jedis连接异常,未获取到资源");
            return null;
        } finally {
            if(null != jedis){
                jedis.close();
            }
        }
    }

    @Override
    public String get(final String key) {
        return execute(new Function<Jedis, String>() {
            @Override
            public String callback(Jedis jedis) {
                return jedis.get(key);
            }
        });
    }

    @Override
    public String set(final String key, final String value) {

        return execute(new Function<Jedis, String>() {
            @Override
            public String callback(Jedis jedis) {
                return jedis.set(key,value);
            }
        });
    }

    @Override
    public Long incr(final String key) {
        return execute(new Function<Jedis,Long>() {
            @Override
            public Long callback(Jedis jedis) {
                return jedis.incr(key);
            }
        });
    }

    @Override
    public String getSet(final String key, final String value) {
        return execute(new Function<Jedis, String>() {
            @Override
            public String callback(Jedis jedis) {
                return jedis.getSet(key,value);
            }
        });
    }

    @Override
    public Long setnx(final String key, final String value) {

        /*return execute(jedis1 -> (jedis1.setnx(key,value)));*/
        return execute(new Function<Jedis,Long>() {
            public Long callback(Jedis jedis) {
                return jedis.setnx(key,value);
            }
        });
    }

    @Override
    public Set<String> keys(final String reg) {

        /*return execute(jedis1 -> (jedis1.keys(reg)));*/
        return execute(new Function<Jedis, Set<String>>() {
            @Override
            public Set<String> callback(Jedis jedis) {
                return jedis.keys(reg);
            }
        });
    }

    @Override
    public boolean exists(final String key) {

        /*return execute(jedis -> (jedis.exists(key)));*/
        return execute(new Function<Jedis, Boolean>() {
            @Override
            public Boolean callback(Jedis jedis) {
                return jedis.exists(key);
            }
        });
    }

    @Override
    public List<String> mget(final String... keys){
        /*return execute(jedis -> (jedis.mget(keys)));*/
        return execute(new Function<Jedis, List<String>>() {
            @Override
            public List<String> callback(Jedis jedis) {
                return jedis.mget(keys);
            }
        });
    }

    @Override
    public Long hset(final String key, final String field, final String value) {
        return execute(new Function<Jedis,Long>() {
            public Long callback(Jedis jedis) {
                return jedis.hset(key,field,value);
            }
        });
    }

    @Override
    public Long hsetnx(final String key, final String field, final String value) {
        return execute(new Function<Jedis,Long>() {
            public Long callback(Jedis jedis) {
                return jedis.hsetnx(key,field,value);
            }
        });
    }

    @Override
    public String hget(final String key, final String field) {
        return execute(new Function<Jedis, String>() {
            @Override
            public String callback(Jedis jedis) {
                return jedis.hget(key,field);
            }
        });
    }

    @Override
    public Map<String, String> hgetAll(final String key) {
        return execute(new Function<Jedis,Map<String,String>>() {
            @Override
            public Map<String, String> callback(Jedis jedis) {
                return jedis.hgetAll(key);
            }
        });
    }

    @Override
    public Long expire(final String key, final int seconds) {
        /*return execute(jedis1 -> (jedis1.setnx(key,value)));*/
        return execute(new Function<Jedis,Long>() {
            public Long callback(Jedis jedis) {
                return jedis.expire(key,seconds);
            }
        });
    }

    @Override
    public Long del(final String key) {
        return execute(new Function<Jedis, Long>() {
            @Override
            public Long callback(Jedis jedis) {
                return jedis.del(key);
            }
        });
    }

    @Override
    public Double incrByFloat(final String key, final double value) {
        return execute(new Function<Jedis, Double>() {
            @Override
            public Double callback(Jedis jedis) {
                return ArithUtil.roundUp(jedis.incrByFloat(key,value),2);
            }
        });
    }
    @Override
    public Long lpush(final String key, final String... strings) {
        return execute(new Function<Jedis, Long>() {
            @Override
            public Long callback(Jedis jedis) {
                return jedis.lpush(key,strings);
            }
        });
    }
    @Override
    public Long rpush(final String key, final String... strings) {
        return execute(new Function<Jedis, Long>() {
            @Override
            public Long callback(Jedis jedis) {
                return jedis.rpush(key,strings);
            }
        });    }

    @Override
    public Long llen(final String key) {
        return execute(new Function<Jedis, Long>() {
            @Override
            public Long callback(Jedis jedis) {
                return jedis.llen(key);
            }
        });
    }
    @Override
    public String lpop(final String key) {
        return execute(new Function<Jedis,String>() {
            public String callback(Jedis jedis) {
                return jedis.lpop(key);
            }
        });
    }
    @Override
    public String rpop(final String key) {
        return execute(new Function<Jedis,String>() {
            public String callback(Jedis jedis) {
                return jedis.rpop(key);
            }
        });
    }

    @Override
    public String lindex(final String key,final long index) {
        return execute(new Function<Jedis,String>() {
            public String callback(Jedis jedis) {
                return jedis.lindex(key,index);
            }
        });
    }
}
