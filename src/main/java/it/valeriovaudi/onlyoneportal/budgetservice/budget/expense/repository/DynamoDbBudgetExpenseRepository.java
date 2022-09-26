package it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.infrastructure.dynamodb.BudgetDynamoDbIdFactory;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserName;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;

import java.util.*;
import java.util.stream.Collectors;

public class DynamoDbBudgetExpenseRepository implements BudgetExpenseRepository {
    private final String tableName;
    private final DynamoDbClient dynamoClient;
    private final BudgetDynamoDbIdFactory<BudgetExpenseId, BudgetExpense> idFactory;
    private final DynamoDbBudgetExpenseFactory dynamoDbBudgetExpenseFactory;

    public DynamoDbBudgetExpenseRepository(String tableName,
                                           DynamoDbClient dynamoClient,
                                           BudgetDynamoDbIdFactory idFactory,
                                           DynamoDbBudgetExpenseFactory dynamoDbBudgetExpenseFactory) {
        this.tableName = tableName;
        this.dynamoClient = dynamoClient;
        this.idFactory = idFactory;
        this.dynamoDbBudgetExpenseFactory = dynamoDbBudgetExpenseFactory;
    }

    @Override
    public Optional<BudgetExpense> findFor(BudgetExpenseId budgetExpenseId) {
        HashMap<String, AttributeValue> itemKeyCondition = dynamoDbBudgetExpenseFactory.itemKeyConditionFor(budgetExpenseId);
        return dynamoClient.query(
                        QueryRequest.builder()
                                .tableName(tableName)
                                .keyConditionExpression("pk =:pk AND range_key =:range_key")
                                .expressionAttributeValues(itemKeyCondition)
                                .build()
                ).items()
                .stream()
                .findFirst()
                .map(dynamoDbBudgetExpenseFactory::fromDynamoDbToModel);
    }


    @Override
    public List<BudgetExpense> findByDateRange(UserName userName, Date star, Date end, String... searchTags) {
        Set<String> pks = dynamoDbBudgetExpenseFactory.primaryKeysFor(star, end);
        List<String> searchTagsList = Arrays.stream(searchTags).toList();
        return pks.stream()
                .flatMap(pk -> {
                    Map<String, AttributeValue> itemKeyCondition = dynamoDbBudgetExpenseFactory.monthlyItemKeyConditionFor(pk,star, end);
                    return dynamoClient.query(
                            QueryRequest.builder()
                                    .tableName(tableName)
                                    .keyConditionExpression("pk =:pk")
                                    .filterExpression("transaction_date >= :star AND transaction_date <= :end")
                                    .expressionAttributeValues(itemKeyCondition)
                                    .build()
                    ).items().stream();
                })
                .map(dynamoDbBudgetExpenseFactory::fromDynamoDbToModel)
                .filter(budgetExpense -> haveToBeIncluded(searchTagsList, budgetExpense))
                .collect(Collectors.toList());
    }

    private static boolean haveToBeIncluded(List<String> searchTagsList, BudgetExpense budgetExpense) {
        return searchTagsList.isEmpty() || searchTagsList.contains(budgetExpense.tag());

    }

    @Override
    public BudgetExpense save(BudgetExpense budgetExpense) {
        BudgetExpense budgetExpenseToSave = new BudgetExpense(
                idFactory.budgetIdFrom(budgetExpense),
                budgetExpense.userName(),
                budgetExpense.date(),
                budgetExpense.amount(),
                budgetExpense.note(),
                budgetExpense.tag()
        );
        dynamoClient.putItem(
                PutItemRequest.builder()
                        .tableName(tableName)
                        .item(dynamoDbBudgetExpenseFactory.putItemPayloadFor(budgetExpenseToSave))
                        .build()
        );

        return budgetExpenseToSave;
    }

    @Override
    public void delete(BudgetExpenseId idBudgetExpense) {
        HashMap<String, AttributeValue> itemKeyCondition = dynamoDbBudgetExpenseFactory.keysFor(idBudgetExpense);
        dynamoClient.deleteItem(
                DeleteItemRequest.builder()
                        .tableName(tableName)
                        .key(itemKeyCondition)
                        .build()
        );
    }


}