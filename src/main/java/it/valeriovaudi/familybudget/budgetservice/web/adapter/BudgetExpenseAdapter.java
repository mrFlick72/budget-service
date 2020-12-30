package it.valeriovaudi.familybudget.budgetservice.web.adapter;

import it.valeriovaudi.familybudget.budgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.budgetservice.domain.model.SearchTag;
import it.valeriovaudi.familybudget.budgetservice.domain.model.attachment.AttachmentFileName;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.NewBudgetExpenseRequest;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.SearchTagRepository;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.UserRepository;
import it.valeriovaudi.familybudget.budgetservice.web.representation.BudgetExpenseRepresentation;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class BudgetExpenseAdapter {

    private final SearchTagRepository searchTagRepository;
    private final UserRepository userRepository;

    public BudgetExpenseAdapter(SearchTagRepository searchTagRepository, UserRepository userRepository) {
        this.searchTagRepository = searchTagRepository;
        this.userRepository = userRepository;
    }

    public BudgetExpenseRepresentation domainToRepresentationModel(BudgetExpense budgetExpense) {
        String searchTag = Optional.ofNullable(searchTagRepository.findSearchTagBy(budgetExpense.getTag()))
                .map(SearchTag::getValue).orElse("");
        return new BudgetExpenseRepresentation(budgetExpense.getId().getContent(), budgetExpense.getDate().formattedDate(),
                budgetExpense.getAmount().stringifyAmount(), budgetExpense.getNote(), budgetExpense.getTag(), searchTag,
                attachments(budgetExpense.getAttachmentFileNames()));
    }

    public BudgetExpense representationModelToDomainModel(BudgetExpenseRepresentation budgetExpenseRepresentation) {
        return new BudgetExpense(new BudgetExpenseId(budgetExpenseRepresentation.getId()),
                userRepository.currentLoggedUserName(),
                Date.dateFor(budgetExpenseRepresentation.getDate()),
                Money.moneyFor(budgetExpenseRepresentation.getAmount()),
                budgetExpenseRepresentation.getNote(), budgetExpenseRepresentation.getTagKey());
    }

    public NewBudgetExpenseRequest newBudgetExpenseRequestFromRepresentation(BudgetExpenseRepresentation budgetExpenseRepresentation) {
        return new NewBudgetExpenseRequest(Date.dateFor(budgetExpenseRepresentation.getDate()),
                Money.moneyFor(budgetExpenseRepresentation.getAmount()),
                budgetExpenseRepresentation.getNote(), budgetExpenseRepresentation.getTagKey());
    }


    private List<String> attachments(List<AttachmentFileName> attachments) {
        return attachments.stream().map(AttachmentFileName::getFileName).collect(toList());
    }


}
