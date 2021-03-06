package it.valeriovaudi.onlyoneportal.budgetservice.domain.usecase;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.attachment.AttachmentFileName;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseNotFoundException;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.user.UserName;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.AttachmentRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetExpenseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DeleteBudgetExpenseAttachmentTest {

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private BudgetExpenseRepository budgetExpenseRepository;

    private DeleteBudgetExpenseAttachment deleteBudgetExpenseAttachment;
    private static final BudgetExpenseId A_BUDGET_EXPENSE_ID = new BudgetExpenseId("A_BUDGET_EXPENSE_ID");

    @BeforeEach
    public void setUp() {
        deleteBudgetExpenseAttachment = new DeleteBudgetExpenseAttachment(budgetExpenseRepository, attachmentRepository);
    }

    @Test
    public void deleteABudgetExpenseAttachment() {
        DeleteBudgetExpenseAttachment deleteBudgetExpenseAttachment = this.deleteBudgetExpenseAttachment;
        AttachmentFileName attachmentFileName = new AttachmentFileName("AN_ATTACHMENT_FILE_NAME");

        BudgetExpense budgetExpense = new BudgetExpense(A_BUDGET_EXPENSE_ID,
                new UserName("USER"),
                Date.dateFor("22/02/2018"),
                Money.ONE, "NOTE", "TAG", asList(attachmentFileName));

        given(budgetExpenseRepository.findFor(A_BUDGET_EXPENSE_ID))
                .willReturn(Optional.of(budgetExpense));

        BudgetExpense newBudgetExpense = new BudgetExpense(budgetExpense.getId(),
                new UserName("USER"),
                budgetExpense.getDate(),
                budgetExpense.getAmount(),
                budgetExpense.getNote(),
                budgetExpense.getTag(),
                asList());

        deleteBudgetExpenseAttachment.deleteAttachmentFor(A_BUDGET_EXPENSE_ID, attachmentFileName);

        verify(budgetExpenseRepository).findFor(A_BUDGET_EXPENSE_ID);
        verify(budgetExpenseRepository).save(newBudgetExpense);
        verify(attachmentRepository).delete(newBudgetExpense, attachmentFileName);
    }

    @Test
    public void whenABudgetExpenseDoesNotExist() {
        DeleteBudgetExpenseAttachment deleteBudgetExpenseAttachment = this.deleteBudgetExpenseAttachment;
        AttachmentFileName attachmentFileName = new AttachmentFileName("AN_ATTACHMENT_FILE_NAME");

        given(budgetExpenseRepository.findFor(A_BUDGET_EXPENSE_ID))
                .willReturn(Optional.empty());

        Assertions.assertThrows(BudgetExpenseNotFoundException.class, () -> {
            deleteBudgetExpenseAttachment.deleteAttachmentFor(A_BUDGET_EXPENSE_ID, attachmentFileName);
        });

        verify(budgetExpenseRepository).findFor(A_BUDGET_EXPENSE_ID);
    }

}