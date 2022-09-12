package it.valeriovaudi.onlyoneportal.budgetservice.web.config;


import it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository.DynamoDbIdProvider;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.IdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProviderConfig {

    @Bean
    public IdProvider idProvider() {
        return new DynamoDbIdProvider();
    }

}
