package it.valeriovaudi.familybudget.budgetservice.domain.usecase;

import it.valeriovaudi.familybudget.budgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.budgetservice.domain.model.attachment.AttachmentFileName;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.budgetservice.domain.model.user.UserName;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.AttachmentRepository;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.BudgetExpenseRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DeleteBudgetExpenseTest {

    @Mock
    private BudgetExpenseRepository budgetExpenseRepository;

    @Mock
    private AttachmentRepository attachmentRepository;

    @Test
    public void delete() {
        DeleteBudgetExpense deleteBudgetExpense =
                new DeleteBudgetExpense(budgetExpenseRepository, attachmentRepository);

        AttachmentFileName anAttachment = new AttachmentFileName("AN_ATTACHMENT");
        AttachmentFileName anotherAttachment = new AttachmentFileName("ANOTHER_ATTACHMENT");

        BudgetExpense budgetExpense = new BudgetExpense(new BudgetExpenseId("ID"),
                new UserName("USER"),
                Date.dateFor("22/01/2018"),
                Money.ONE,
                "NOTE",
                "TAG",
                asList(anAttachment, anotherAttachment));

        given(budgetExpenseRepository.findFor(budgetExpense.getId()))
                .willReturn(Optional.of(budgetExpense));

        deleteBudgetExpense.delete(new BudgetExpenseId("ID"));

        verify(budgetExpenseRepository).findFor(budgetExpense.getId());
        verify(attachmentRepository).delete(budgetExpense, anAttachment);
        verify(attachmentRepository).delete(budgetExpense, anotherAttachment);
        verify(budgetExpenseRepository).delete(budgetExpense.getId());
    }
}