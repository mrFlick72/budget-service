package it.valeriovaudi.onlyoneportal.budgetservice.web.config;


import it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository.*;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetExpenseRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetRevenueRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.SearchTagRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public UserRepository userRepository() {
        return new SpringSecurityUserRepository();
    }

    @Bean
    public BudgetRevenueRepository jdbcBudgetRevenueRepository(DynamoDbClient dynamoDbClient,
                                                               @Value("${budget-service.dynamo-db.budget-revenue.table-name}") String tableName,
                                                               UserRepository userRepository, JdbcTemplate jdbcTemplate) {
        JdbcBudgetRevenueRepository jdbcBudgetRevenueRepository = new JdbcBudgetRevenueRepository(jdbcTemplate);
        DynamoDbBudgetRevenueRepository dynamoDbBudgetExpenseRepository = new DynamoDbBudgetRevenueRepository(tableName, dynamoDbClient,
                new BudgetDynamoDbIdFactory(new UUIDSaltGenerator()),
                userRepository, new DynamoDbAttributeValueFactory());
        new CompositeBudgetRevenueRepository(
                dynamoDbBudgetExpenseRepository,
                jdbcBudgetRevenueRepository
        );
        return dynamoDbBudgetExpenseRepository;
    }

    @Bean
    public BudgetExpenseRepository budgetExpenseRepository(DynamoDbClient dynamoDbClient,
                                                           @Value("${budget-service.dynamo-db.budget-expense.table-name}") String tableName,
                                                           UserRepository userRepository, JdbcTemplate jdbcTemplate) {
        JdbcBudgetExpenseRepository jdbcBudgetExpenseRepository = new JdbcBudgetExpenseRepository(jdbcTemplate);
        DynamoDbBudgetExpenseRepository dynamoDbBudgetExpenseRepository = new DynamoDbBudgetExpenseRepository(tableName, dynamoDbClient,
                new BudgetDynamoDbIdFactory(new UUIDSaltGenerator()),
                userRepository, new DynamoDbAttributeValueFactory());
        new CompositeBudgetExpenseRepository(
                dynamoDbBudgetExpenseRepository,
                jdbcBudgetExpenseRepository
        );
        return dynamoDbBudgetExpenseRepository;
    }

    @Bean
    public SearchTagRepository searchTagRepository(DynamoDbClient dynamoDbClient,
                                                   @Value("${budget-service.dynamo-db.search-tags.table-name}") String tableName,
                                                   UserRepository userRepository, JdbcTemplate jdbcTemplate) {
        JdbcSearchTagRepository jdbcSearchTagRepository = new JdbcSearchTagRepository(userRepository, jdbcTemplate);
        DynamoDBSearchTagRepository dynamoDBSearchTagRepository = new DynamoDBSearchTagRepository(tableName, userRepository, dynamoDbClient, new DynamoDbAttributeValueFactory());
        return new CompositeSearchTagRepository(dynamoDBSearchTagRepository, jdbcSearchTagRepository);
    }

    @Bean
    public RestTemplate repositoryServiceRestTemplate() {
        return new RestTemplateBuilder().build();
    }
}
