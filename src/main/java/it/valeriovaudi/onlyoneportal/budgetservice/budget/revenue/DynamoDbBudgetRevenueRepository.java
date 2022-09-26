package it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue;

import it.valeriovaudi.onlyoneportal.budgetservice.infrastructure.dynamodb.BudgetDynamoDbIdFactory;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;

import java.util.*;
import java.util.stream.Collectors;

public class DynamoDbBudgetRevenueRepository implements BudgetRevenueRepository {

    private final String tableName;
    private final DynamoDbClient dynamoClient;
    private final BudgetDynamoDbIdFactory<BudgetRevenueId, BudgetRevenue> idFactory;
    private final DynamoDbBudgeRevenueFactory dynamoDbBudgeRevenueFactory;

    public DynamoDbBudgetRevenueRepository(String tableName,
                                           DynamoDbClient dynamoClient,
                                           BudgetDynamoDbIdFactory idFactory,
                                           DynamoDbBudgeRevenueFactory dynamoDbBudgeRevenueFactory) {
        this.tableName = tableName;
        this.dynamoClient = dynamoClient;
        this.idFactory = idFactory;
        this.dynamoDbBudgeRevenueFactory = dynamoDbBudgeRevenueFactory;
    }

    @Override
    public List<BudgetRevenue> findByDateRange(String user, Date star, Date end) {
        Set<String> pks = dynamoDbBudgeRevenueFactory.primaryKeysFor(star, end);
        return pks.stream()
                .flatMap(pk -> {
                    Map<String, AttributeValue> itemKeyCondition = dynamoDbBudgeRevenueFactory.yearlyItemKeyConditionFor(pk, star, end);
                    return dynamoClient.query(
                            QueryRequest.builder()
                                    .tableName(tableName)
                                    .keyConditionExpression("pk =:pk")
                                    .filterExpression("transaction_date >= :star AND transaction_date <= :end")
                                    .expressionAttributeValues(itemKeyCondition)
                                    .build()
                    ).items().stream();
                })
                .map(dynamoDbBudgeRevenueFactory::fromDynamoDbToModel)
                .sorted(Comparator.comparing(o -> o.registrationDate().getLocalDate()))
                .collect(Collectors.toList());
    }


    @Override
    public BudgetRevenue save(BudgetRevenue budgetRevenue) {
        BudgetRevenue budgetRevenueToSave = new BudgetRevenue(
                idFactory.budgetIdFrom(budgetRevenue),
                budgetRevenue.userName(),
                budgetRevenue.registrationDate(),
                budgetRevenue.amount(),
                budgetRevenue.note()
        );

        dynamoClient.putItem(
                PutItemRequest.builder()
                        .tableName(tableName)
                        .item(dynamoDbBudgeRevenueFactory.putItemPayloadFor(budgetRevenueToSave))
                        .build()
        );

        return budgetRevenueToSave;
    }



    @Override
    public void update(BudgetRevenue budgetRevenue) {
        save(budgetRevenue);
    }

    @Override
    public void delete(BudgetRevenueId idBudgetRevenue) {
        HashMap<String, AttributeValue> itemKeyCondition = dynamoDbBudgeRevenueFactory.keysFor(idBudgetRevenue);
        dynamoClient.deleteItem(
                DeleteItemRequest.builder()
                        .tableName(tableName)
                        .key(itemKeyCondition)
                        .build()
        );
    }


}
