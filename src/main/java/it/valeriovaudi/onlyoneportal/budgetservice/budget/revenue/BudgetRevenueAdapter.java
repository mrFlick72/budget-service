package it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserRepository;


public class BudgetRevenueAdapter {

    private final UserRepository userRepository;

    public BudgetRevenueAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public BudgetRevenue fromRepresentationToModel(BudgetRevenueRepresentation budgetRevenueRepresentation) {
        return new BudgetRevenue(
                new BudgetRevenueId(budgetRevenueRepresentation.id()),
                userRepository.currentLoggedUserName().content(),
                Date.dateFor(budgetRevenueRepresentation.date()),
                Money.moneyFor(budgetRevenueRepresentation.amount()),
                budgetRevenueRepresentation.note());
    }

    public BudgetRevenueRepresentation fromDomainToRepresentation(BudgetRevenue budgetRevenue) {
        return new BudgetRevenueRepresentation(budgetRevenue.id().content(),
                budgetRevenue.registrationDate().formattedDate(),
                budgetRevenue.amount().stringifyAmount(),
                budgetRevenue.note());
    }
}
