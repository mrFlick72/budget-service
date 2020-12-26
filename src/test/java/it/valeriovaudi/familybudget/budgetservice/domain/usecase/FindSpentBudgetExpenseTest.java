package it.valeriovaudi.familybudget.budgetservice.domain.usecase;

import it.valeriovaudi.familybudget.budgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.budgetservice.domain.model.SearchTag;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.SpentBudget;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Month;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Year;
import it.valeriovaudi.familybudget.budgetservice.domain.model.user.UserName;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.BudgetExpenseRepository;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.SearchTagRepository;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpenseId.emptyBudgetExpenseId;
import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
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

        Assertions.assertEquals(actual.totalForSearchTags(), expected);
        Assertions.assertEquals(actual.total(), Money.moneyFor("95.00"));
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

        Assertions.assertEquals(actual.totalForSearchTags(), expected);
        Assertions.assertEquals(actual.total(), Money.moneyFor("95.00"));
    }
}