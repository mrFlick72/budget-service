package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;


import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.attachment.AttachmentFileName;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.user.UserName;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetExpenseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

@Slf4j
@Transactional
public class JdbcBudgetExpenseRepository implements BudgetExpenseRepository {

    static final String SELECT_ONE_QUERY = "SELECT * FROM BUDGET_EXPENSE WHERE ID=?";
    static final String SELECT_BETWEEN_DATE_CRITERIA_WITHOUT_SEARCH_TAG = "SELECT * FROM BUDGET_EXPENSE WHERE USER_NAME = ? AND date BETWEEN ? AND ?";
    static final String SELECT_BETWEEN_DATE_CRITERIA_WITH_SEARCH_TAG = "SELECT * FROM BUDGET_EXPENSE WHERE USER_NAME = ? AND date BETWEEN ? AND ? AND TAG IN (%s)";

    static final String INSERT_BUDGET_EXPENSE_QUERY = "INSERT INTO BUDGET_EXPENSE(ID, USER_NAME, DATE, AMOUNT, NOTE, TAG) VALUES (?, ?, ?, ?, ?, ?) ON CONFLICT (ID) DO UPDATE SET DATE=?, AMOUNT=?, NOTE=?, TAG=?";

    static final String DELETE_BUDGET_EXPENSE_QUERY = "DELETE FROM budget_expense WHERE ID=?";
    static final String INSERT_BUDGET_EXPENSE_ATTACHMENT_QUERY = "INSERT INTO BUDGET_EXPENSE_ATTACHMENTS(BUDGET_EXPENSE_ID, ATTACHMENT_NAME) VALUES (?, ?) ON CONFLICT (BUDGET_EXPENSE_ID, ATTACHMENT_NAME) DO UPDATE SET BUDGET_EXPENSE_ID=?, ATTACHMENT_NAME=?";

    static final String SELECT_ATTACHMENT_FOR_QUERY_BUDGET_EXPENSE_ATTACHMENT_QUERY = "SELECT * FROM BUDGET_EXPENSE_ATTACHMENTS WHERE BUDGET_EXPENSE_ID=?";
    static final String DELETE_ALL_ATTACHMENT_FOR_QUERY_BUDGET_EXPENSE_ATTACHMENT_QUERY = "DELETE FROM BUDGET_EXPENSE_ATTACHMENTS WHERE BUDGET_EXPENSE_ID=?";

    private final JdbcTemplate jdbcTemplate;

    public JdbcBudgetExpenseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<BudgetExpense> findFor(BudgetExpenseId budgetExpenseId) {
        return jdbcTemplate.query(SELECT_ONE_QUERY, new Object[]{budgetExpenseId.getContent()}, budgetExpenseRowMapper)
                .stream().findFirst()
                .map(budgetExpenseWith(getAttachmentFileNamesFor(budgetExpenseId)));
    }

    private List<AttachmentFileName> getAttachmentFileNamesFor(BudgetExpenseId budgetExpenseId) {
        return jdbcTemplate.query(SELECT_ATTACHMENT_FOR_QUERY_BUDGET_EXPENSE_ATTACHMENT_QUERY,
                new Object[]{budgetExpenseId.getContent()}, budgetExpenseAttachmentRowMapper);
    }

    private Function<BudgetExpense, BudgetExpense> budgetExpenseWith(List<AttachmentFileName> attachmentFileNames) {
        return budgetExpense -> {
            return new BudgetExpense(budgetExpense.getId(),
                    budgetExpense.getUserName(),
                    budgetExpense.getDate(),
                    budgetExpense.getAmount(),
                    budgetExpense.getNote(),
                    budgetExpense.getTag(),
                    attachmentFileNames);
        };
    }

    @Override
    public List<BudgetExpense> findByDateRange(UserName userName, Date star, Date end, String... searchTags) {
        String query = inClauseOfSearchTags(searchTags)
                .map(inClauseOfSearchTagsCriteria -> format(SELECT_BETWEEN_DATE_CRITERIA_WITH_SEARCH_TAG, inClauseOfSearchTagsCriteria))
                .orElse(SELECT_BETWEEN_DATE_CRITERIA_WITHOUT_SEARCH_TAG);

        return jdbcTemplate.query(query, new Object[]{userName.getContent(), star.getLocalDate(), end.getLocalDate()}, budgetExpenseRowMapper)
                .stream().map(expense -> budgetExpenseWith(getAttachmentFileNamesFor(expense.getId())).apply(expense))
                .collect(Collectors.toList());
    }

    private Optional<String> inClauseOfSearchTags(String[] searchTags) {
        Optional<String> result = Optional.empty();
        if (searchTags.length > 0) {
            String inClauseOfSearchTags = Stream.of(searchTags).map(item -> format("'%s'", item)).reduce("", (s1, s2) -> s1 + "," + s2);
            inClauseOfSearchTags = inClauseOfSearchTags.substring(1);
            result = Optional.of(inClauseOfSearchTags);
        }
        return result;
    }

    @Override
    public void save(BudgetExpense budgetExpense) {
        jdbcTemplate.update(INSERT_BUDGET_EXPENSE_QUERY, budgetExpense.getId().getContent(),
                budgetExpense.getUserName().getContent(),
                budgetExpense.getDate().getLocalDate(),
                budgetExpense.getAmount().getAmount(), budgetExpense.getNote(), budgetExpense.getTag(),

                budgetExpense.getDate().getLocalDate(),
                budgetExpense.getAmount().getAmount(), budgetExpense.getNote(), budgetExpense.getTag());

        deleteAllBudgetExpenseAttachmentFor(budgetExpense.getId());

        budgetExpense.getAttachmentFileNames().forEach(attachmentFileName ->
                jdbcTemplate.update(INSERT_BUDGET_EXPENSE_ATTACHMENT_QUERY, budgetExpense.getId().getContent(), attachmentFileName.getFileName(),
                        budgetExpense.getId().getContent(), attachmentFileName.getFileName()));
    }

    private void deleteAllBudgetExpenseAttachmentFor(BudgetExpenseId budgetExpenseId) {
        jdbcTemplate.update(DELETE_ALL_ATTACHMENT_FOR_QUERY_BUDGET_EXPENSE_ATTACHMENT_QUERY, budgetExpenseId.getContent());
    }

    @Override
    public void delete(BudgetExpenseId idBudgetExpense) {
        deleteAllBudgetExpenseAttachmentFor(idBudgetExpense);
        jdbcTemplate.update(DELETE_BUDGET_EXPENSE_QUERY, idBudgetExpense.getContent());
    }

    RowMapper<BudgetExpense> budgetExpenseRowMapper =
            (resultSet, i) -> {
                return new BudgetExpense(new BudgetExpenseId(resultSet.getString("ID")),
                        new UserName(resultSet.getString("USER_NAME")),
                        new Date(resultSet.getObject("DATE", LocalDate.class)),
                        new Money(resultSet.getBigDecimal("AMOUNT")),
                        resultSet.getString("NOTE"),
                        resultSet.getString("TAG"));
            };

    RowMapper<AttachmentFileName> budgetExpenseAttachmentRowMapper =
            (resultSet, i) -> new AttachmentFileName(resultSet.getString("ATTACHMENT_NAME"));
}
