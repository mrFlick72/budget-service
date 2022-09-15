package it.valeriovaudi.onlyoneportal.budgetservice.domain.usecase;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.user.UserName;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetExpenseRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.searchtag.SearchTag;
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