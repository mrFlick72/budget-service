package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.SearchTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@JdbcTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JdbcSearchTagRepositoryIT {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcSearchTagRepository jdbcBudgetExpenseRepository;

    @BeforeEach
    public void setUp() {
        jdbcBudgetExpenseRepository = new JdbcSearchTagRepository(jdbcTemplate);
    }

    @Test
    @Sql("classpath:/search_tag/findAll.sql")
    public void findAll() {
        Assertions.assertEquals(jdbcBudgetExpenseRepository.findAllSearchTag().size(), 24);
    }

    @Test
    @Sql("classpath:/search_tag/findAll.sql")
    public void findSearchTagBy() {
        Assertions.assertEquals(jdbcBudgetExpenseRepository.findSearchTagBy("super-market"), new SearchTag("super-market", "Spesa"));
    }

    @Test
    @Sql("classpath:/search_tag/findAll.sql")
    public void delete() {
        jdbcBudgetExpenseRepository.delete("super-market");
        Assertions.assertThrows(EmptyResultDataAccessException.class,
                () -> jdbcBudgetExpenseRepository.findSearchTagBy("super-market"));
    }

    @Test
    @Sql("classpath:/search_tag/findAll.sql")
    public void save() {
        jdbcBudgetExpenseRepository.save(new SearchTag("test", "Test"));
        SearchTag actual = jdbcBudgetExpenseRepository.findSearchTagBy("test");
        Assertions.assertEquals(actual, new SearchTag("test", "Test"));
    }
}