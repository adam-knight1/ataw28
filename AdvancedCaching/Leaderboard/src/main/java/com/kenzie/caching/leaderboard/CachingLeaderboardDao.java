package com.kenzie.caching.leaderboard;

import com.kenzie.caching.leaderboard.resources.datasource.Entry;
import com.kenzie.caching.leaderboard.resources.datasource.LeaderboardDao;

import javax.inject.Inject;
import java.util.Optional;

public class CachingLeaderboardDao {
    private final LeaderboardDao dataSource;
    private final CacheClient cache;
    private static final int TTL = 300;

    /**
     * Constructor.
     *
     * @param dataSource LeaderboardDAO object
     * @param cache      CacheClient object
     */
    @Inject
    public CachingLeaderboardDao(LeaderboardDao dataSource, CacheClient cache) {
        this.dataSource = dataSource;
        this.cache = cache;
    }

    /**
     * Retrieves score associated with the specified user. Should use the cache when possible, but the dataSource object
     * is our source of truth for high scores. The TTL for our high scores should be 5 minutes.
     * <p>
     * PARTICIPANTS: replace return 0 with your implementation of this method.
     *
     * @param username String representing player username
     * @return long representing score associated with username
     */
    public long getHighScore(String username) {
        Optional<String> cachedScore = cache.getValue(username);
        if (cachedScore.isPresent()) {
            return Long.parseLong(cachedScore.get()); //string?? ParseLong
        } else {
            Entry entry = dataSource.getEntry(username);
            if (entry != null) {
                long score = entry.getScore();
                cache.setValue(username, TTL, String.valueOf(score));
                return score;
            } else {
                System.out.println("user not found");
                return 0;
            }
        }
    }

    public void invalidateScore(String username) {
        cache.invalidate(username);
    }

}
