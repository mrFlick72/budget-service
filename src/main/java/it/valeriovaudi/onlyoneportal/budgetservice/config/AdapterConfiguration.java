package it.valeriovaudi.onlyoneportal.budgetservice.config;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.converter.BudgetExpenseConverter;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.converter.SpentBudgetConverter;
import it.valeriovaudi.onlyoneportal.budgetservice.searchtag.SearchTagRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.web.adapter.BudgetRevenueAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdapterConfiguration {

    @Bean
    public BudgetRevenueAdapter budgetRevenueAdapter(UserRepository userRepository) {
        return new BudgetRevenueAdapter(userRepository);
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