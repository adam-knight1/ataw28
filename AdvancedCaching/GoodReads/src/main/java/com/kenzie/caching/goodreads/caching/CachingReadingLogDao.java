package com.kenzie.caching.goodreads.caching;


import com.kenzie.caching.goodreads.dao.ReadingLogDao;
import com.kenzie.caching.goodreads.dao.models.ReadingLog;

import java.time.ZonedDateTime;
import java.util.Optional;
import javax.inject.Inject;

import static com.kenzie.caching.goodreads.caching.CacheClient.constructCacheKey;

public class CachingReadingLogDao implements ReadingLogDao {

    private CacheClient cacheClient;
    private final ReadingLogDao nonCachingReadingLogDao;

    @Inject
    public CachingReadingLogDao(CacheClient cacheClient, ReadingLogDao nonCachingReadingLogDao) {
        this.cacheClient = cacheClient;
        this.nonCachingReadingLogDao = nonCachingReadingLogDao;
    }

    @Override
    public ReadingLog updateReadingProgress(String userId, String isbn, ZonedDateTime timestamp,
                                            int pageNumber, boolean isFinished) {

        if (isFinished) {
            int currentYear = ZonedDateTime.now().getYear();
            String cacheKey = constructCacheKey(userId, currentYear);
            cacheClient.invalidate(cacheKey);
        }
        return nonCachingReadingLogDao.updateReadingProgress(userId, isbn, timestamp, pageNumber, isFinished);
    }

    @Override
    public int getBooksReadInYear(String userId, int year) {
        String cacheKey = constructCacheKey(userId, year);

        Optional<Integer> booksRead = cacheClient.getBooksRead(cacheKey);

        if (booksRead.isPresent()) {
            return booksRead.get();
        } else {
            int booksReadCount = nonCachingReadingLogDao.getBooksReadInYear(userId, year);
            cacheClient.setValue(cacheKey, booksReadCount);

            return booksReadCount;
        }
    }
}
