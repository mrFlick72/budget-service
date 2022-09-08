package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetRevenue;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetRevenueRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import static it.valeriovaudi.onlyoneportal.budgetservice.BudgetFixture.saltGenerator;
import static it.valeriovaudi.onlyoneportal.budgetservice.UserTestFixture.A_USER_NAME;
import static it.valeriovaudi.onlyoneportal.budgetservice.support.DatabaseUtils.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DynamoDbBudgetRevenueRepositoryIT {

    private static final Date DATE = Date.dateFor("12/02/2018");
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
        databaseInit();
    }

    private void databaseInit() {
        loadBudgetRevenue().forEach(budgetRevenueRepository::save);
    }

/*    @Test
    public void saveAnewBudgetRevenue() {
        BudgetRevenue budgetRevenue = new BudgetRevenue(null, "A_USER", DATE, Money.ONE, "A_NOTE");

        budgetRevenueRepository.save(budgetRevenue);

        List<Map<String, AttributeValue>> items = DYNAMO_DB_CLIENT.query(
                QueryRequest.builder()
                        .tableName(BUDGET_REVENUE_TABLE_NAME)

                        .build()
        ).items();

        Assertions.assertFalse(items.isEmpty());
    }*/

    /*@Test
    public void findByDateRange() {
        List<BudgetRevenue> actualRange = budgetRevenueRepository.findByDateRange("USER", Date.dateFor("01/01/2018"), Date.dateFor("05/05/2018"));
        List<BudgetRevenue> expectedRange =
                asList(new BudgetRevenue("5", "USER", Date.dateFor("06/01/2018"), Money.moneyFor("17.50"), "Lanch"),
                        new BudgetRevenue("1", "USER", Date.dateFor("12/02/2018"), Money.moneyFor("10.50"), "Super Market"),
                        new BudgetRevenue("3", "USER", Date.dateFor("13/02/2018"), Money.moneyFor("17.50"), "Dinner"),
                        new BudgetRevenue("2", "USER", Date.dateFor("22/02/2018"), Money.moneyFor("17.50"), "Super Market"));

        Assertions.assertEquals(actualRange, expectedRange);
    }


    @Test
    public void updateBudgetRevenue() {
        BudgetRevenue expected = new BudgetRevenue("1", "USER", Date.dateFor("12/02/2018"), Money.moneyFor("110.50"), "Super Market");

        budgetRevenueRepository.update(expected);

        BudgetRevenue actual = getBudgetRevenue("1");

        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void deleteBudgetExpense() {
        budgetRevenueRepository.delete("1");

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> getBudgetRevenue("1"));

    }*/

    private BudgetRevenue getBudgetRevenue(String id) {
        return null;
    }
}