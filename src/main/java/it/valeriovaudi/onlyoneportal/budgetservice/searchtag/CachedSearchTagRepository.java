package it.valeriovaudi.onlyoneportal.budgetservice.searchtag;

import it.valeriovaudi.onlyoneportal.budgetservice.user.UserName;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static it.valeriovaudi.onlyoneportal.budgetservice.infrastructure.strings.Sha256Utils.*;

public class CachedSearchTagRepository implements SearchTagRepository {
    private final String cacheName;
    private final int cacheTtlMillis;
    private final SearchTagRepository repository;
    private final RedisTemplate redisTemplate;
    private final UserRepository userRepository;

    public CachedSearchTagRepository(String cacheName,
                                     Integer cacheTtlMillis,
                                     RedisTemplate redisTemplate,
                                     UserRepository userRepository,
                                     SearchTagRepository repository) {

        Assert.notNull(cacheTtlMillis, "The cache ttl can not be null");
        Assert.isTrue(cacheTtlMillis > 0, "The cache ttl has to be not negative");
        Assert.isTrue(StringUtils.hasText(cacheName), "The cache name has to be not null or empty");

        this.cacheName = cacheName;
        this.cacheTtlMillis = cacheTtlMillis;
        this.repository = repository;
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
    }

    @Override
    public SearchTag findSearchTagBy(String key) {
        UserName userName = userRepository.currentLoggedUserName();

        return Optional.ofNullable(getFromCache(key, userName))
                .orElseGet(() -> {
                    SearchTag searchTag = repository.findSearchTagBy(key);
                    storeInCache(userName, searchTag);
                    return searchTag;
                });
    }

    @Override
    public List<SearchTag> findAllSearchTag() {
        UserName userName = userRepository.currentLoggedUserName();

        return Optional.ofNullable(getFromCache(userName))
                .orElseGet(() -> {
                    List<SearchTag> allSearchTag = repository.findAllSearchTag();
                    storeInCache(userName, allSearchTag);
                    return allSearchTag;
                });
    }

    @Override
    public void save(SearchTag searchTag) {
        UserName userName = userRepository.currentLoggedUserName();
        repository.save(searchTag);
        storeInCache(userName, searchTag);
    }

    @Override
    public void delete(String key) {

    }


    private String cacheKeyFor(UserName userName, String searchTagKey) {
        return String.format("%s_%s_%s", cacheName, userName.content(), searchTagKey);
    }

    private String cacheKeyFor(UserName userName) {
        return String.format("%s_%s", cacheName, userName.content());
    }

    private SearchTag getFromCache(String searchTagKey, UserName userName) {
        String cacheKey = cacheKeyFor(userName, searchTagKey);

        return (SearchTag) redisTemplate.opsForHash().get(cacheKey, sha256For(cacheKey));
    }

    private List<SearchTag> getFromCache(UserName userName) {
        String cacheKey = cacheKeyFor(userName);
        return (List<SearchTag>) redisTemplate.opsForHash().get(cacheKey, sha256For(cacheKey));
    }

    private void storeInCache(UserName userName, SearchTag cachedSearchTag) {
        String cacheKey = cacheKeyFor(userName, cachedSearchTag.key());
        redisTemplate.opsForHash().put(cacheKey, sha256For(cacheKey), cachedSearchTag);
        redisTemplate.opsForHash().getOperations().expire(cacheKey, Duration.ofMillis(cacheTtlMillis));
    }

    private void storeInCache(UserName userName, List<SearchTag> cachedSearchTag) {
        String cacheKey = cacheKeyFor(userName);
        redisTemplate.opsForHash().put(cacheKey, sha256For(cacheKey), cachedSearchTag);
        redisTemplate.opsForHash().getOperations().expire(cacheKey, Duration.ofMillis(cacheTtlMillis));
    }

}