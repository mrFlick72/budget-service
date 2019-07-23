package it.valeriovaudi.familybudget.spentbudgetservice.web.config;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import it.valeriovaudi.familybudget.spentbudgetservice.adapters.repository.*;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.BudgetExpenseRepository;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.BudgetRevenueRepository;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.UserRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

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
    public S3AttachmentRepository s3AttachmentRepository(AttachmentRepositoryConfigurationProperties config) {
        AWSCredentials credentials = new BasicAWSCredentials(config.getAccessKey(), config.getSecretKey());

        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(config.getRegion())
                .build();

        return new S3AttachmentRepository(s3client, config.getBucketName(), config.getBucketPrefix());
    }
}
