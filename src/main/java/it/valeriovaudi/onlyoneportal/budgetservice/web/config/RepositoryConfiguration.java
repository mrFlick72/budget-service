package it.valeriovaudi.onlyoneportal.budgetservice.web.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository.JdbcBudgetExpenseRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository.JdbcBudgetRevenueRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository.JdbcSearchTagRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository.SpringSecurityUserRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository.attachment.AttachmentRepositoryConfigurationProperties;
import it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository.attachment.RestAttachmentRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.AttachmentRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetExpenseRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetRevenueRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@EnableConfigurationProperties({AttachmentRepositoryConfigurationProperties.class})
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
    public JdbcSearchTagRepository jdbcSearchTagRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcSearchTagRepository(jdbcTemplate);
    }

    @Bean
    public AttachmentRepository attachmentRepository(@Value("${spring.application.name}") String applicationName,
                                                     @Value("${repository-service.uri}") String repositoryServiceBaseUrl,
                                                     ObjectMapper objectMapper,
                                                     RestTemplate repositoryServiceRestTemplate) {
        String url = UriComponentsBuilder.fromHttpUrl(repositoryServiceBaseUrl)
                .pathSegment("documents", applicationName)
                .toUriString();
        return new RestAttachmentRepository(url, repositoryServiceRestTemplate, objectMapper);
    }

    @Bean
    public RestTemplate repositoryServiceRestTemplate() {
        return new RestTemplateBuilder().build();
    }
}
