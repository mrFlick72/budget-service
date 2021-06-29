package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.attachment.AttachmentFileName;
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
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;

@JdbcTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JdbcBudgetExpenseRepositoryIT {

    private static final String DATE_STRING = "12/02/2018";
    private static final Date DATE = Date.dateFor("12/02/2018");

    @Autowired
    JdbcTemplate jdbcTemplate;

    private JdbcBudgetExpenseRepository jdbcBudgetExpenseRepository;

    @BeforeEach
    public void setUp() {
        jdbcBudgetExpenseRepository = new JdbcBudgetExpenseRepository(jdbcTemplate);
    }


    @Test
    public void saveAnewBudgetExpenseWithoutAttachments() {
        BudgetExpenseId id = new BudgetExpenseId(UUID.randomUUID().toString());
        BudgetExpense expected = new BudgetExpense(id, new UserName("USER"), DATE, Money.moneyFor("10.50"), "NOTE", "TAG");

        jdbcBudgetExpenseRepository.save(expected);

        BudgetExpense actual = getBudgetExpense("USER", DATE_STRING, "10.50", "NOTE", "TAG");

        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void saveAnewBudgetExpenseWithAttachments() {
        BudgetExpenseId id = new BudgetExpenseId(UUID.randomUUID().toString());
        BudgetExpense expected = new BudgetExpense(id, new UserName("USER"), DATE, Money.moneyFor("10.50"), "NOTE", "TAG", asList(new AttachmentFileName("A_FILE")));
        jdbcBudgetExpenseRepository.save(expected);

        BudgetExpense actual = getBudgetExpenseWithAttachments(id, DATE_STRING, "10.50", "NOTE", "TAG");

        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void saveOrUpdateABudgetExpenseWithAttachments() {
        BudgetExpenseId id = new BudgetExpenseId(UUID.randomUUID().toString());

        BudgetExpense budgetExpense = new BudgetExpense(id, new UserName("USER"), DATE, Money.moneyFor("10.50"), "NOTE", "TAG",
                asList(new AttachmentFileName("A_FILE1"),
                        new AttachmentFileName("ANOTHER_FILE"),
                        new AttachmentFileName("ANOTHER_FILE_AGAIN")));
        jdbcBudgetExpenseRepository.save(budgetExpense);

        BudgetExpense expected = new BudgetExpense(id, new UserName("USER"), DATE, Money.moneyFor("10.50"), "NOTE", "TAG",
                asList(new AttachmentFileName("A_FILE1"),
                        new AttachmentFileName("A_FILE_AGAIN"),
                        new AttachmentFileName("ANOTHER_FILE_NAME")));

        jdbcBudgetExpenseRepository.save(expected);

        BudgetExpense actual = getBudgetExpenseWithAttachments(id, DATE_STRING, "10.50", "NOTE", "TAG");

        Assertions.assertEquals(actual, expected);
    }

    @Test
    @Sql("classpath:budget-expense/find-by-date-range-data-set.sql")
    public void deleteBudgetExpense() {
        jdbcBudgetExpenseRepository.delete(new BudgetExpenseId("1"));
        Assertions.assertThrows(EmptyResultDataAccessException.class,
                () -> getBudgetExpense("USER", DATE_STRING, "10.50", "Super Market", "super-market"));
    }

    @Test
    public void findABudgetExpenseByReference() {
        BudgetExpenseId id = new BudgetExpenseId(UUID.randomUUID().toString());
        BudgetExpense expected = new BudgetExpense(id, new UserName("USER"), DATE, Money.moneyFor("10.50"), "NOTE", "TAG", asList(new AttachmentFileName("A_FILE")));
        jdbcBudgetExpenseRepository.save(expected);

        Assertions.assertEquals(jdbcBudgetExpenseRepository.findFor(id).get(), expected);
    }

    @Test
    @Sql("classpath:budget-expense/find-by-date-range-data-set.sql")
    public void findByDateRange() {
        List<BudgetExpense> actualRange = jdbcBudgetExpenseRepository.findByDateRange(new UserName("USER"), Date.dateFor("01/01/2018"), Date.dateFor("05/05/2018"));
        List<BudgetExpense> expectedRange =
                asList(new BudgetExpense(new BudgetExpenseId("1"), new UserName("USER"), Date.dateFor("12/02/2018"), Money.moneyFor("10.50"), "Super Market", "super-market"),
                        new BudgetExpense(new BudgetExpenseId("2"), new UserName("USER"), Date.dateFor("22/02/2018"), Money.moneyFor("17.50"), "Super Market", "super-market"),
                        new BudgetExpense(new BudgetExpenseId("3"), new UserName("USER"), Date.dateFor("13/02/2018"), Money.moneyFor("17.50"), "Dinner", "dinner"),
                        new BudgetExpense(new BudgetExpenseId("4"), new UserName("USER"), Date.dateFor("05/05/2018"), Money.moneyFor("17.50"), "Lanch", "lanch"),
                        new BudgetExpense(new BudgetExpenseId("5"), new UserName("USER"), Date.dateFor("06/01/2018"), Money.moneyFor("17.50"), "Lanch", "lanch"));

        Assertions.assertEquals(actualRange, expectedRange);
    }

    @Test
    @Sql("classpath:budget-expense/find-by-date-range-data-set.sql")
    public void findByDateRangeAndSearchTags() {
        List<BudgetExpense> actualRange = jdbcBudgetExpenseRepository.findByDateRange(new UserName("USER"), Date.dateFor("01/01/2018"), Date.dateFor("05/05/2018"), "super-market", "dinner");
        List<BudgetExpense> expectedRange =
                asList(new BudgetExpense(new BudgetExpenseId("1"), new UserName("USER"), Date.dateFor("12/02/2018"), Money.moneyFor("10.50"), "Super Market", "super-market"),
                        new BudgetExpense(new BudgetExpenseId("2"), new UserName("USER"), Date.dateFor("22/02/2018"), Money.moneyFor("17.50"), "Super Market", "super-market"),
                        new BudgetExpense(new BudgetExpenseId("3"), new UserName("USER"), Date.dateFor("13/02/2018"), Money.moneyFor("17.50"), "Dinner", "dinner"));

        Assertions.assertEquals(actualRange, expectedRange);
    }

    @Test
    @Sql("classpath:budget-expense/find-by-date-range-data-set.sql")
    public void deleteBudgetExpenseWithAttachment() {
        jdbcBudgetExpenseRepository.delete(new BudgetExpenseId("13"));

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> getBudgetExpenseFor(new BudgetExpenseId("13")));
    }


    private BudgetExpense getBudgetExpense(String userName, String date, String amount, String note, String tag) {
        return jdbcTemplate.queryForObject("SELECT * FROM BUDGET_EXPENSE WHERE USER_NAME = ? AND DATE=? AND AMOUNT=? AND NOTE=? AND TAG=?",
                jdbcBudgetExpenseRepository.budgetExpenseRowMapper, userName, Date.dateFor(date).getLocalDate(),
                Money.moneyFor(amount).getAmount(), note, tag);
    }

    private BudgetExpense getBudgetExpenseWithAttachments(BudgetExpenseId id, String date, String amount, String note, String tag) {
        List<AttachmentFileName> attachmentFileNames = jdbcTemplate.query("SELECT * FROM BUDGET_EXPENSE_ATTACHMENTS WHERE BUDGET_EXPENSE_ID=?",
                new Object[]{id.getContent()}, jdbcBudgetExpenseRepository.budgetExpenseAttachmentRowMapper);

        BudgetExpense budgetExpense = jdbcTemplate.queryForObject("SELECT * FROM BUDGET_EXPENSE WHERE DATE=? AND AMOUNT=? AND NOTE=? AND TAG=?",
                jdbcBudgetExpenseRepository.budgetExpenseRowMapper, Date.dateFor(date).getLocalDate(),
                Money.moneyFor(amount).getAmount(), note, tag);
        return new BudgetExpense(budgetExpense.getId(), new UserName("USER"), budgetExpense.getDate(), budgetExpense.getAmount(), budgetExpense.getNote(), budgetExpense.getTag(), attachmentFileNames);
    }

    private BudgetExpense getBudgetExpenseFor(BudgetExpenseId id) {
        List<AttachmentFileName> attachmentFileNames = jdbcTemplate.query("SELECT * FROM BUDGET_EXPENSE_ATTACHMENTS WHERE BUDGET_EXPENSE_ID=?",
                new Object[]{id.getContent()}, jdbcBudgetExpenseRepository.budgetExpenseAttachmentRowMapper);

        BudgetExpense budgetExpense = jdbcTemplate.queryForObject("SELECT * FROM BUDGET_EXPENSE WHERE ID=?",
                jdbcBudgetExpenseRepository.budgetExpenseRowMapper, id.getContent());
        return new BudgetExpense(budgetExpense.getId(), new UserName("USER"), budgetExpense.getDate(), budgetExpense.getAmount(), budgetExpense.getNote(), budgetExpense.getTag(), attachmentFileNames);
    }

    private List<AttachmentFileName> getBudgetExpenseAttachmentFor(BudgetExpenseId id) {
        return jdbcTemplate.query("SELECT * FROM BUDGET_EXPENSE_ATTACHMENTS WHERE BUDGET_EXPENSE_ID=?",
                new Object[]{id.getContent()}, jdbcBudgetExpenseRepository.budgetExpenseAttachmentRowMapper);

    }
}