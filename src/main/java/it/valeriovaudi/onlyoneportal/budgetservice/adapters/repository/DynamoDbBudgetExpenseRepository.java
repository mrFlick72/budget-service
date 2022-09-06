package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.user.UserName;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetExpenseRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.UserRepository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DynamoDbBudgetExpenseRepository implements BudgetExpenseRepository {
    private final String tableName;
    private final DynamoDbClient dynamoClient;
    private final BudgetDynamoDbIdFactory idFactory;
    private final UserRepository userRepository;
    private final DynamoDbAttributeValueFactory attributeValueFactory;

    public DynamoDbBudgetExpenseRepository(String tableName,
                                           DynamoDbClient dynamoClient,
                                           BudgetDynamoDbIdFactory idFactory,
                                           UserRepository userRepository,
                                           DynamoDbAttributeValueFactory attributeValueFactory) {
        this.tableName = tableName;
        this.dynamoClient = dynamoClient;
        this.idFactory = idFactory;
        this.userRepository = userRepository;
        this.attributeValueFactory = attributeValueFactory;
    }

    @Override
    public Optional<BudgetExpense> findFor(BudgetExpenseId budgetExpenseId) {
        HashMap<String, AttributeValue> itemKeyCondition = itemKeyConditionFor(budgetExpenseId);
        return dynamoClient.query(
                        QueryRequest.builder()
                                .tableName(tableName)
                                .keyConditionExpression("pk =:pk AND range_key =:range_key")
                                .expressionAttributeValues(itemKeyCondition)
                                .build()
                ).items()
                .stream()
                .findFirst()
                .map(this::fromDynamoDbToModel);
    }

    private HashMap<String, AttributeValue> itemKeyConditionFor(BudgetExpenseId budgetExpenseId) {
        HashMap<String, AttributeValue> itemKeyCondition = new HashMap<>();
        itemKeyCondition.put(":pk", attributeValueFactory.stringAttributeFor(idFactory.partitionKeyFor(budgetExpenseId)));
        itemKeyCondition.put(":range_key", attributeValueFactory.stringAttributeFor(idFactory.rangeKeyFor(budgetExpenseId)));
        return itemKeyCondition;
    }

    @Override
    public List<BudgetExpense> findByDateRange(UserName userName, Date star, Date end, String... searchTags) {
        return null;
    }

    @Override
    public BudgetExpense save(BudgetExpense budgetExpense) {
        BudgetExpense budgetExpenseToSave = new BudgetExpense(
                idFactory.budgetIdFrom(budgetExpense),
                budgetExpense.getUserName(),
                budgetExpense.getDate(),
                budgetExpense.getAmount(),
                budgetExpense.getNote(),
                budgetExpense.getTag()
        );
        dynamoClient.putItem(
                PutItemRequest.builder()
                        .tableName(tableName)
                        .item(putItemPayloadFor(budgetExpenseToSave))
                        .build()
        );

        return budgetExpenseToSave;
    }

    private BudgetExpense fromDynamoDbToModel(Map<String, AttributeValue> item) {
        return new BudgetExpense(
                new BudgetExpenseId(item.get("budget_id").s()),
                new UserName(item.get("user_name").s()),
                Date.dateFor(item.get("date").s()),
                Money.moneyFor(item.get("amount").s()),
                item.get("note").s(),
                item.get("tag").s()
        );
    }

    private Map<String, AttributeValue> putItemPayloadFor(BudgetExpense budgetExpense) {
        Map<String, AttributeValue> payload = new HashMap<>();

        payload.put("pk", attributeValueFactory.stringAttributeFor(idFactory.partitionKeyFor(budgetExpense.getId())));
        payload.put("range_key", attributeValueFactory.stringAttributeFor(idFactory.rangeKeyFor(budgetExpense.getId())));

        payload.put("budget_id", attributeValueFactory.stringAttributeFor(budgetExpense.getId().getContent()));
        payload.put("user_name", attributeValueFactory.stringAttributeFor(userRepository.currentLoggedUserName().getContent()));
        payload.put("date", attributeValueFactory.stringAttributeFor(budgetExpense.getDate().formattedDate()));
        payload.put("amount", attributeValueFactory.stringAttributeFor(budgetExpense.getAmount().stringifyAmount()));
        payload.put("note", attributeValueFactory.stringAttributeFor(budgetExpense.getNote()));
        payload.put("tag", attributeValueFactory.stringAttributeFor(budgetExpense.getTag()));

        return payload;
    }

    @Override
    public void delete(BudgetExpenseId idBudgetExpense) {

    }
}