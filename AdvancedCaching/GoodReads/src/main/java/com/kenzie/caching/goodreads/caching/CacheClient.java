package com.kenzie.caching.goodreads.caching;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.inject.Inject;
import java.util.Optional;

public class CacheClient {
    private final JedisPool pool;


    @Inject
    public CacheClient(JedisPool jedisPool) {
        this.pool = jedisPool;
    }

    public void setValue(String key, int booksRead) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null");
        }

        try (Jedis jedis = pool.getResource()) {
            jedis.setex(key, 3600, Integer.toString(booksRead));
        }
    }

    public Optional<Integer> getBooksRead(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null");
        }

        try (Jedis jedis = pool.getResource()) {
            String value = jedis.get(key);
            return Optional.ofNullable(value).map(Integer::valueOf);
        }
    }

    public boolean invalidate(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null");
        }

        try (Jedis jedis = pool.getResource()) {
            return jedis.del(key) > 0;
        }
    }

    public static String constructCacheKey(String userId, int year) {
        return "books-read::" + userId + "::" + year;
    }
}


