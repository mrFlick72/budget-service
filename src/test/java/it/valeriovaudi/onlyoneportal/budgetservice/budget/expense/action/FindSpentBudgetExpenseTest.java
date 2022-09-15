package it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.action;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.SpentBudget;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.repository.BudgetExpenseRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.searchtag.SearchTag;
import it.valeriovaudi.onlyoneportal.budgetservice.searchtag.SearchTagRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.support.BudgetFixture;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Month;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Year;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static it.valeriovaudi.onlyoneportal.budgetservice.support.UserTestFixture.A_USER_NAME;
import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
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
                .willReturn(A_USER_NAME);

        lenient().when(searchTagRepository.findSearchTagBy("super-market")).thenReturn(new SearchTag("super-market", "super-market"));
        given(searchTagRepository.findSearchTagBy("dinner")).willReturn(new SearchTag("dinner", "dinner"));

        given(budgetExpenseRepository.findByDateRange(A_USER_NAME, Date.firstDateOfMonth(Month.FEBRUARY, Year.of(2018)),
                Date.lastDateOfMonth(Month.FEBRUARY, Year.of(2018)), "dinner", "super-market"))

                .willReturn(asList(new BudgetExpense(BudgetFixture.emptyBudgetExpenseId(), A_USER_NAME, Date.dateFor("15/02/2018"), Money.moneyFor("10"), "dinner", "dinner"),
                        new BudgetExpense(BudgetFixture.emptyBudgetExpenseId(), A_USER_NAME, Date.dateFor("01/02/2018"), Money.moneyFor("12.50"), "super-market", "super-market"),
                        new BudgetExpense(BudgetFixture.emptyBudgetExpenseId(), A_USER_NAME, Date.dateFor("05/02/2018"), Money.moneyFor("12.50"), "super-market", "super-market"),
                        new BudgetExpense(BudgetFixture.emptyBudgetExpenseId(), A_USER_NAME, Date.dateFor("04/02/2018"), Money.moneyFor("20"), "dinner", "dinner"),
                        new BudgetExpense(BudgetFixture.emptyBudgetExpenseId(), A_USER_NAME, Date.dateFor("03/02/2018"), Money.moneyFor("12.50"), "super-market", "super-market"),
                        new BudgetExpense(BudgetFixture.emptyBudgetExpenseId(), A_USER_NAME, Date.dateFor("02/02/2018"), Money.moneyFor("12.50"), "super-market", "super-market"),
                        new BudgetExpense(BudgetFixture.emptyBudgetExpenseId(), A_USER_NAME, Date.dateFor("01/02/2018"), Money.moneyFor("15"), "dinner", "dinner")));

        SpentBudget actual = new FindSpentBudget(userRepository, budgetExpenseRepository, searchTagRepository)
                .findBy(Month.FEBRUARY, Year.of(2018), asList("dinner", "super-market"));

        Map<SearchTag, Money> expected = Map.of(new SearchTag("super-market", "super-market"), Money.moneyFor("50.00"),
                new SearchTag("dinner", "dinner"), Money.moneyFor("45.00"));

        verify(userRepository).currentLoggedUserName();
        verify(searchTagRepository).findSearchTagBy("dinner");
        verify(searchTagRepository).findSearchTagBy("super-market");
        verify(budgetExpenseRepository)
                .findByDateRange(A_USER_NAME, Date.firstDateOfMonth(Month.FEBRUARY, Year.of(2018)),
                        Date.lastDateOfMonth(Month.FEBRUARY, Year.of(2018)), "dinner", "super-market");

        Assertions.assertEquals(expected, actual.totalForSearchTags());
        Assertions.assertEquals(actual.total(), Money.moneyFor("95.00"));
    }


    @Test
    public void budgetExpenseTotalBySearchTagsWithoutConstraints() {
        given(userRepository.currentLoggedUserName())
                .willReturn(A_USER_NAME);

        given(budgetExpenseRepository.findByDateRange(A_USER_NAME,
                Date.firstDateOfMonth(Month.FEBRUARY, Year.of(2018)),
                Date.lastDateOfMonth(Month.FEBRUARY, Year.of(2018))))

                .willReturn(asList(new BudgetExpense(BudgetFixture.emptyBudgetExpenseId(), A_USER_NAME, Date.dateFor("15/02/2018"), Money.moneyFor("10"), "dinner", "dinner"),
                        new BudgetExpense(BudgetFixture.emptyBudgetExpenseId(), A_USER_NAME, Date.dateFor("01/02/2018"), Money.moneyFor("12.50"), "super market", "super-market"),
                        new BudgetExpense(BudgetFixture.emptyBudgetExpenseId(), A_USER_NAME, Date.dateFor("05/02/2018"), Money.moneyFor("12.50"), "super market", "super-market"),
                        new BudgetExpense(BudgetFixture.emptyBudgetExpenseId(), A_USER_NAME, Date.dateFor("04/02/2018"), Money.moneyFor("20"), "dinner", "dinner"),
                        new BudgetExpense(BudgetFixture.emptyBudgetExpenseId(), A_USER_NAME, Date.dateFor("03/02/2018"), Money.moneyFor("12.50"), "super market", "super-market"),
                        new BudgetExpense(BudgetFixture.emptyBudgetExpenseId(), A_USER_NAME, Date.dateFor("02/02/2018"), Money.moneyFor("12.50"), "super market", "super-market"),
                        new BudgetExpense(BudgetFixture.emptyBudgetExpenseId(), A_USER_NAME, Date.dateFor("01/02/2018"), Money.moneyFor("15"), "dinner", "dinner")));

        lenient().when(searchTagRepository.findSearchTagBy("super-market")).thenReturn(new SearchTag("super-market", "super-market"));
        given(searchTagRepository.findSearchTagBy("dinner")).willReturn(new SearchTag("dinner", "dinner"));

        SpentBudget actual = new FindSpentBudget(userRepository, budgetExpenseRepository, searchTagRepository)
                .findBy(Month.FEBRUARY, Year.of(2018), asList());

        Map<SearchTag, Money> expected = Map.of(new SearchTag("super-market", "super-market"), Money.moneyFor("50.00"),
                new SearchTag("dinner", "dinner"), Money.moneyFor("45.00"));

        verify(userRepository).currentLoggedUserName();
        verify(searchTagRepository).findSearchTagBy("dinner");
        verify(searchTagRepository).findSearchTagBy("super-market");
        verify(budgetExpenseRepository)
                .findByDateRange(A_USER_NAME,
                        Date.firstDateOfMonth(Month.FEBRUARY, Year.of(2018)),
                        Date.lastDateOfMonth(Month.FEBRUARY, Year.of(2018)));

        Assertions.assertEquals(actual.totalForSearchTags(), expected);
        Assertions.assertEquals(actual.total(), Money.moneyFor("95.00"));
    }
}