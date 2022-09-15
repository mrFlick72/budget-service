package it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.converter;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.action.NewBudgetExpenseRequest;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.endpoint.BudgetExpenseRepresentation;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.searchtag.SearchTag;
import it.valeriovaudi.onlyoneportal.budgetservice.searchtag.SearchTagRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserRepository;

import java.util.Optional;

public class BudgetExpenseConverter {

    private final SearchTagRepository searchTagRepository;
    private final UserRepository userRepository;

    public BudgetExpenseConverter(SearchTagRepository searchTagRepository, UserRepository userRepository) {
        this.searchTagRepository = searchTagRepository;
        this.userRepository = userRepository;
    }

    public BudgetExpenseRepresentation domainToRepresentationModel(BudgetExpense budgetExpense) {
        String searchTag = Optional.ofNullable(searchTagRepository.findSearchTagBy(budgetExpense.getTag()))
                .map(SearchTag::getValue).orElse("");
        return new BudgetExpenseRepresentation(budgetExpense.getId().content(), budgetExpense.getDate().formattedDate(),
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
