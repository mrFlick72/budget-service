package it.valeriovaudi.familybudget.spentbudgetservice.domain.usecase;

import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.SearchTag;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.SpentBudget;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Month;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Year;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.user.UserName;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.BudgetExpenseRepository;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.SearchTagRepository;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.UserRepository;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpenseId.emptyBudgetExpenseId;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FindSpentBudgetExpenseTest {

    @Mock
    BudgetExpenseRepository budgetExpenseRepository;

    @Mock
    SearchTagRepository searchTagRepository;

    @Mock
    UserRepository userRepository;

    @Test
    public void budgetExpenseTotalBySearchTagsWithConstraints() {
        given(userRepository.currentLoggedUserName())
                .willReturn(new UserName("USER"));

        given(budgetExpenseRepository.findByDateRange(new UserName("USER"), Date.firstDateOfMonth(Month.FEBRUARY, Year.of(2018)),
                Date.lastDateOfMonth(Month.FEBRUARY, Year.of(2018)), "dinner", "super-market"))

                .willReturn(asList(new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("15/02/2018"), Money.moneyFor("10"), "dinner", "dinner"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("01/02/2018"), Money.moneyFor("12.50"), "super market", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("05/02/2018"), Money.moneyFor("12.50"), "super market", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("04/02/2018"), Money.moneyFor("20"), "dinner", "dinner"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("03/02/2018"), Money.moneyFor("12.50"), "super market", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("02/02/2018"), Money.moneyFor("12.50"), "super market", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("01/02/2018"), Money.moneyFor("15"), "dinner", "dinner")));

        given(searchTagRepository.findSearchTagBy("dinner")).willReturn(new SearchTag("dinner", "dinner"));
        given(searchTagRepository.findSearchTagBy("super-market")).willReturn(new SearchTag("super-market", "super-market"));

        SpentBudget actual = new FindSpentBudget(userRepository, budgetExpenseRepository, searchTagRepository)
                .findBy(Month.FEBRUARY, Year.of(2018), asList("dinner", "super-market"));

        Map<SearchTag, Money> expected = Map.of(new SearchTag("super-market", "super-market"), Money.moneyFor("50.00"),
                new SearchTag("dinner", "dinner"), Money.moneyFor("45.00"));

        verify(userRepository).currentLoggedUserName();
        verify(searchTagRepository).findSearchTagBy("dinner");
        verify(searchTagRepository).findSearchTagBy("super-market");
        verify(budgetExpenseRepository)
                .findByDateRange(new UserName("USER"), Date.firstDateOfMonth(Month.FEBRUARY, Year.of(2018)),
                        Date.lastDateOfMonth(Month.FEBRUARY, Year.of(2018)), "dinner", "super-market");

        System.out.println(actual);
        assertThat(actual.totalForSearchTags(), Is.is(expected));
        assertThat(actual.total(), Is.is(Money.moneyFor("95.00")));
    }


    @Test
    public void budgetExpenseTotalBySearchTagsWithoutConstraints() {
        given(userRepository.currentLoggedUserName())
                .willReturn(new UserName("USER"));

        given(budgetExpenseRepository.findByDateRange(new UserName("USER"),
                Date.firstDateOfMonth(Month.FEBRUARY, Year.of(2018)),
                Date.lastDateOfMonth(Month.FEBRUARY, Year.of(2018))))

                .willReturn(asList(new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("15/02/2018"), Money.moneyFor("10"), "dinner", "dinner"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("01/02/2018"), Money.moneyFor("12.50"), "super market", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("05/02/2018"), Money.moneyFor("12.50"), "super market", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("04/02/2018"), Money.moneyFor("20"), "dinner", "dinner"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("03/02/2018"), Money.moneyFor("12.50"), "super market", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("02/02/2018"), Money.moneyFor("12.50"), "super market", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("01/02/2018"), Money.moneyFor("15"), "dinner", "dinner")));

        given(searchTagRepository.findSearchTagBy("dinner")).willReturn(new SearchTag("dinner", "dinner"));
        given(searchTagRepository.findSearchTagBy("super-market")).willReturn(new SearchTag("super-market", "super-market"));

        SpentBudget actual = new FindSpentBudget(userRepository, budgetExpenseRepository, searchTagRepository)
                .findBy(Month.FEBRUARY, Year.of(2018), asList());

        Map<SearchTag, Money> expected = Map.of(new SearchTag("super-market", "super-market"), Money.moneyFor("50.00"),
                new SearchTag("dinner", "dinner"), Money.moneyFor("45.00"));

        verify(userRepository).currentLoggedUserName();
        verify(searchTagRepository).findSearchTagBy("dinner");
        verify(searchTagRepository).findSearchTagBy("super-market");
        verify(budgetExpenseRepository)
                .findByDateRange(new UserName("USER"),
                        Date.firstDateOfMonth(Month.FEBRUARY, Year.of(2018)),
                        Date.lastDateOfMonth(Month.FEBRUARY, Year.of(2018)));

        System.out.println(actual);
        assertThat(actual.totalForSearchTags(), Is.is(expected));
        assertThat(actual.total(), Is.is(Money.moneyFor("95.00")));
    }
}