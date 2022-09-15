package it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.action;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.repository.BudgetExpenseRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.searchtag.SearchTag;
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
public class UpdateBudgetExpenseDetailsTest {


    @Mock
    private BudgetExpenseRepository budgetExpenseRepository;

    @Test
    public void update() {
        BudgetExpenseId budgetExpenseId = BudgetExpenseId.randomBudgetExpenseId();

        UpdateBudgetExpenseDetails updateBudgetExpenseDetails = new UpdateBudgetExpenseDetails(budgetExpenseRepository);

        BudgetExpense budgetExpense = new BudgetExpense(budgetExpenseId, new UserName("USER"), Date.dateFor("22/02/2018"), Money.ONE, "test", SearchTag.DEFAULT_KEY);
        BudgetExpense foundBudgetExpense = new BudgetExpense(budgetExpenseId, new UserName("USER"), Date.dateFor("22/02/2018"), Money.ONE, "", SearchTag.DEFAULT_KEY);

        BudgetExpense updatedBudgetExpense = new BudgetExpense(budgetExpenseId, new UserName("USER"), Date.dateFor("22/02/2018"), Money.ONE, "test", SearchTag.DEFAULT_KEY);

        given(budgetExpenseRepository.findFor(budgetExpense.getId()))
                .willReturn(Optional.of(foundBudgetExpense));

        updateBudgetExpenseDetails.updateWithoutAttachment(budgetExpense);

        verify(budgetExpenseRepository).findFor(budgetExpense.getId());
        verify(budgetExpenseRepository).save(updatedBudgetExpense);
    }
}