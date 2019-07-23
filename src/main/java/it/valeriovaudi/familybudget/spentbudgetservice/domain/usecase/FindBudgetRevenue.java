package it.valeriovaudi.familybudget.spentbudgetservice.domain.usecase;

import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetRevenue;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Month;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Year;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.BudgetRevenueRepository;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.UserRepository;

import java.util.List;

public class FindBudgetRevenue {

    private final BudgetRevenueRepository budgetRevenueRepository;
    private final UserRepository userRepository;

    public FindBudgetRevenue(BudgetRevenueRepository budgetRevenueRepository, UserRepository userRepository) {
        this.budgetRevenueRepository = budgetRevenueRepository;
        this.userRepository = userRepository;
    }

    public List<BudgetRevenue> findBy(Year year) {
        return budgetRevenueRepository.findByDateRange(userRepository.currentLoggedUserName().getContent(),
                Date.firstDateOfMonth(Month.JANUARY, year),
                Date.lastDateOfMonth(Month.DECEMBER, year));
    }
}