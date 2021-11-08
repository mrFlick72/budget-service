package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetRevenue;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetRevenueRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional
public class JdbcBudgetRevenueRepository implements BudgetRevenueRepository {

    static final String INSERT_BUDGET_REVENUE_QUERY = "INSERT INTO BUDGET_REVENUE (ID, USER_NAME, REGISTRATION_DATE, AMOUNT, NOTE) VALUES (?, ?, ?, ?, ?)";
    static final String SELECT_BETWEEN_DATE_CRITERIA = "SELECT * FROM BUDGET_REVENUE WHERE USER_NAME = ? AND REGISTRATION_DATE BETWEEN ? AND ? ORDER BY REGISTRATION_DATE ASC";
    static final String UPDATE_QUERY = "UPDATE BUDGET_REVENUE SET USER_NAME=?, REGISTRATION_DATE=?, AMOUNT=?, NOTE=? WHERE ID=?";
    static final String DELETE_QUERY = "DELETE FROM BUDGET_REVENUE WHERE ID=?";

    private final JdbcTemplate jdbcTemplate;

    public JdbcBudgetRevenueRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<BudgetRevenue> findByDateRange(String user, Date star, Date end) {
        return jdbcTemplate.query(SELECT_BETWEEN_DATE_CRITERIA, new Object[]{user, star.getLocalDate(), end.getLocalDate()}, budgetRevenueRowMapper);

    }

    @Override
    public void save(BudgetRevenue budgetRevenue) {
        jdbcTemplate.update(INSERT_BUDGET_REVENUE_QUERY, budgetRevenue.getId(), budgetRevenue.getUserName(),
                budgetRevenue.getRegistrationDate().getLocalDate(), budgetRevenue.getAmount().getAmount(),
                budgetRevenue.getNote());
    }

    @Override
    public void update(BudgetRevenue budgetRevenue) {
        jdbcTemplate.update(UPDATE_QUERY, budgetRevenue.getUserName(),
                budgetRevenue.getRegistrationDate().getLocalDate(), budgetRevenue.getAmount().getAmount(),
                budgetRevenue.getNote(), budgetRevenue.getId());
    }

    @Override
    public void delete(String idBudgetRevenue) {
        jdbcTemplate.update(DELETE_QUERY, idBudgetRevenue);
    }

    RowMapper<BudgetRevenue> budgetRevenueRowMapper =
            (resultSet, i) -> new BudgetRevenue(resultSet.getString("ID"),
                    resultSet.getString("USER_NAME"),
                    new Date(resultSet.getObject("REGISTRATION_DATE", LocalDate.class)),
                    new Money(resultSet.getBigDecimal("AMOUNT")),
                    resultSet.getString("NOTE"));

}
