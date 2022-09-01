package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.user.UserName;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetExpenseRepository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;
import java.util.Optional;

public class DynamoDbBudgetExpenseRepository implements BudgetExpenseRepository {
    private final String tableName;
    private final DynamoDbClient dynamoClient;

    public DynamoDbBudgetExpenseRepository(String tableName, DynamoDbClient dynamoClient) {
        this.tableName = tableName;
        this.dynamoClient = dynamoClient;
    }

    @Override
    public Optional<BudgetExpense> findFor(BudgetExpenseId budgetExpenseId) {
        return Optional.empty();
    }

    @Override
    public List<BudgetExpense> findByDateRange(UserName userName, Date star, Date end, String... searchTags) {
        return null;
    }

    @Override
    public void save(BudgetExpense budgetExpense) {
        partitionKeyFor(budgetExpense);
        rangeKeyFor(budgetExpense);


    }

    private void partitionKeyFor(BudgetExpense budgetExpense) {

        budgetExpense.getDate().getLocalDate().getYear();
        budgetExpense.getDate().getLocalDate().getMonthValue();
    }

    private void rangeKeyFor(BudgetExpense budgetExpense) {

    }

    @Override
    public void delete(BudgetExpenseId idBudgetExpense) {

    }
}

class BudgetDynamoDbIdFactory {
    public String partitionKeyFor(BudgetExpense budgetExpense) {
        int budgetExpenseYear = budgetExpense.getDate().getLocalDate().getYear();
        int budgetExpenseMonth = budgetExpense.getDate().getLocalDate().getMonthValue();
        String budgetExpenseUser = budgetExpense.getUserName().getContent();
        return String.format("%s_%s_%s", budgetExpenseYear, budgetExpenseMonth, budgetExpenseUser);
    }

    public String rangeKeyFor(BudgetExpense budgetExpense) {
        int dayOfMonth = budgetExpense.getDate().getLocalDate().getDayOfMonth();
        String budgetId = budgetExpense.getId().getContent();
        return String.format("%s_%s", dayOfMonth, budgetId);
    }

    private String encrypt(String data) {
        return null;
    }
}