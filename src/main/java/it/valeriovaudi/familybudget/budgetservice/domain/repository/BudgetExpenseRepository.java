package it.valeriovaudi.familybudget.budgetservice.domain.repository;

import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.budgetservice.domain.model.user.UserName;

import java.util.List;
import java.util.Optional;

public interface BudgetExpenseRepository {

    Optional<BudgetExpense> findFor(BudgetExpenseId budgetExpenseId);

    List<BudgetExpense> findByDateRange(UserName userName, Date star, Date end, String... searchTags);

    void save(BudgetExpense budgetExpense);

    void delete(BudgetExpenseId idBudgetExpense);
}