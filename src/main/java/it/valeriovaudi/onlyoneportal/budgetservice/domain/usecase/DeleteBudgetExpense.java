package it.valeriovaudi.onlyoneportal.budgetservice.domain.usecase;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseNotFoundException;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.AttachmentRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetExpenseRepository;

public class DeleteBudgetExpense {

    private final BudgetExpenseRepository budgetExpenseRepository;
    private final AttachmentRepository attachmentRepository;

    public DeleteBudgetExpense(BudgetExpenseRepository budgetExpenseRepository, AttachmentRepository attachmentRepository) {

        this.budgetExpenseRepository = budgetExpenseRepository;
        this.attachmentRepository = attachmentRepository;
    }

    public void delete(BudgetExpenseId budgetExpenseId) {
        budgetExpenseRepository.findFor(budgetExpenseId)
                .ifPresentOrElse(budgetExpense -> {
                            budgetExpense.getAttachmentFileNames()
                                    .forEach(attachmentFileName -> attachmentRepository.delete(budgetExpense, attachmentFileName));
                            budgetExpenseRepository.delete(budgetExpense.getId());
                        },
                        () -> {
                            throw new BudgetExpenseNotFoundException();
                        });
    }
}
