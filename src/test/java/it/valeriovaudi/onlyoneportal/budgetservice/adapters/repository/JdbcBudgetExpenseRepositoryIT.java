package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.user.UserName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@JdbcTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JdbcBudgetExpenseRepositoryIT extends AbstractBudgetExpenseRepositoryIT {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        budgetExpenseRepository = new JdbcBudgetExpenseRepository(jdbcTemplate);
    }


    BudgetExpense getBudgetExpense(String userName, String date, String amount, String note, String tag) {
        return jdbcTemplate.queryForObject("SELECT * FROM BUDGET_EXPENSE WHERE USER_NAME = ? AND DATE=? AND AMOUNT=? AND NOTE=? AND TAG=?",
                JdbcBudgetExpenseRepository.budgetExpenseRowMapper, userName, Date.dateFor(date).getLocalDate(),
                Money.moneyFor(amount).getAmount(), note, tag);
    }


    BudgetExpense getBudgetExpenseFor(BudgetExpenseId id) {
        BudgetExpense budgetExpense = jdbcTemplate.queryForObject("SELECT * FROM BUDGET_EXPENSE WHERE ID=?",
                JdbcBudgetExpenseRepository.budgetExpenseRowMapper, id.getContent());
        return new BudgetExpense(budgetExpense.getId(), new UserName("USER"), budgetExpense.getDate(), budgetExpense.getAmount(), budgetExpense.getNote(), budgetExpense.getTag());
    }

}