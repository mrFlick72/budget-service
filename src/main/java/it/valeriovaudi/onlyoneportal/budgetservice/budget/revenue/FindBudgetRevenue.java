package it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue;

import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Month;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Year;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserRepository;

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