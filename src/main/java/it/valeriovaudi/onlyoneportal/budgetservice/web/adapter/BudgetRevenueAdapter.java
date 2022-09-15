package it.valeriovaudi.onlyoneportal.budgetservice.web.adapter;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue.BudgetRevenue;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue.BudgetRevenueId;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.web.model.BudgetRevenueRepresentation;


public class BudgetRevenueAdapter {

    private final UserRepository userRepository;

    public BudgetRevenueAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public BudgetRevenue fromRepresentationToModel(BudgetRevenueRepresentation budgetRevenueRepresentation) {
        return new BudgetRevenue(budgetRevenueRepresentation.getId(),
                new BudgetRevenueId(budgetRevenueRepresentation.getId()),
                userRepository.currentLoggedUserName().getContent(),
                Date.dateFor(budgetRevenueRepresentation.getDate()),
                Money.moneyFor(budgetRevenueRepresentation.getAmount()),
                budgetRevenueRepresentation.getNote());
    }

    public BudgetRevenueRepresentation fromDomainToRepresentation(BudgetRevenue budgetRevenue) {
        return new BudgetRevenueRepresentation(budgetRevenue.getId(),
                budgetRevenue.getRegistrationDate().formattedDate(),
                budgetRevenue.getAmount().stringifyAmount(),
                budgetRevenue.getNote());
    }
}
