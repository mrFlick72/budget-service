package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.user.UserName;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.BDDMockito.given;


@JdbcTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JdbcSearchTagRepositoryIT extends AbstractSearchTagRepositoryIT {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcSearchTagRepository jdbcBudgetExpenseRepository;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        jdbcBudgetExpenseRepository = new JdbcSearchTagRepository(userRepository, jdbcTemplate);

        given(userRepository.currentLoggedUserName())
                .willReturn(new UserName("amail@test.com"));
    }

    @Test
    @Sql("classpath:/search_tag/findAll.sql")
    public void findAll() {
        super.findAll(jdbcBudgetExpenseRepository);
    }

    @Test
    @Sql("classpath:/search_tag/findAll.sql")
    public void findSearchTagBy() {
        super.findSearchTagBy(jdbcBudgetExpenseRepository);
    }

    @Test
    @Sql("classpath:/search_tag/findAll.sql")
    public void delete() {
        super.delete(jdbcBudgetExpenseRepository);
    }

    @Test
    @Sql("classpath:/search_tag/findAll.sql")
    public void save() {
        super.save(jdbcBudgetExpenseRepository);
    }
}