package com.hunteron.crawler.dao.cache;

import com.hunteron.crawler.configuration.Path;
import com.hunteron.crawler.configuration.RegularParser;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;

public class SingletonRedis {

    private static class Stn{
        private static JedisPool pool;
        private static JedisPoolConfig config = new JedisPoolConfig();
        private static Logger logger = LogManager.getLogger(SingletonRedis.class.getName());
        private static HashMap<String,String> map = RegularParser.getXML(Path.REDIS).get("redis");
        private static Stn stn = new Stn();

        Stn() {

            int port = 6379;
            try {
                port = Integer.parseInt(map.get("port"));
            }catch (NumberFormatException e){
                logger.error("redis-port:配置错误,请检查配置");
            }
            config.setMaxTotal(6000);
            config.setMaxIdle(1000);
            config.setMaxWaitMillis(20000);
            config.setTestOnBorrow(true);
            if(StringUtils.isBlank(map.get("password"))){
                pool = new JedisPool(config, map.get("host"),port,10000);
            }else{
                pool = new JedisPool(config, map.get("host"),port,10000,map.get("password"));
            }
        }

        JedisPool getJedisConnection(){
            return pool;
        }

        public static void close(Jedis jedis) {
            if(null != jedis){
                jedis.close();
            }
        }

        public void destroy(){
            pool.destroy();
        }

        public static Stn getInstance(){
            return stn;
        }
    }
    static JedisPool getJedisConnection(){
        return Stn.getInstance().getJedisConnection();
    }
}
