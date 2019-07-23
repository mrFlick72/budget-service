package it.valeriovaudi.familybudget.spentbudgetservice.web.endpoint;

import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.user.UserName;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.DockerComposeContainer;

import javax.sql.DataSource;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import static it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpenseId.randomBudgetExpenseId;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = ExportImportBudgetRevenueExpenseByFileEndPointTest.Initializer.class)
public class ExportImportBudgetRevenueExpenseByFileEndPointTest {

    private static final String FILE_PATH = "budget-expense/importSample.csv";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    }

    @ClassRule
    public static DockerComposeContainer postgres = new DockerComposeContainer(new File("src/test/resources/docker-compose.yml"))
            .withExposedService("postgres_1", 5432);

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            String serviceHost = postgres.getServiceHost("postgres_1", 5432);
            Integer servicePort = postgres.getServicePort("postgres_1", 5432);
            TestPropertyValues.of(format("spring.datasource.url=jdbc:postgresql://%s:%s/budget_expense", serviceHost, servicePort)).applyTo(configurableApplicationContext);
            TestPropertyValues.of("spring.datasource.username=root").applyTo(configurableApplicationContext);
            TestPropertyValues.of("spring.datasource.password=root").applyTo(configurableApplicationContext);
        }
    }

    @Test
    @DirtiesContext
    public void loadACsvFile() throws Exception {
        List<BudgetExpense> expected = asList(
                new BudgetExpense(randomBudgetExpenseId(), new UserName("USER"), Date.dateFor("01/02/2018"), Money.moneyFor("12.50"), "...\ntest", "super-market"),
                new BudgetExpense(randomBudgetExpenseId(), new UserName("USER"), Date.dateFor("01/02/2018"), Money.moneyFor("12.50"), "...\ntest\ntest", "super-market"),
                new BudgetExpense(randomBudgetExpenseId(), new UserName("USER"), Date.dateFor("01/02/2018"), Money.moneyFor("12.50"), "...\ntest\ntest\ntest", "super-market"));

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FILE_PATH)) {
            mockMvc.perform(multipart("/budget-expense")
                    .file("file", inputStream.readAllBytes())
                    .accept("application/csv"))
                    .andExpect(status().isCreated());
        }

        assertThat(jdbcTemplate.query("SELECT * FROM BUDGET_EXPENSE", budgetExpenseRowMapper), is(expected));
    }

    RowMapper<BudgetExpense> budgetExpenseRowMapper =
            (resultSet, i) -> new BudgetExpense(new BudgetExpenseId(resultSet.getString("ID")),
                    new UserName(resultSet.getString("USER_NAME")),
                    new Date(resultSet.getObject("DATE", LocalDate.class)),
                    new Money(resultSet.getBigDecimal("AMOUNT")),
                    resultSet.getString("NOTE"),
                    resultSet.getString("TAG"));
}
