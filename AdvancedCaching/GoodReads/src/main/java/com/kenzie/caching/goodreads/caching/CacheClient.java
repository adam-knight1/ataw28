package com.kenzie.caching.goodreads.caching;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.inject.Inject;

public class CacheClient {
    private final JedisPool pool;


    @Inject
    public CacheClient(JedisPool jedisPool) {
        this.pool = jedisPool;
    }


        public void setValue(String key, int seconds, String value) {
            if (key == null) {
                throw new IllegalArgumentException("Key is null");
            }

            try (Jedis jedis = pool.getResource()) {
                jedis.setex(key,seconds,value);

            }

    }
}
