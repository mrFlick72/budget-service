package it.valeriovaudi.onlyoneportal.budgetservice.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration(proxyBeanMethods = false)
public class AwsConfig {

    @Bean("awsCredentialsProvider")
    public AwsCredentialsProvider iamUserAwsCredentialsProvider() {
        return EnvironmentVariableCredentialsProvider.create();
    }

    @Bean
    public DynamoDbClient dynamoDbClient(AwsCredentialsProvider awsCredentialsProvider) {
        return DynamoDbClient.builder()
                .credentialsProvider(awsCredentialsProvider)
                .build();
    }
}
