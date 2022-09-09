package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetRevenue;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetRevenueRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;

import java.util.List;
import java.util.Map;

import static it.valeriovaudi.onlyoneportal.budgetservice.BudgetFixture.saltGenerator;
import static it.valeriovaudi.onlyoneportal.budgetservice.UserTestFixture.A_USER_NAME;
import static it.valeriovaudi.onlyoneportal.budgetservice.support.DatabaseUtils.*;
import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DynamoDbBudgetRevenueRepositoryIT {

    private static final Date DATE = Date.dateFor("12/02/2000");
    public static final DynamoDbClient DYNAMO_DB_CLIENT = dynamoClient();

    @Mock
    private UserRepository userRepository;

    private BudgetRevenueRepository budgetRevenueRepository;

    private final BudgetDynamoDbIdFactory idFactory = new BudgetDynamoDbIdFactory(saltGenerator);


    @BeforeEach
    public void setUp() {
        budgetRevenueRepository = new DynamoDbBudgetRevenueRepository(BUDGET_REVENUE_TABLE_NAME,
                DYNAMO_DB_CLIENT,
                idFactory,
                userRepository,
                new DynamoDbAttributeValueFactory());

        given(userRepository.currentLoggedUserName())
                .willReturn(A_USER_NAME);

        resetDatabase();
    }

    private void databaseInit() {
        loadBudgetRevenue().forEach(budgetRevenueRepository::save);
    }

    @Test
    void saveAnewBudgetRevenue() {
        BudgetRevenue budgetRevenue = new BudgetRevenue(null, "USER", DATE, Money.ONE, "A_NOTE");

        budgetRevenueRepository.save(budgetRevenue);

        List<BudgetRevenue> expectedRange = budgetRevenueRepository.findByDateRange("USER", DATE, DATE);

        Assertions.assertFalse(expectedRange.isEmpty());
    }

    @Test
    public void findByDateRange() {
        databaseInit();

        List<BudgetRevenue> actualRange = budgetRevenueRepository.findByDateRange("USER", Date.dateFor("01/01/2018"), Date.dateFor("05/05/2018"));
        List<BudgetRevenue> expectedRange =
                asList(new BudgetRevenue("MjAxOF9VU0VS-Nl8xX0FfU0FMVA==", "USER", Date.dateFor("06/01/2018"), Money.moneyFor("17.50"), "Lanch"),
                        new BudgetRevenue("MjAxOF9VU0VS-MTJfMl9BX1NBTFQ=", "USER", Date.dateFor("12/02/2018"), Money.moneyFor("10.50"), "Super Market"),
                        new BudgetRevenue("MjAxOF9VU0VS-MTNfMl9BX1NBTFQ=", "USER", Date.dateFor("13/02/2018"), Money.moneyFor("17.50"), "Dinner"),
                        new BudgetRevenue("MjAxOF9VU0VS-MjJfMl9BX1NBTFQ=", "USER", Date.dateFor("22/02/2018"), Money.moneyFor("17.50"), "Super Market"));

        Assertions.assertTrue(actualRange.containsAll(expectedRange));
    }


    @Test
    public void updateBudgetRevenue() {
        BudgetRevenue budgetRevenue = new BudgetRevenue(null, "USER", DATE, Money.ONE, "A_NOTE");
        BudgetRevenue revenue = budgetRevenueRepository.save(budgetRevenue);


        budgetRevenueRepository.update(new BudgetRevenue(
                revenue.getId(),
                revenue.getUserName(),
                revenue.getRegistrationDate(),
                revenue.getAmount(),
                "A NEW NOTE"
        ));

        List<BudgetRevenue> user = budgetRevenueRepository.findByDateRange("USER", DATE, DATE);

        Assertions.assertEquals("A NEW NOTE", user.get(0).getNote());
    }

    @Test
    public void deleteBudgetExpense() {
        BudgetRevenue budgetRevenue = new BudgetRevenue(null, "USER", DATE, Money.ONE, "A_NOTE");

        BudgetRevenue revenue = budgetRevenueRepository.save(budgetRevenue);

        List<BudgetRevenue> expectedRange = budgetRevenueRepository.findByDateRange("USER", DATE, DATE);

        Assertions.assertFalse(expectedRange.isEmpty());

        budgetRevenueRepository.delete(revenue.getId());
        expectedRange = budgetRevenueRepository.findByDateRange("USER", DATE, DATE);

        Assertions.assertTrue(expectedRange.isEmpty());
    }

}