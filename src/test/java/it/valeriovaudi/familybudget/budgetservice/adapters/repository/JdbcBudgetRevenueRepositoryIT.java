package it.valeriovaudi.familybudget.budgetservice.adapters.repository;

import it.valeriovaudi.familybudget.budgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetRevenue;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Date;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;


@JdbcTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JdbcBudgetRevenueRepositoryIT {

    private static final Date DATE = Date.dateFor("12/02/2018");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcBudgetRevenueRepository budgetRevenueRepository;


    @Before
    public void setUp() {
        budgetRevenueRepository = new JdbcBudgetRevenueRepository(jdbcTemplate);
    }

    @Test
    public void saveAnewBudgetRevenue() {
        String budgetRevenueId = "1234";
        BudgetRevenue budgetRevenue = new BudgetRevenue(budgetRevenueId, "A_USER", DATE, Money.ONE, "A_NOTE");

        budgetRevenueRepository.save(budgetRevenue);

        List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT * FROM BUDGET_REVENUE WHERE ID='1234'");

        Assert.assertFalse(maps.isEmpty());
    }

    @Test
    @Sql("classpath:budget_revenue/find-by-date-range-data-set.sql")
    public void findByDateRange() {
        List<BudgetRevenue> actualRange = budgetRevenueRepository.findByDateRange("USER", Date.dateFor("01/01/2018"), Date.dateFor("05/05/2018"));
        List<BudgetRevenue> expectedRange =
                asList(new BudgetRevenue("5", "USER", Date.dateFor("06/01/2018"), Money.moneyFor("17.50"), "Lanch"),
                        new BudgetRevenue("1", "USER", Date.dateFor("12/02/2018"), Money.moneyFor("10.50"), "Super Market"),
                        new BudgetRevenue("3", "USER", Date.dateFor("13/02/2018"), Money.moneyFor("17.50"), "Dinner"),
                        new BudgetRevenue("2", "USER", Date.dateFor("22/02/2018"), Money.moneyFor("17.50"), "Super Market"));

        assertThat(actualRange, is(expectedRange));
    }


    @Test
    @Sql("classpath:budget_revenue/find-by-date-range-data-set.sql")
    public void updateBudgetRevenue() {
        BudgetRevenue expected = new BudgetRevenue("1", "USER", Date.dateFor("12/02/2018"), Money.moneyFor("110.50"), "Super Market");

        budgetRevenueRepository.update(expected);

        BudgetRevenue actual = getBudgetRevenue("1");

        assertThat(actual, is(expected));
    }

    @Sql("classpath:budget_revenue/find-by-date-range-data-set.sql")
    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteBudgetExpense() {
        budgetRevenueRepository.delete("1");

        BudgetRevenue actual = getBudgetRevenue("1");

        assertThat(actual, is(nullValue()));
    }

    private BudgetRevenue getBudgetRevenue(String id) {
        return jdbcTemplate.queryForObject("SELECT * FROM BUDGET_REVENUE WHERE ID=?", new Object[]{id}, budgetRevenueRepository.budgetRevenueRowMapper);
    }
}