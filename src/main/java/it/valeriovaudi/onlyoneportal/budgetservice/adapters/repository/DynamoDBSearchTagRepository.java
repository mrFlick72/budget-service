package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.SearchTag;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.SearchTagRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.UserRepository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamoDBSearchTagRepository implements SearchTagRepository {
    private final String tableName;
    private final UserRepository userRepository;
    private final DynamoDbClient client;

    public DynamoDBSearchTagRepository(String tableName, UserRepository userRepository, DynamoDbClient client) {
        this.tableName = tableName;
        this.userRepository = userRepository;
        this.client = client;
    }

    @Override
    public SearchTag findSearchTagBy(String key) {
        HashMap<String, AttributeValue> itemKeyCondition = itemKeyConditionFor(key);
        QueryResponse query = client
                .query(
                        QueryRequest.builder()
                                .tableName(this.tableName)
                                .keyConditionExpression("USER_NAME =:SEARCH_TAG_USER_NAME AND SEARCH_TAG_KEY =:SEARCH_TAG_KEY")
                                .expressionAttributeValues(itemKeyCondition)
                                .build()
                );

        return query.items().stream()
                .map(this::fromDynamoDbToModel)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<SearchTag> findAllSearchTag() {
        return null;
    }

    @Override
    public void save(SearchTag searchTag) {
        client.putItem(
                PutItemRequest.builder()
                        .tableName(this.tableName)
                        .item(putItemPayloadFor(searchTag))
                        .build()
        );
    }

    @Override
    public void delete(String key) {

    }

    private HashMap<String, AttributeValue> itemKeyConditionFor(String key) {
        HashMap<String, AttributeValue> itemKeyCondition = new HashMap<>();
        itemKeyCondition.put(":SEARCH_TAG_USER_NAME", dynamoDbStringAttributeFor(userRepository.currentLoggedUserName().getContent()));
        itemKeyCondition.put(":SEARCH_TAG_KEY", dynamoDbStringAttributeFor(key));
        return itemKeyCondition;
    }

    private SearchTag fromDynamoDbToModel(Map<String, AttributeValue> attributes) {
        return new SearchTag(attributes.get("SEARCH_TAG_KEY").s(), attributes.get("SEARCH_TAG_VALUE").s());
    }

    private HashMap<String, AttributeValue> putItemPayloadFor(SearchTag searchTag) {
        HashMap<String, AttributeValue> attributes = new HashMap<>();
        attributes.put("USER_NAME", dynamoDbStringAttributeFor(userRepository.currentLoggedUserName().getContent()));
        attributes.put("SEARCH_TAG_KEY", dynamoDbStringAttributeFor(searchTag.getKey()));
        attributes.put("SEARCH_TAG_VALUE", dynamoDbStringAttributeFor(searchTag.getValue()));
        return attributes;
    }

    private AttributeValue dynamoDbStringAttributeFor(String content) {
        return AttributeValue.builder().s(content).build();
    }
}