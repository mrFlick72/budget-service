package it.valeriovaudi.onlyoneportal.budgetservice.web.config;


import it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository.*;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetExpenseRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetRevenueRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.searchtag.DynamoDBSearchTagRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.searchtag.SearchTagRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                                                               UserRepository userRepository) {

        return new DynamoDbBudgetRevenueRepository(tableName, dynamoDbClient,
                new BudgetDynamoDbIdFactory(new UUIDSaltGenerator()),
                userRepository, new DynamoDbAttributeValueFactory());
    }

    @Bean
    public BudgetExpenseRepository budgetExpenseRepository(DynamoDbClient dynamoDbClient,
                                                           @Value("${budget-service.dynamo-db.budget-expense.table-name}") String tableName,
                                                           UserRepository userRepository) {

        return new DynamoDbBudgetExpenseRepository(tableName, dynamoDbClient,
                new BudgetDynamoDbIdFactory(new UUIDSaltGenerator()),
                userRepository, new DynamoDbAttributeValueFactory());
    }

    @Bean
    public SearchTagRepository searchTagRepository(DynamoDbClient dynamoDbClient,
                                                   @Value("${budget-service.dynamo-db.search-tags.table-name}") String tableName,
                                                   UserRepository userRepository) {
        return new DynamoDBSearchTagRepository(tableName, userRepository, dynamoDbClient, new DynamoDbAttributeValueFactory());
    }

    @Bean
    public RestTemplate repositoryServiceRestTemplate() {
        return new RestTemplateBuilder().build();
    }
}
