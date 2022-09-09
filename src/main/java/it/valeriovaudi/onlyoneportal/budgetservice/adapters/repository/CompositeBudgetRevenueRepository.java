package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetRevenue;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetRevenueRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class CompositeBudgetRevenueRepository implements BudgetRevenueRepository {
    private final BudgetRevenueRepository primary;
    private final BudgetRevenueRepository secondary;

    public CompositeBudgetRevenueRepository(BudgetRevenueRepository primary, BudgetRevenueRepository secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }


    @Override
    public List<BudgetRevenue> findByDateRange(String user, Date star, Date end) {
        List<BudgetRevenue> result = primary.findByDateRange(user, star, end);
        secondary.findByDateRange(user, star, end);
        return result;
    }

    @Override
    public BudgetRevenue save(BudgetRevenue budgetRevenue) {
        BudgetRevenue result = primary.save(budgetRevenue);
        secondary.save(budgetRevenue);
        return result;
    }

    @Override
    public void update(BudgetRevenue budgetRevenue) {
        primary.update(budgetRevenue);
        secondary.update(budgetRevenue);
    }

    @Override
    public void delete(String idBudgetRevenue) {
        primary.delete(idBudgetRevenue);
        secondary.delete(idBudgetRevenue);
    }
}
