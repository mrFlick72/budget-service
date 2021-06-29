package it.valeriovaudi.onlyoneportal.budgetservice.domain.usecase;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.attachment.AttachmentFileName;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseNotFoundException;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.AttachmentRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetExpenseRepository;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class DeleteBudgetExpenseAttachment {

    private final BudgetExpenseRepository budgetExpenseRepository;
    private final AttachmentRepository attachmentRepository;

    public DeleteBudgetExpenseAttachment(BudgetExpenseRepository budgetExpenseRepository, AttachmentRepository attachmentRepository) {

        this.budgetExpenseRepository = budgetExpenseRepository;
        this.attachmentRepository = attachmentRepository;
    }

    public void deleteAttachmentFor(BudgetExpenseId budgetExpenseId, AttachmentFileName attachmentFileName) {
        budgetExpenseRepository.findFor(budgetExpenseId)
                .ifPresentOrElse(budgetExpense -> {
                            List<AttachmentFileName> filtredAttachmentFileNames =
                                    budgetExpense.getAttachmentFileNames()
                                            .stream()
                                            .filter(attachmentName -> !Objects.equals(attachmentName, attachmentFileName))
                                            .collect(toList());

                            BudgetExpense newBudgetExpense = new BudgetExpense(budgetExpense.getId(),
                                    budgetExpense.getUserName(),
                                    budgetExpense.getDate(),
                                    budgetExpense.getAmount(),
                                    budgetExpense.getNote(),
                                    budgetExpense.getTag(),
                                    filtredAttachmentFileNames);
                            attachmentRepository.delete(newBudgetExpense, attachmentFileName);
                            budgetExpenseRepository.save(newBudgetExpense);
                        },
                        () -> {
                            throw new BudgetExpenseNotFoundException();
                        });
    }
}
