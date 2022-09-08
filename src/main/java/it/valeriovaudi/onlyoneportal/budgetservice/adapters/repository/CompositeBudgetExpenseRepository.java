package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.user.UserName;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetExpenseRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class CompositeBudgetExpenseRepository implements BudgetExpenseRepository {
    private final BudgetExpenseRepository primary;
    private final BudgetExpenseRepository secondary;

    public CompositeBudgetExpenseRepository(BudgetExpenseRepository primary, BudgetExpenseRepository secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    @Override
    public Optional<BudgetExpense> findFor(BudgetExpenseId budgetExpenseId) {
        Optional<BudgetExpense> result = primary.findFor(budgetExpenseId);
        secondary.findFor(budgetExpenseId);
        return result;
    }

    @Override
    public List<BudgetExpense> findByDateRange(UserName userName, Date star, Date end, String... searchTags) {
        List<BudgetExpense> result = primary.findByDateRange(userName, star, end, searchTags);
        secondary.findByDateRange(userName, star, end, searchTags);
        return result;
    }

    @Override
    public BudgetExpense save(BudgetExpense budgetExpense) {
        BudgetExpense result = primary.save(budgetExpense);
        secondary.save(budgetExpense);
        return result;
    }

    @Override
    public void delete(BudgetExpenseId idBudgetExpense) {
        primary.delete(idBudgetExpense);
        secondary.delete(idBudgetExpense);
    }
}
