package it.valeriovaudi.familybudget.budgetservice.adapters.repository;

import it.valeriovaudi.familybudget.budgetservice.domain.model.SearchTag;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


@JdbcTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JdbcSearchTagRepositoryIT {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcSearchTagRepository jdbcBudgetExpenseRepository;

    @Before
    public void setUp() {
        jdbcBudgetExpenseRepository = new JdbcSearchTagRepository(jdbcTemplate);
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