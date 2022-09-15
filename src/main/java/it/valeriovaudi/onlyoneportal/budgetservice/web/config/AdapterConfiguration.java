package it.valeriovaudi.onlyoneportal.budgetservice.web.config;

import it.valeriovaudi.onlyoneportal.budgetservice.searchtag.SearchTagRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.web.adapter.BudgetExpenseAdapter;
import it.valeriovaudi.onlyoneportal.budgetservice.web.adapter.BudgetRevenueAdapter;
import it.valeriovaudi.onlyoneportal.budgetservice.web.adapter.SpentBudgetAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdapterConfiguration {

    @Bean
    public BudgetRevenueAdapter budgetRevenueAdapter(UserRepository userRepository) {
        return new BudgetRevenueAdapter(userRepository);
    }

    @Bean
    public BudgetExpenseAdapter budgetExpenseAdapter(UserRepository userRepository,
                                                     SearchTagRepository searchTagRepository) {
        return new BudgetExpenseAdapter(searchTagRepository, userRepository);
    }

    @Bean
    public SpentBudgetAdapter spentBudgetAdapter(BudgetExpenseAdapter budgetExpenseAdapter) {
        return new SpentBudgetAdapter(budgetExpenseAdapter);
    }
}