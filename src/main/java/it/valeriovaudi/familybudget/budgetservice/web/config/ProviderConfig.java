package it.valeriovaudi.familybudget.budgetservice.web.config;


import it.valeriovaudi.familybudget.budgetservice.domain.model.IdProvider;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.TimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProviderConfig {

    @Bean
    public IdProvider idProvider() {
        return new IdProvider();
    }

    @Bean
    public TimeProvider timeProvider() {
        return new TimeProvider();
    }
}
