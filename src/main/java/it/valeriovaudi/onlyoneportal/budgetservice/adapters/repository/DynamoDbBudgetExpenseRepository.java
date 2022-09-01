package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.user.UserName;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetExpenseRepository;

import java.util.List;
import java.util.Optional;

public class DynamoDbBudgetExpenseRepository implements BudgetExpenseRepository {
    @Override
    public Optional<BudgetExpense> findFor(BudgetExpenseId budgetExpenseId) {
        return Optional.empty();
    }

    @Override
    public List<BudgetExpense> findByDateRange(UserName userName, Date star, Date end, String... searchTags) {
        return null;
    }

    @Override
    public void save(BudgetExpense budgetExpense) {

    }

    @Override
    public void delete(BudgetExpenseId idBudgetExpense) {

    }
}
