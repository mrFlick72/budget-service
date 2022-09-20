package it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.action;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.repository.BudgetExpenseRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DeleteBudgetExpenseTest {

    @Mock
    private BudgetExpenseRepository budgetExpenseRepository;


    @Test
    public void delete() {
        DeleteBudgetExpense deleteBudgetExpense = new DeleteBudgetExpense(budgetExpenseRepository);

        BudgetExpense budgetExpense = new BudgetExpense(new BudgetExpenseId("ID"),
                new UserName("USER"),
                Date.dateFor("22/01/2018"),
                Money.ONE,
                "NOTE",
                "TAG"
        );

        given(budgetExpenseRepository.findFor(budgetExpense.id()))
                .willReturn(Optional.of(budgetExpense));

        deleteBudgetExpense.delete(new BudgetExpenseId("ID"));

        verify(budgetExpenseRepository).findFor(budgetExpense.id());
        verify(budgetExpenseRepository).delete(budgetExpense.id());
    }
}