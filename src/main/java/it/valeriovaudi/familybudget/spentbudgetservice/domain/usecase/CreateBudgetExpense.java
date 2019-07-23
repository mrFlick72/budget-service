package it.valeriovaudi.familybudget.spentbudgetservice.domain.usecase;

import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.IdProvider;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.SearchTag;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.attachment.Attachment;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.attachment.AttachmentFileName;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.NewBudgetExpenseRequest;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.AttachmentRepository;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.BudgetExpenseRepository;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class CreateBudgetExpense {

    private final UserRepository userRepository;
    private final BudgetExpenseRepository budgetExpenseRepository;
    private final AttachmentRepository attachmentRepository;
    private final IdProvider idProvider;
    private final String DEFAULT_TAG = SearchTag.DEFAULT_KEY;

    public CreateBudgetExpense(BudgetExpenseRepository budgetExpenseRepository,
                               AttachmentRepository attachmentRepository,
                               UserRepository userRepository, IdProvider idProvider) {
        this.budgetExpenseRepository = budgetExpenseRepository;
        this.attachmentRepository = attachmentRepository;
        this.userRepository = userRepository;
        this.idProvider = idProvider;
    }

    public BudgetExpense newBudgetExpense(NewBudgetExpenseRequest newBudgetExpenseRequest) {
        BudgetExpense budgetExpense = new BudgetExpense(idProvider.budgetExpenseId(),
                userRepository.currentLoggedUserName(),
                newBudgetExpenseRequest.getDate(),
                newBudgetExpenseRequest.getAmount(),
                newBudgetExpenseRequest.getNote(),
                getSearchTag(newBudgetExpenseRequest));

        budgetExpenseRepository.save(budgetExpense);
        return budgetExpense;
    }

    private String getSearchTag(NewBudgetExpenseRequest newBudgetExpenseRequest) {
        return Optional.ofNullable(newBudgetExpenseRequest.getTag())
                .map(tag -> tag.isBlank() ? DEFAULT_TAG : tag)
                .orElse(DEFAULT_TAG);
    }

    // fixme understand how throw an error in case that I am puttin a new attacment for a budget expense that I cant do
    public void newBudgetExpenseAttachment(BudgetExpenseId budgetExpenseId, Attachment attachment) {
        budgetExpenseRepository.findFor(budgetExpenseId)
                .ifPresent(budgetExpense -> {
                    List<AttachmentFileName> attachmentFileNames = budgetExpense.getAttachmentFileNames();
                    attachmentFileNames.add(attachment.getName());
                    BudgetExpense budgetExpenseWithAttachments = new BudgetExpense(budgetExpense.getId(),
                            budgetExpense.getUserName(),
                            budgetExpense.getDate(),
                            budgetExpense.getAmount(),
                            budgetExpense.getNote(),
                            budgetExpense.getTag(),
                            attachmentFileNames);
                    budgetExpenseRepository.save(budgetExpenseWithAttachments);
                    attachmentRepository.save(budgetExpenseWithAttachments, attachment);
                });
    }
}
