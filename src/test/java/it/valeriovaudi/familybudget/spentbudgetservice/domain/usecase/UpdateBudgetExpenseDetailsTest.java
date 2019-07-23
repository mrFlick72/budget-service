package it.valeriovaudi.familybudget.spentbudgetservice.domain.usecase;

import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.SearchTag;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.attachment.AttachmentFileName;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.user.UserName;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.BudgetExpenseRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UpdateBudgetExpenseDetailsTest {


    @Mock
    private BudgetExpenseRepository budgetExpenseRepository;

    @Test
    public void update() {
        BudgetExpenseId budgetExpenseId = BudgetExpenseId.randomBudgetExpenseId();

        UpdateBudgetExpenseDetails updateBudgetExpenseDetails = new UpdateBudgetExpenseDetails(budgetExpenseRepository);

        BudgetExpense budgetExpense = new BudgetExpense(budgetExpenseId, new UserName("USER"), Date.dateFor("22/02/2018"), Money.ONE, "test", SearchTag.DEFAULT_KEY);
        BudgetExpense foundBudgetExpense = new BudgetExpense(budgetExpenseId, new UserName("USER"), Date.dateFor("22/02/2018"), Money.ONE, "", SearchTag.DEFAULT_KEY, asList(new AttachmentFileName("att.pdf")));

        BudgetExpense updatedBudgetExpense = new BudgetExpense(budgetExpenseId, new UserName("USER"), Date.dateFor("22/02/2018"), Money.ONE, "test", SearchTag.DEFAULT_KEY, asList(new AttachmentFileName("att.pdf")));

        given(budgetExpenseRepository.findFor(budgetExpense.getId()))
                .willReturn(Optional.of(foundBudgetExpense));

        updateBudgetExpenseDetails.updateWithoutAttachment(budgetExpense);

        verify(budgetExpenseRepository).findFor(budgetExpense.getId());
        verify(budgetExpenseRepository).save(updatedBudgetExpense);
    }
}