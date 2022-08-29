package it.valeriovaudi.onlyoneportal.budgetservice.web.config;


import it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository.JdbcBudgetExpenseRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository.JdbcBudgetRevenueRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository.JdbcSearchTagRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository.SpringSecurityUserRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetExpenseRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetRevenueRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.UserRepository;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public UserRepository userRepository() {
        return new SpringSecurityUserRepository();
    }

    @Bean
    public BudgetRevenueRepository jdbcBudgetRevenueRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcBudgetRevenueRepository(jdbcTemplate);
    }

    @Bean
    public BudgetExpenseRepository jdbBudgetExpenseRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcBudgetExpenseRepository(jdbcTemplate);
    }

    @Bean
    public JdbcSearchTagRepository jdbcSearchTagRepository(UserRepository userRepository, JdbcTemplate jdbcTemplate) {
        return new JdbcSearchTagRepository(userRepository, jdbcTemplate);
    }

    @Bean
    public RestTemplate repositoryServiceRestTemplate() {
        return new RestTemplateBuilder().build();
    }
}
