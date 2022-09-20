package it.valeriovaudi.onlyoneportal.budgetservice.config;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.action.CreateBudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.action.DeleteBudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.action.FindSpentBudget;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.action.UpdateBudgetExpenseDetails;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.repository.BudgetExpenseRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue.BudgetRevenueRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue.FindBudgetRevenue;
import it.valeriovaudi.onlyoneportal.budgetservice.searchtag.SearchTagRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserRepository;
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
                                                   BudgetExpenseRepository budgetExpenseRepository) {
        return new CreateBudgetExpense(budgetExpenseRepository, userRepository);
    }

}
