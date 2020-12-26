package it.valeriovaudi.familybudget.budgetservice.domain.usecase;

import it.valeriovaudi.familybudget.budgetservice.domain.model.IdProvider;
import it.valeriovaudi.familybudget.budgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.budgetservice.domain.model.SearchTag;
import it.valeriovaudi.familybudget.budgetservice.domain.model.attachment.Attachment;
import it.valeriovaudi.familybudget.budgetservice.domain.model.attachment.AttachmentFileName;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.NewBudgetExpenseRequest;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.budgetservice.domain.model.user.UserName;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.AttachmentRepository;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.BudgetExpenseRepository;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static java.util.Arrays.asList;
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

    @Mock
    private AttachmentRepository attachmentRepository;

    @Test
    public void saveANewAttachment() {
        CreateBudgetExpense createBudgetExpense = new CreateBudgetExpense(budgetExpenseRepository, attachmentRepository, userRepository, idProvider);
        Attachment attachment = new Attachment(new AttachmentFileName("AN_ATTACHMENT_NAME"), "A_CONTENT_TYPE", new byte[0]);

        BudgetExpense budgetExpense = new BudgetExpense(A_BUDGET_EXPENSE_ID,
                new UserName("USER"),
                Date.dateFor("22/10/2018"),
                Money.ONE, "", "a-tag");

        BudgetExpense budgetExpenseWithAttachment = new BudgetExpense(A_BUDGET_EXPENSE_ID,
                new UserName("USER"),
                Date.dateFor("22/10/2018"),
                Money.ONE, "", "a-tag", asList(new AttachmentFileName("AN_ATTACHMENT_NAME")));

        given(budgetExpenseRepository.findFor(A_BUDGET_EXPENSE_ID))
                .willReturn(Optional.of(budgetExpense));

        createBudgetExpense.newBudgetExpenseAttachment(A_BUDGET_EXPENSE_ID, attachment);

        verify(budgetExpenseRepository).findFor(A_BUDGET_EXPENSE_ID);
        verify(budgetExpenseRepository).save(budgetExpenseWithAttachment);
        verify(attachmentRepository).save(budgetExpenseWithAttachment, attachment);
    }

    @Test
    public void saveANewBudgetExpenseWithoutASearchTag() {
        CreateBudgetExpense createBudgetExpense = new CreateBudgetExpense(budgetExpenseRepository, attachmentRepository, userRepository, idProvider);

        Date date = Date.dateFor("22/10/2018");
        Money amount = Money.ONE;

        given(userRepository.currentLoggedUserName())
                .willReturn(new UserName("USER"));

        given(idProvider.budgetExpenseId())
                .willReturn(A_BUDGET_EXPENSE_ID);

        BudgetExpense budgetExpense = new BudgetExpense(A_BUDGET_EXPENSE_ID, new UserName("USER"), date, amount, "", SearchTag.DEFAULT_KEY);

        createBudgetExpense.newBudgetExpense(new NewBudgetExpenseRequest(date, amount, "", ""));

        verify(userRepository).currentLoggedUserName();
        verify(idProvider).budgetExpenseId();
        verify(budgetExpenseRepository).save(budgetExpense);
    }
}