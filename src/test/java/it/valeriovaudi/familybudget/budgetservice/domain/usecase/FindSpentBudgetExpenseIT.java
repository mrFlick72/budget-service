package it.valeriovaudi.familybudget.budgetservice.domain.usecase;

import it.valeriovaudi.familybudget.budgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.budgetservice.domain.model.SearchTag;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.SpentBudget;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Month;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Year;
import it.valeriovaudi.familybudget.budgetservice.domain.model.user.UserName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

// fixme
@SpringBootTest
@RunWith(SpringRunner.class)
public class FindSpentBudgetExpenseIT {

    @Autowired
    private FindSpentBudget findSpentBudget;

    @Test
    @SqlGroup(value = {
            @Sql("classpath:schema.sql"),
            @Sql("classpath:budget-expense/find-by-date-range-data-set.sql"),
            @Sql("classpath:search_tag/findAll.sql")
    })
    public void budgetExpenseInAMonth() {
        SpentBudget actual = findSpentBudget.findBy(Month.FEBRUARY, Year.of(2018), null);
        SpentBudget expected = new SpentBudget(asList(
                new BudgetExpense(new BudgetExpenseId("1"), new UserName("USER"), Date.dateFor("12/02/2018"), Money.moneyFor("10.50"), "Super Market", "super-market"),
                new BudgetExpense(new BudgetExpenseId("2"), new UserName("USER"), Date.dateFor("13/02/2018"), Money.moneyFor("17.50"), "Dinner", "dinner"),
                new BudgetExpense(new BudgetExpenseId("3"), new UserName("USER"), Date.dateFor("22/02/2018"), Money.moneyFor("17.50"), "Super Market", "super-market")),
                asList(new SearchTag("super-market", "Spesa"), new SearchTag("dinner", "Cena")));

        assertThat(actual, is(expected));
    }

}