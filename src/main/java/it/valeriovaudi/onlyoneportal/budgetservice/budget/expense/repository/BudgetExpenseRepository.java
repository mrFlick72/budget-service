package it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserName;

import java.util.List;
import java.util.Optional;

public interface BudgetExpenseRepository {

    Optional<BudgetExpense> findFor(BudgetExpenseId budgetExpenseId);

    List<BudgetExpense> findByDateRange(UserName userName, Date star, Date end, String... searchTags);

    BudgetExpense save(BudgetExpense budgetExpense);

    void delete(BudgetExpenseId idBudgetExpense);
}
