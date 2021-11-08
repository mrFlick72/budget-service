package it.valeriovaudi.onlyoneportal.budgetservice.web.adapter;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetRevenue;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.UserRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.web.model.BudgetRevenueRepresentation;


public class BudgetRevenueAdapter {

    private final UserRepository userRepository;

    public BudgetRevenueAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public BudgetRevenue fromRepresentationToModel(BudgetRevenueRepresentation budgetRevenueRepresentation) {
        return new BudgetRevenue(budgetRevenueRepresentation.id(),
                userRepository.currentLoggedUserName().getContent(),
                Date.dateFor(budgetRevenueRepresentation.date()),
                Money.moneyFor(budgetRevenueRepresentation.amount()),
                budgetRevenueRepresentation.note());
    }

    public BudgetRevenueRepresentation fromDomainToRepresentation(BudgetRevenue budgetRevenue) {
        return new BudgetRevenueRepresentation(budgetRevenue.id(),
                budgetRevenue.registrationDate().formattedDate(),
                budgetRevenue.amount().stringifyAmount(),
                budgetRevenue.note());
    }
}