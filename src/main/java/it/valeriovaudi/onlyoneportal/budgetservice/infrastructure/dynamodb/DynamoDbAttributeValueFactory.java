package it.valeriovaudi.onlyoneportal.budgetservice.infrastructure.dynamodb;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class DynamoDbAttributeValueFactory {
    public AttributeValue stringAttributeFor(String content) {
        return AttributeValue.builder().s(content).build();
    }
}
