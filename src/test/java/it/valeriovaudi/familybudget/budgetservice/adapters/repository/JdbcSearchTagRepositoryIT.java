package it.valeriovaudi.familybudget.budgetservice.adapters.repository;

import it.valeriovaudi.familybudget.budgetservice.domain.model.SearchTag;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


@JdbcTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = JdbcSearchTagRepositoryIT.Initializer.class)
public class JdbcSearchTagRepositoryIT {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcSearchTagRepository jdbcBudgetExpenseRepository;

    @Before
    public void setUp() {
        jdbcBudgetExpenseRepository = new JdbcSearchTagRepository(jdbcTemplate);
    }

    @ClassRule
    public static DockerComposeContainer postgres = new DockerComposeContainer(new File("src/test/resources/docker-compose.yml"))
            .withExposedService("postgres_1", 5432);

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            String serviceHost = postgres.getServiceHost("postgres_1", 5432);
            Integer servicePort = postgres.getServicePort("postgres_1", 5432);
            TestPropertyValues.of(format("spring.datasource.url=jdbc:postgresql://%s:%s/budget_expense", serviceHost, servicePort)).applyTo(configurableApplicationContext);
            TestPropertyValues.of("spring.datasource.username=root").applyTo(configurableApplicationContext);
            TestPropertyValues.of("spring.datasource.password=root").applyTo(configurableApplicationContext);
        }
    }

    @Test
    @Sql("classpath:/search_tag/findAll.sql")
    public void findAll() {
        assertThat(jdbcBudgetExpenseRepository.findAllSearchTag().size(), is(24));
    }

    @Test
    @Sql("classpath:/search_tag/findAll.sql")
    public void findSearchTagBy() {
        assertThat(jdbcBudgetExpenseRepository.findSearchTagBy("super-market"), is(new SearchTag("super-market", "Spesa")));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    @Sql("classpath:/search_tag/findAll.sql")
    public void delete() {
        jdbcBudgetExpenseRepository.delete("super-market");
        jdbcBudgetExpenseRepository.findSearchTagBy("super-market");
    }

    @Test
    @Sql("classpath:/search_tag/findAll.sql")
    public void save() {
        jdbcBudgetExpenseRepository.save(new SearchTag("test", "Test"));
        SearchTag actual = jdbcBudgetExpenseRepository.findSearchTagBy("test");
        assertThat(actual, is(new SearchTag("test", "Test")));
    }
}