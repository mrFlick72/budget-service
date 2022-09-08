package it.valeriovaudi.onlyoneportal.budgetservice.web.config;


import it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository.DynamoDbIdProvider;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.IdProvider;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.TimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProviderConfig {

    @Bean
    public IdProvider idProvider() {
        return new DynamoDbIdProvider();
    }

    @Bean
    public TimeProvider timeProvider() {
        return new TimeProvider();
    }
}
