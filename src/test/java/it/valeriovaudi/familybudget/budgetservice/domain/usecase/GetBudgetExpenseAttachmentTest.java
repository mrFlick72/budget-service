package it.valeriovaudi.familybudget.budgetservice.domain.usecase;

import it.valeriovaudi.familybudget.budgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.budgetservice.domain.model.attachment.Attachment;
import it.valeriovaudi.familybudget.budgetservice.domain.model.attachment.AttachmentFileName;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.budgetservice.domain.model.user.UserName;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.AttachmentRepository;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.BudgetExpenseRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class GetBudgetExpenseAttachmentTest {

    private static final Date DATE = Date.dateFor("22/02/2018");
    private static final Money ONE = Money.ONE;
    private static final String NOTE = "NOTE";
    private static final String TAG = "TAG";
    private static final BudgetExpenseId BUDGET_EXPENSE_ID = new BudgetExpenseId("BUDGET_EXPENSE_ID");
    private final UserName USER = new UserName("USER");

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private BudgetExpenseRepository budgetExpenseRepository;
    private GetBudgetExpenseAttachment getBudgetExpenseAttachment;

    @Before
    public void setUp() {
        getBudgetExpenseAttachment = new GetBudgetExpenseAttachment(attachmentRepository, budgetExpenseRepository);
    }

    @Test
    public void getAttachmentFor() {
        AttachmentFileName attachmentFileName = new AttachmentFileName("AN_ATTACHMENT_FILE_NAME");
        Attachment attachment = new Attachment(attachmentFileName, "CONTENT_TYPE", "CONTENT".getBytes());

        BudgetExpense budgetExpense = new BudgetExpense(BUDGET_EXPENSE_ID, USER, DATE, ONE, NOTE, TAG, asList(attachmentFileName));

        given(budgetExpenseRepository.findFor(BUDGET_EXPENSE_ID))
                .willReturn(Optional.of(budgetExpense));

        given(attachmentRepository.findAttachmentFor(budgetExpense, attachmentFileName)).willReturn(Optional.of(attachment));

        Attachment actual = getBudgetExpenseAttachment.findAttachmentFor(BUDGET_EXPENSE_ID, attachmentFileName).get();

        assertThat(actual, is(new Attachment(attachmentFileName, "CONTENT_TYPE", "CONTENT".getBytes())));

        Mockito.verify(budgetExpenseRepository).findFor(BUDGET_EXPENSE_ID);
        Mockito.verify(attachmentRepository).findAttachmentFor(budgetExpense, attachmentFileName);
    }

    @Test
    public void whenWeDoNotHaveAnAttachmentFor() {
        AttachmentFileName attachmentFileName = new AttachmentFileName("AN_ATTACHMENT_FILE_NAME");
        BudgetExpense budgetExpense = new BudgetExpense(BUDGET_EXPENSE_ID, USER, DATE, ONE, NOTE, TAG);

        given(budgetExpenseRepository.findFor(BUDGET_EXPENSE_ID))
                .willReturn(Optional.of(budgetExpense));

        given(attachmentRepository.findAttachmentFor(budgetExpense, attachmentFileName)).willReturn(Optional.empty());

        Optional<Attachment> actual = getBudgetExpenseAttachment.findAttachmentFor(BUDGET_EXPENSE_ID, attachmentFileName);

        assertThat(actual, is(Optional.empty()));

        Mockito.verify(budgetExpenseRepository).findFor(BUDGET_EXPENSE_ID);
        Mockito.verify(attachmentRepository).findAttachmentFor(budgetExpense, attachmentFileName);
    }

    @Test
    public void whenWeDoNotHaveAnBudgetExpense() {
        AttachmentFileName attachmentFileName = new AttachmentFileName("AN_ATTACHMENT_FILE_NAME");

        given(budgetExpenseRepository.findFor(BUDGET_EXPENSE_ID))
                .willReturn(Optional.empty());

        Optional<Attachment> actual = getBudgetExpenseAttachment.findAttachmentFor(BUDGET_EXPENSE_ID, attachmentFileName);

        assertThat(actual, is(Optional.empty()));

        Mockito.verify(budgetExpenseRepository).findFor(BUDGET_EXPENSE_ID);
    }
}