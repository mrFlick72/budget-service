package it.valeriovaudi.familybudget.budgetservice.web.adapter;

import it.valeriovaudi.familybudget.budgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetRevenue;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.UserRepository;
import it.valeriovaudi.familybudget.budgetservice.web.model.BudgetRevenueRepresentation;


public class BudgetRevenueAdapter {

    private final UserRepository userRepository;

    public BudgetRevenueAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public BudgetRevenue fromRepresentationToModel(BudgetRevenueRepresentation budgetRevenueRepresentation) {
        return new BudgetRevenue(budgetRevenueRepresentation.getId(),
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
