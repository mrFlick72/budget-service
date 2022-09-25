package it.valeriovaudi.onlyoneportal.budgetservice.config;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.converter.BudgetExpenseConverter;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.converter.SpentBudgetConverter;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue.BudgetRevenueConverter;
import it.valeriovaudi.onlyoneportal.budgetservice.searchtag.SearchTagRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AdapterConfiguration {

    @Bean
    public BudgetRevenueConverter budgetRevenueAdapter(UserRepository userRepository) {
        return new BudgetRevenueConverter(userRepository);
    }

    @Bean
    public BudgetExpenseConverter budgetExpenseAdapter(UserRepository userRepository,
                                                       SearchTagRepository searchTagRepository) {
        return new BudgetExpenseConverter(searchTagRepository, userRepository);
    }

    @Bean
    public SpentBudgetConverter spentBudgetAdapter(BudgetExpenseConverter budgetExpenseConverter) {
        return new SpentBudgetConverter(budgetExpenseConverter);
    }
}