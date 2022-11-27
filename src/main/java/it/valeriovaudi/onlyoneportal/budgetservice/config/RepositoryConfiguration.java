package it.valeriovaudi.onlyoneportal.budgetservice.config;


import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.repository.BudgetExpenseRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.repository.DynamoDbBudgetExpenseRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue.BudgetRevenueRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue.DynamoDbBudgetRevenueRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.infrastructure.dynamodb.*;
import it.valeriovaudi.onlyoneportal.budgetservice.searchtag.CachedSearchTagRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.searchtag.DynamoDBSearchTagRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.searchtag.SearchTagRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.user.SpringSecurityUserRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration(proxyBeanMethods = false)
public class RepositoryConfiguration {

    @Bean
    public UserRepository userRepository() {
        return new SpringSecurityUserRepository();
    }

    @Bean
    public BudgetRevenueRepository budgetRevenueRepository(DynamoDbClient dynamoDbClient,
                                                           @Value("${budget-service.dynamo-db.budget-revenue.table-name}") String tableName,
                                                           UserRepository userRepository, SaltGenerator saltGenerator) {

        return new DynamoDbBudgetRevenueRepository(tableName, dynamoDbClient,
                new BudgetRevenueDynamoDbIdFactory(saltGenerator),
                userRepository, new DynamoDbAttributeValueFactory());
    }

    @Bean
    public BudgetExpenseRepository budgetExpenseRepository(DynamoDbClient dynamoDbClient,
                                                           @Value("${budget-service.dynamo-db.budget-expense.table-name}") String tableName,
                                                           UserRepository userRepository, SaltGenerator saltGenerator) {

        return new DynamoDbBudgetExpenseRepository(tableName, dynamoDbClient,
                new BudgetExpenseDynamoDbIdFactory(saltGenerator),
                userRepository, new DynamoDbAttributeValueFactory());
    }

    @Bean
    public SearchTagRepository searchTagRepository(DynamoDbClient dynamoDbClient,
                                                   RedisTemplate redisTemplate,
                                                   @Value("${budget-service.dynamo-db.search-tags.cache-name}") String cacheName,
                                                   @Value("${budget-service.dynamo-db.search-tags.cache-ttl}") Integer cacheTtl,
                                                   @Value("${budget-service.dynamo-db.search-tags.table-name}") String tableName,
                                                   UserRepository userRepository) {
        DynamoDBSearchTagRepository repository = new DynamoDBSearchTagRepository(tableName, userRepository, dynamoDbClient, new DynamoDbAttributeValueFactory());
        return new CachedSearchTagRepository(cacheName, cacheTtl, redisTemplate, userRepository, repository);
    }

    @Bean
    public RestTemplate repositoryServiceRestTemplate() {
        return new RestTemplateBuilder().build();
    }

    @Bean
    public SaltGenerator saltGenerator() {
        return new UUIDSaltGenerator();
    }
}
