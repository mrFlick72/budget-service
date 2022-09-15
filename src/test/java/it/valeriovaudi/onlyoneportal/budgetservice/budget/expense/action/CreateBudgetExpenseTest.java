package it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.action;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.repository.BudgetExpenseRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.searchtag.SearchTag;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserName;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CreateBudgetExpenseTest {

    @Mock
    private BudgetExpenseRepository budgetExpenseRepository;

    @Mock
    private UserRepository userRepository;


    @Test
    public void saveANewBudgetExpenseWithoutASearchTag() {
        CreateBudgetExpense createBudgetExpense = new CreateBudgetExpense(budgetExpenseRepository, userRepository);

        Date date = Date.dateFor("22/10/2018");
        Money amount = Money.ONE;

        given(userRepository.currentLoggedUserName())
                .willReturn(new UserName("USER"));


        BudgetExpense budgetExpense = new BudgetExpense(null, new UserName("USER"), date, amount, "", SearchTag.DEFAULT_KEY);

        createBudgetExpense.newBudgetExpense(new NewBudgetExpenseRequest(date, amount, "", ""));

        verify(userRepository).currentLoggedUserName();
        verify(budgetExpenseRepository).save(budgetExpense);
    }
}