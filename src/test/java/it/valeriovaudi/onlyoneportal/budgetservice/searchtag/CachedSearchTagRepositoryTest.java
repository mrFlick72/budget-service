package it.valeriovaudi.onlyoneportal.budgetservice.searchtag;

import it.valeriovaudi.onlyoneportal.budgetservice.user.UserName;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static it.valeriovaudi.onlyoneportal.budgetservice.infrastructure.strings.Sha256Utils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CachedSearchTagRepositoryTest {

    @Mock
    private SearchTagRepository repository;

    @Mock
    private UserRepository userRepository;

    private RedisTemplate<String, SearchTag> redisTemplate;

    private CachedSearchTagRepository underTest;

    @BeforeEach
    void setup() {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory("localhost", 6379);
        connectionFactory.afterPropertiesSet();
        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.afterPropertiesSet();
        underTest = new CachedSearchTagRepository("search_tag_cache", 1000, redisTemplate, userRepository, repository);

        given(userRepository.currentLoggedUserName())
                .willReturn(new UserName("anemail@test.com"));
    }

    @AfterEach
    void tearDown() {
        reset(repository);
        reset(userRepository);
    }

    @Test
    void whenASearchTagIsStored() {
        String cacheKey = "search_tag_cache_anemail@test.com_key";
        SearchTag searchTag = new SearchTag("key", "value");

        underTest.save(searchTag);

        SearchTag actual = (SearchTag) redisTemplate.opsForHash().get(cacheKey, sha256For(cacheKey));

        assertEquals(searchTag, actual);
        verify(repository).save(searchTag);
    }

    @Test
    void whenASearchTagIsRetrievedFromTheCache() {
        String cacheKey = "search_tag_cache_anemail@test.com_key";
        SearchTag searchTag = new SearchTag("key", "value");

        redisTemplate.opsForHash().put(cacheKey, sha256For(cacheKey), searchTag);

        SearchTag actual = underTest.findSearchTagBy("key");

        SearchTag expectedFormTheCache = (SearchTag) redisTemplate.opsForHash().get(cacheKey, sha256For(cacheKey));

        assertEquals(expectedFormTheCache, actual);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void whenASearchTagIsRetrievedFromTheDatabase() {
        String cacheKey = "search_tag_cache_anemail@test.com_key";
        String searchTagKey = "key";
        SearchTag searchTag = new SearchTag(searchTagKey, "value");

        given(repository.findSearchTagBy(searchTagKey))
                .willReturn(searchTag);

        SearchTag actual = underTest.findSearchTagBy(searchTagKey);

        SearchTag expectedFormTheCache = (SearchTag) redisTemplate.opsForHash().get(cacheKey, sha256For(cacheKey));

        assertEquals(expectedFormTheCache, actual);
    }
}