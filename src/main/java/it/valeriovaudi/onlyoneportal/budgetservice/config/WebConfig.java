package it.valeriovaudi.onlyoneportal.budgetservice.config;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.converter.StringToBudgetSearchCriteriaRepresentationConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToBudgetSearchCriteriaRepresentationConverter());
    }
}
