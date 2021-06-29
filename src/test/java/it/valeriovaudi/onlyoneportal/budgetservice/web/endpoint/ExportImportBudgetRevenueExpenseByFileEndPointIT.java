package it.valeriovaudi.onlyoneportal.budgetservice.web.endpoint;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.user.UserName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import static it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId.randomBudgetExpenseId;
import static java.util.Arrays.asList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ExportImportBudgetRevenueExpenseByFileEndPointIT {

    private static final String FILE_PATH = "budget-expense/importSample.csv";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
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

        Assertions.assertEquals(jdbcTemplate.query("SELECT * FROM BUDGET_EXPENSE", budgetExpenseRowMapper), expected);
    }

    RowMapper<BudgetExpense> budgetExpenseRowMapper =
            (resultSet, i) -> new BudgetExpense(new BudgetExpenseId(resultSet.getString("ID")),
                    new UserName(resultSet.getString("USER_NAME")),
                    new Date(resultSet.getObject("DATE", LocalDate.class)),
                    new Money(resultSet.getBigDecimal("AMOUNT")),
                    resultSet.getString("NOTE"),
                    resultSet.getString("TAG"));
}
