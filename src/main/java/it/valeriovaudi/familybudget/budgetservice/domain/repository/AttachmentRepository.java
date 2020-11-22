package it.valeriovaudi.familybudget.budgetservice.domain.repository;

import it.valeriovaudi.familybudget.budgetservice.domain.model.attachment.Attachment;
import it.valeriovaudi.familybudget.budgetservice.domain.model.attachment.AttachmentFileName;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpense;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository {
    void save(BudgetExpense budgetExpense, Attachment attachment);

    Optional<Attachment> findAttachmentFor(BudgetExpense budgetExpense, AttachmentFileName attachmentFileName);

    void delete(BudgetExpense budgetExpense, AttachmentFileName attachmentFileName);
}
