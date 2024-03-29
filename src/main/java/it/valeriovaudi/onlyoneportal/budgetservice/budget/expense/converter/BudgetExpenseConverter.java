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
        String searchTag = Optional.ofNullable(searchTagRepository.findSearchTagBy(budgetExpense.tag()))
                .map(SearchTag::value).orElse("");
        return new BudgetExpenseRepresentation(budgetExpense.id().content(), budgetExpense.date().formattedDate(),
                budgetExpense.amount().stringifyAmount(), budgetExpense.note(), budgetExpense.tag(), searchTag);
    }

    public BudgetExpense representationModelToDomainModel(BudgetExpenseRepresentation budgetExpenseRepresentation) {
        return new BudgetExpense(new BudgetExpenseId(budgetExpenseRepresentation.id()),
                userRepository.currentLoggedUserName(),
                Date.dateFor(budgetExpenseRepresentation.date()),
                Money.moneyFor(budgetExpenseRepresentation.amount()),
                budgetExpenseRepresentation.note(), budgetExpenseRepresentation.tagKey());
    }

    public NewBudgetExpenseRequest newBudgetExpenseRequestFromRepresentation(BudgetExpenseRepresentation budgetExpenseRepresentation) {
        return new NewBudgetExpenseRequest(Date.dateFor(budgetExpenseRepresentation.date()),
                Money.moneyFor(budgetExpenseRepresentation.amount()),
                budgetExpenseRepresentation.note(), budgetExpenseRepresentation.tagKey());
    }

}
