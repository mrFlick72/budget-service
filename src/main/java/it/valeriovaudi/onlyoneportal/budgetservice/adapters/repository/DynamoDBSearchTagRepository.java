package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.SearchTag;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.SearchTagRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.UserRepository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                                .keyConditionExpression("user_name =:user_name AND search_tag_key =:search_tag_key")
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
        QueryResponse query = client.query(
                QueryRequest.builder()
                        .tableName(tableName)
                        .keyConditionExpression("user_name =:user_name")
                        .expressionAttributeValues(findAllItemKeyCondition())
                        .build()
        );
        return query.items().stream()
                .map(this::fromDynamoDbToModel)
                .collect(Collectors.toList());
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
        HashMap<String, AttributeValue> itemKeyCondition = itemKeysFor(key);
        client.deleteItem(
                DeleteItemRequest.builder()
                        .tableName(tableName)
                        .key(itemKeyCondition)
                        .build()
        );
    }

    private HashMap<String, AttributeValue> itemKeyConditionFor(String key) {
        HashMap<String, AttributeValue> itemKeyCondition = new HashMap<>();
        itemKeyCondition.put(":user_name", dynamoDbStringAttributeFor(userRepository.currentLoggedUserName().getContent()));
        itemKeyCondition.put(":search_tag_key", dynamoDbStringAttributeFor(key));
        return itemKeyCondition;
    }
    private HashMap<String, AttributeValue> itemKeysFor(String key) {
        HashMap<String, AttributeValue> itemKeyCondition = new HashMap<>();
        itemKeyCondition.put("user_name", dynamoDbStringAttributeFor(userRepository.currentLoggedUserName().getContent()));
        itemKeyCondition.put("search_tag_key", dynamoDbStringAttributeFor(key));
        return itemKeyCondition;
    }

    private HashMap<String, AttributeValue> findAllItemKeyCondition() {
        HashMap<String, AttributeValue> itemKeyCondition = new HashMap<>();
        itemKeyCondition.put(":user_name", dynamoDbStringAttributeFor(userRepository.currentLoggedUserName().getContent()));
        return itemKeyCondition;
    }

    private SearchTag fromDynamoDbToModel(Map<String, AttributeValue> attributes) {
        return new SearchTag(attributes.get("search_tag_key").s(), attributes.get("search_tag_value").s());
    }

    private HashMap<String, AttributeValue> putItemPayloadFor(SearchTag searchTag) {
        HashMap<String, AttributeValue> attributes = new HashMap<>();
        attributes.put("user_name", dynamoDbStringAttributeFor(userRepository.currentLoggedUserName().getContent()));
        attributes.put("search_tag_key", dynamoDbStringAttributeFor(searchTag.getKey()));
        attributes.put("search_tag_value", dynamoDbStringAttributeFor(searchTag.getValue()));
        return attributes;
    }

    private AttributeValue dynamoDbStringAttributeFor(String content) {
        return AttributeValue.builder().s(content).build();
    }
}