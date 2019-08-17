package it.valeriovaudi.familybudget.budgetservice.domain.usecase;

import it.valeriovaudi.familybudget.budgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetRevenue;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Year;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;


@SpringBootTest
@RunWith(SpringRunner.class)
public class FindBudgetRevenueIT {

    @Autowired
    private FindBudgetRevenue findBudgetRevenue;

    @Test
    @SqlGroup(value = {
            @Sql("classpath:schema.sql"),
            @Sql("classpath:budget_revenue/find-year-data-set.sql")
    })
    @WithMockUser("USER")
    public void findYearlyBudgetRevenue() {
        Year of = Year.of(2018);

        List<BudgetRevenue> yearlyBudgetRevenueList = findBudgetRevenue.findBy(of);
        List<BudgetRevenue> expectedBudgetRevenueList =
                asList(new BudgetRevenue("ID_1", "USER", Date.dateFor("01/01/2018"), Money.ONE, ""),
                        new BudgetRevenue("ID_2", "USER", Date.dateFor("01/02/2018"), Money.ONE, ""),
                        new BudgetRevenue("ID_3", "USER", Date.dateFor("01/03/2018"), Money.ONE, ""),
                        new BudgetRevenue("ID_5", "USER", Date.dateFor("01/05/2018"), Money.ONE, ""),
                        new BudgetRevenue("ID_6", "USER", Date.dateFor("01/06/2018"), Money.ONE, ""),
                        new BudgetRevenue("ID_9", "USER", Date.dateFor("01/09/2018"), Money.ONE, ""),
                        new BudgetRevenue("ID_10", "USER", Date.dateFor("01/10/2018"), Money.ONE, ""));

        System.out.println(yearlyBudgetRevenueList);
        System.out.println(expectedBudgetRevenueList);
        Assert.assertThat(yearlyBudgetRevenueList, is(expectedBudgetRevenueList));
    }
}