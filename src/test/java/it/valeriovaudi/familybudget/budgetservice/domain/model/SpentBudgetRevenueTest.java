package it.valeriovaudi.familybudget.budgetservice.domain.model;

import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.DailyBudgetExpense;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.SpentBudget;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.budgetservice.domain.model.user.UserName;
import org.junit.Test;

import java.util.List;

import static it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpenseId.emptyBudgetExpenseId;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SpentBudgetRevenueTest {

    @Test
    public void getTotal() {
        SpentBudget spent25Budget =
                new SpentBudget(asList(new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("12/02/2018"), Money.moneyFor("12.50"), "super-market", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("12/02/2018"), Money.moneyFor("12.50"), "super-market", "super-market")), asList());
        SpentBudget spent30Budget =
                new SpentBudget(asList(new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("12/02/2018"), Money.moneyFor("12.50"), "super-market", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("12/02/2018"), Money.moneyFor("12.50"), "super-market", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("12/02/2018"), Money.moneyFor("5"), "super-market", "super-market")), asList());

        SpentBudget spent50Budget =
                new SpentBudget(asList(new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("12/02/2018"), Money.moneyFor("12.50"), "super-market", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("12/02/2018"), Money.moneyFor("12.50"), "super-market", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("12/02/2018"), Money.moneyFor("20.00"), "super-market", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("12/02/2018"), Money.moneyFor("5"), "super-market", "super-market")), asList());

        assertThat(Money.moneyFor("30.00"), is(spent30Budget.total()));
        assertThat(Money.moneyFor("25.00"), is(spent25Budget.total()));
        assertThat(Money.moneyFor("50.00"), is(spent50Budget.total()));
    }

    @Test
    public void dailyBudgetExpenseList() {
        SpentBudget spentBudget =
                new SpentBudget(asList(new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("12/02/2018"), Money.moneyFor("12.50"), "super-market", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("12/02/2018"), Money.moneyFor("12.50"), "super-market", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("15/02/2018"), Money.moneyFor("20.00"), "super-market", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("15/02/2018"), Money.moneyFor("5"), "super-market", "super-market")), asList());

        List<DailyBudgetExpense> expected = asList(
                new DailyBudgetExpense(asList(new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("12/02/2018"), Money.moneyFor("12.50"), "super-market", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("12/02/2018"), Money.moneyFor("12.50"), "super-market", "super-market")),
                        Date.dateFor("12/02/2018"), Money.moneyFor("25.00")),
                new DailyBudgetExpense(asList(new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("15/02/2018"), Money.moneyFor("20.00"), "super-market", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("15/02/2018"), Money.moneyFor("5"), "super-market", "super-market")),
                        Date.dateFor("15/02/2018"), Money.moneyFor("25.00")));

        assertThat(spentBudget.dailyBudgetExpenseList(), is(expected));
    }

}