package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.user.UserName;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static it.valeriovaudi.onlyoneportal.budgetservice.support.DatabaseUtils.*;

class DynamoDbBudgetExpenseRepositoryTest {
    private static final String DATE_STRING = "12/02/2018";
    private static final Date DATE = Date.dateFor("12/02/2018");


    private BudgetExpenseRepository budgetExpenseRepository;

    @BeforeEach
    public void setUp() {
        budgetExpenseRepository = new DynamoDbBudgetExpenseRepository(BUDGET_EXPENSE_TABLE_NAME, dynamoClient());
        resetDatabase();
    }

    @Test
    public void saveAnewBudgetExpense() {
        BudgetExpenseId id = new BudgetExpenseId(UUID.randomUUID().toString());
        BudgetExpense expected = new BudgetExpense(id, new UserName("USER"), DATE, Money.moneyFor("10.50"), "NOTE", "TAG");

        budgetExpenseRepository.save(expected);

//        BudgetExpense actual = getBudgetExpense("USER", DATE_STRING, "10.50", "NOTE", "TAG");
//
//        Assertions.assertEquals(actual, expected);
    }
/*
        @Test
        @Sql("classpath:/budget-expense/find-by-date-range-data-set.sql")
        public void deleteBudgetExpense() {
            budgetExpenseRepository.delete(new BudgetExpenseId("1"));
            Assertions.assertThrows(EmptyResultDataAccessException.class,
                    () -> getBudgetExpense("USER", DATE_STRING, "10.50", "Super Market", "super-market"));
        }

        @Test
        public void findABudgetExpenseByReference() {
            BudgetExpenseId id = new BudgetExpenseId(UUID.randomUUID().toString());
            BudgetExpense expected = new BudgetExpense(id, new UserName("USER"), DATE, Money.moneyFor("10.50"), "NOTE", "TAG");
            budgetExpenseRepository.save(expected);

            Assertions.assertEquals(budgetExpenseRepository.findFor(id).get(), expected);
        }

        @Test
        @Sql("classpath:budget-expense/find-by-date-range-data-set.sql")
        public void findByDateRange() {
            List<BudgetExpense> actualRange = budgetExpenseRepository.findByDateRange(new UserName("USER"), Date.dateFor("01/01/2018"), Date.dateFor("05/05/2018"));
            List<BudgetExpense> expectedRange =
                    asList(
                            new BudgetExpense(new BudgetExpenseId("1"), new UserName("USER"), Date.dateFor("12/02/2018"), Money.moneyFor("10.50"), "Super Market", "super-market"),
                            new BudgetExpense(new BudgetExpenseId("2"), new UserName("USER"), Date.dateFor("22/02/2018"), Money.moneyFor("17.50"), "Super Market", "super-market"),
                            new BudgetExpense(new BudgetExpenseId("3"), new UserName("USER"), Date.dateFor("13/02/2018"), Money.moneyFor("17.50"), "Dinner", "dinner"),
                            new BudgetExpense(new BudgetExpenseId("4"), new UserName("USER"), Date.dateFor("05/05/2018"), Money.moneyFor("17.50"), "Lanch", "lanch"),
                            new BudgetExpense(new BudgetExpenseId("5"), new UserName("USER"), Date.dateFor("06/01/2018"), Money.moneyFor("17.50"), "Lanch", "lanch")
                    );

            Assertions.assertEquals(expectedRange.size(), actualRange.size());
        }

        @Test
        @Sql("classpath:/budget-expense/find-by-date-range-data-set.sql")
        public void findByDateRangeAndSearchTags() {
            List<BudgetExpense> actualRange = budgetExpenseRepository.findByDateRange(new UserName("USER"), Date.dateFor("01/01/2018"), Date.dateFor("05/05/2018"), "super-market", "dinner");
            List<BudgetExpense> expectedRange =
                    asList(new BudgetExpense(new BudgetExpenseId("1"), new UserName("USER"), Date.dateFor("12/02/2018"), Money.moneyFor("10.50"), "Super Market", "super-market"),
                            new BudgetExpense(new BudgetExpenseId("2"), new UserName("USER"), Date.dateFor("22/02/2018"), Money.moneyFor("17.50"), "Super Market", "super-market"),
                            new BudgetExpense(new BudgetExpenseId("3"), new UserName("USER"), Date.dateFor("13/02/2018"), Money.moneyFor("17.50"), "Dinner", "dinner"));

            Assertions.assertEquals(actualRange, expectedRange);
        }

        @Test
        @Sql("classpath:/budget-expense/find-by-date-range-data-set.sql")
        public void deleteBudgetExpenseWithAttachment() {
            budgetExpenseRepository.delete(new BudgetExpenseId("13"));

            Assertions.assertThrows(EmptyResultDataAccessException.class, () -> getBudgetExpenseFor(new BudgetExpenseId("13")));
        }


        private BudgetExpense getBudgetExpense(String userName, String date, String amount, String note, String tag) {
            return jdbcTemplate.queryForObject("SELECT * FROM BUDGET_EXPENSE WHERE USER_NAME = ? AND DATE=? AND AMOUNT=? AND NOTE=? AND TAG=?",
                    budgetExpenseRepository.budgetExpenseRowMapper, userName, Date.dateFor(date).getLocalDate(),
                    Money.moneyFor(amount).getAmount(), note, tag);
        }


        private BudgetExpense getBudgetExpenseFor(BudgetExpenseId id) {
            BudgetExpense budgetExpense = jdbcTemplate.queryForObject("SELECT * FROM BUDGET_EXPENSE WHERE ID=?",
                    budgetExpenseRepository.budgetExpenseRowMapper, id.getContent());
            return new BudgetExpense(budgetExpense.getId(), new UserName("USER"), budgetExpense.getDate(), budgetExpense.getAmount(), budgetExpense.getNote(), budgetExpense.getTag());
        }*/
}