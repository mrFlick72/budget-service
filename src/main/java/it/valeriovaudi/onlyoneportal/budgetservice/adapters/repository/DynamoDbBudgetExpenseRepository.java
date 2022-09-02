package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.user.UserName;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetExpenseRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.UserRepository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.*;

public class DynamoDbBudgetExpenseRepository implements BudgetExpenseRepository {
    private final String tableName;
    private final DynamoDbClient dynamoClient;
    private final BudgetDynamoDbIdFactory idFactory;
    private final UserRepository userRepository;

    public DynamoDbBudgetExpenseRepository(String tableName,
                                           DynamoDbClient dynamoClient,
                                           BudgetDynamoDbIdFactory idFactory,
                                           UserRepository userRepository) {
        this.tableName = tableName;
        this.dynamoClient = dynamoClient;
        this.idFactory = idFactory;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<BudgetExpense> findFor(BudgetExpenseId budgetExpenseId) {
        return Optional.empty();
    }

    @Override
    public List<BudgetExpense> findByDateRange(UserName userName, Date star, Date end, String... searchTags) {
        return null;
    }

    @Override
    public void save(BudgetExpense budgetExpense) {
        dynamoClient.putItem(
                PutItemRequest.builder()
                        .tableName(tableName)
                        .item(putItemPayloadFor(budgetExpense))
                        .build()
        );

    }

    private Map<String, AttributeValue> putItemPayloadFor(BudgetExpense budgetExpense) {
        Map<String, AttributeValue> payload = new HashMap<>();

        payload.put("pk", AttributeValue.builder().s(idFactory.partitionKeyFor(budgetExpense)).build());
        payload.put("range_key", AttributeValue.builder().s(idFactory.rangeKeyFor(budgetExpense)).build());

        payload.put("budget_id", AttributeValue.builder().s(budgetExpense.getId().getContent()).build());
        payload.put("user_name", AttributeValue.builder().s(userRepository.currentLoggedUserName().getContent()).build());
        payload.put("date", AttributeValue.builder().s(budgetExpense.getDate().formattedDate()).build());
        payload.put("amount", AttributeValue.builder().s(budgetExpense.getAmount().stringifyAmount()).build());
        payload.put("note", AttributeValue.builder().s(budgetExpense.getNote()).build());
        payload.put("tag", AttributeValue.builder().s(budgetExpense.getTag()).build());

        return payload;
    }

    @Override
    public void delete(BudgetExpenseId idBudgetExpense) {

    }
}