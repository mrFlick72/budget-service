package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.SearchTagRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;

import java.net.URI;

import static it.valeriovaudi.onlyoneportal.budgetservice.UserTestFixture.A_USER_NAME;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DynamoDBSearchTagRepositoryIT extends AbstractSearchTagRepositoryIT {


    private SearchTagRepository budgetExpenseRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        DynamoDbClient client = DynamoDbClient.builder()
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create("ACCESS_KEY_ID", "SECRET_ACCESS_KEY"))
                ).region(Region.US_EAST_1)
                .endpointOverride(URI.create("http://localhost:8000"))
                .build();
        budgetExpenseRepository = new DynamoDBSearchTagRepository("BUDGET_SERVICE_SEARCH_TAGS_STAGING", userRepository, client);

        given(userRepository.currentLoggedUserName())
                .willReturn(A_USER_NAME);

        System.out.println("*********************************************");
        System.out.println("*********************************************" +
                client.scan(ScanRequest.builder().tableName("BUDGET_SERVICE_SEARCH_TAGS_STAGING").build())
                        .items()
        );
    }

/*
    @Test
    public void findAll() {
        super.findAll(budgetExpenseRepository);
    }

    @Test
    public void findSearchTagBy() {
        super.findSearchTagBy(budgetExpenseRepository);
    }

    @Test
    public void delete() {
        super.delete(budgetExpenseRepository);
    }
*/

    @Test
    public void save() {
        super.save(budgetExpenseRepository);
    }
}