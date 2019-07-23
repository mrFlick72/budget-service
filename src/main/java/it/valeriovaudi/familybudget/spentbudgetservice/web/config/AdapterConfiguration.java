package it.valeriovaudi.familybudget.spentbudgetservice.web.config;

import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.SearchTagRepository;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.UserRepository;
import it.valeriovaudi.familybudget.spentbudgetservice.web.adapter.BudgetExpenseAdapter;
import it.valeriovaudi.familybudget.spentbudgetservice.web.adapter.BudgetRevenueAdapter;
import it.valeriovaudi.familybudget.spentbudgetservice.web.adapter.SpentBudgetAdapter;
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