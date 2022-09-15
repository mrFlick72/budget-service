package it.valeriovaudi.onlyoneportal.budgetservice.domain.usecase;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.IdProvider;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.NewBudgetExpenseRequest;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.user.UserName;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetExpenseRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.UserRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.searchtag.SearchTag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CreateBudgetExpenseTest {

    private static final BudgetExpenseId A_BUDGET_EXPENSE_ID = new BudgetExpenseId("A_BUDGET_EXPENSE_ID");

    @Mock
    private BudgetExpenseRepository budgetExpenseRepository;

    @Mock
    private IdProvider idProvider;

    @Mock
    private UserRepository userRepository;


    @Test
    public void saveANewBudgetExpenseWithoutASearchTag() {
        CreateBudgetExpense createBudgetExpense = new CreateBudgetExpense(budgetExpenseRepository, userRepository, idProvider);

        Date date = Date.dateFor("22/10/2018");
        Money amount = Money.ONE;

        given(userRepository.currentLoggedUserName())
                .willReturn(new UserName("USER"));

        given(idProvider.newBudgetExpenseId())
                .willReturn(A_BUDGET_EXPENSE_ID);

        BudgetExpense budgetExpense = new BudgetExpense(A_BUDGET_EXPENSE_ID, new UserName("USER"), date, amount, "", SearchTag.DEFAULT_KEY);

        createBudgetExpense.newBudgetExpense(new NewBudgetExpenseRequest(date, amount, "", ""));

        verify(userRepository).currentLoggedUserName();
        verify(idProvider).newBudgetExpenseId();
        verify(budgetExpenseRepository).save(budgetExpense);
    }
}