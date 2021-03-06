package it.valeriovaudi.onlyoneportal.budgetservice.domain.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetRevenue;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;

import java.util.List;

public interface BudgetRevenueRepository {

    List<BudgetRevenue> findByDateRange(String user, Date star, Date end);

    void save(BudgetRevenue budgetRevenue);

    void update(BudgetRevenue budgetRevenue);

    void delete(String idBudgetRevenue);
}
