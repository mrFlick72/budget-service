package it.valeriovaudi.onlyoneportal.budgetservice.web.adapter;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.SearchTag;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.NewBudgetExpenseRequest;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.SearchTagRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.UserRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.web.model.BudgetExpenseRepresentation;

import java.util.Optional;

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
                budgetExpense.getAmount().stringifyAmount(), budgetExpense.getNote(), budgetExpense.getTag(), searchTag);
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

}
