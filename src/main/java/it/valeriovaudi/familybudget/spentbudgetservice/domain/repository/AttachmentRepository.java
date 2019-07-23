package it.valeriovaudi.familybudget.spentbudgetservice.domain.repository;

import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.attachment.Attachment;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.attachment.AttachmentFileName;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpense;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository {
    void save(BudgetExpense budgetExpense, Attachment attachment);

    List<AttachmentFileName> findFor(BudgetExpense budgetExpense);

    Optional<Attachment> findAttachmentFor(BudgetExpense budgetExpense, AttachmentFileName attachmentFileName);

    void delete(BudgetExpense budgetExpense, AttachmentFileName attachmentFileName);
}
