package com.kenzie.caching.goodreads.caching;


import com.kenzie.caching.goodreads.dao.ReadingLogDao;
import com.kenzie.caching.goodreads.dao.models.ReadingLog;

import java.time.ZonedDateTime;
import javax.inject.Inject;

public class CachingReadingLogDao implements ReadingLogDao {

    private CacheClient cacheClient;
    private final ReadingLogDao nonCachingReadingLogDao;

    @Inject
    public CachingReadingLogDao(CacheClient cacheClient,ReadingLogDao nonCachingReadingLogDao) {
        this.cacheClient = cacheClient;
        this.nonCachingReadingLogDao = nonCachingReadingLogDao;


    }

    @Override
    public ReadingLog updateReadingProgress(String userId, String isbn, ZonedDateTime timestamp,
                                            int pageNumber, boolean isFinished) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getBooksReadInYear(String userId, int year) {
        throw new UnsupportedOperationException();
    }
}
