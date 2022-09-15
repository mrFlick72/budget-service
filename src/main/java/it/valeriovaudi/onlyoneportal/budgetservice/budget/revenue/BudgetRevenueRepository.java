package it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue;

import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;

import java.util.List;

public interface BudgetRevenueRepository {

    List<BudgetRevenue> findByDateRange(String user, Date star, Date end);

    BudgetRevenue save(BudgetRevenue budgetRevenue);

    void update(BudgetRevenue budgetRevenue);

    void delete(String idBudgetRevenue);
}
