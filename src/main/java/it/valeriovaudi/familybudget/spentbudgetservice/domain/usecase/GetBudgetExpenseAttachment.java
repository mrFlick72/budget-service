package it.valeriovaudi.familybudget.spentbudgetservice.domain.usecase;

import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.attachment.Attachment;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.attachment.AttachmentFileName;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.AttachmentRepository;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.BudgetExpenseRepository;

import java.util.Optional;

public class GetBudgetExpenseAttachment {

    private final AttachmentRepository attachmentRepository;
    private final BudgetExpenseRepository budgetExpenseRepository;

    public GetBudgetExpenseAttachment(AttachmentRepository attachmentRepository,
                                      BudgetExpenseRepository budgetExpenseRepository) {
        this.attachmentRepository = attachmentRepository;
        this.budgetExpenseRepository = budgetExpenseRepository;
    }

    public Optional<Attachment> findAttachmentFor(BudgetExpenseId budgetExpenseId, AttachmentFileName attachmentFileName) {
        return budgetExpenseRepository.findFor(budgetExpenseId)
                .flatMap(budgetExpense -> attachmentRepository.findAttachmentFor(budgetExpense, attachmentFileName));

    }
}
