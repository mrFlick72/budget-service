package it.valeriovaudi.onlyoneportal.budgetservice.web.config;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.IdProvider;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetExpenseRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetRevenueRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.UserRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.usecase.*;
import it.valeriovaudi.onlyoneportal.budgetservice.searchtag.SearchTagRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BusinessLogicConfiguration {

    @Bean
    public DeleteBudgetExpense deleteBudgetExpense(BudgetExpenseRepository budgetExpenseRepository) {
        return new DeleteBudgetExpense(budgetExpenseRepository);
    }

    @Bean
    public UpdateBudgetExpenseDetails updateBudgetExpenseDetails(BudgetExpenseRepository budgetExpenseRepository) {
        return new UpdateBudgetExpenseDetails(budgetExpenseRepository);
    }


    @Bean
    public FindBudgetRevenue findBudgetRevenue(UserRepository userRepository,
                                               BudgetRevenueRepository budgetRevenueRepository) {
        return new FindBudgetRevenue(budgetRevenueRepository, userRepository);
    }

    @Bean
    public FindSpentBudget findSpentBudget(UserRepository userRepository,
                                           SearchTagRepository searchTagRepository,
                                           BudgetExpenseRepository budgetExpenseRepository) {
        return new FindSpentBudget(userRepository, budgetExpenseRepository, searchTagRepository);
    }


    @Bean
    public CreateBudgetExpense createBudgetExpense(UserRepository userRepository,
                                                   BudgetExpenseRepository budgetExpenseRepository,
                                                   IdProvider idProvider) {
        return new CreateBudgetExpense(budgetExpenseRepository, userRepository, idProvider);
    }

}
