package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.user.UserName;

import java.util.Base64;
import java.util.Optional;

public class BudgetDynamoDbIdFactory {

    private final static Base64.Encoder ENCODER = Base64.getEncoder();

    private final SaltGenerator saltGenerator;

    public BudgetDynamoDbIdFactory(SaltGenerator saltGenerator) {
        this.saltGenerator = saltGenerator;
    }


    public BudgetExpenseId budgetIdFrom(BudgetExpense budgetExpense) {
        return Optional.ofNullable(budgetExpense.getId())
                .orElseGet(() -> new BudgetExpenseId(String.format("%s-%s", partitionKeyFrom(budgetExpense.getDate(), budgetExpense.getUserName()), rangeKeyFrom(budgetExpense))));
    }

    public String partitionKeyFrom(Date date, UserName userName) {
        int budgetExpenseYear = date.getLocalDate().getYear();
        int budgetExpenseMonth = date.getLocalDate().getMonthValue();
        String budgetExpenseUser = userName.getContent();

        String partitionKey = String.format("%s_%s_%s", budgetExpenseYear, budgetExpenseMonth, budgetExpenseUser);

        return ENCODER.encodeToString(partitionKey.getBytes());
    }

    public String partitionKeyFrom(BudgetExpenseId id) {
        return id.getContent().split("-")[0];
    }

    public String rangeKeyFrom(BudgetExpenseId id) {
        return id.getContent().split("-")[1];
    }


    private String rangeKeyFrom(BudgetExpense budgetExpense) {
        int dayOfMonth = budgetExpense.getDate().getLocalDate().getDayOfMonth();
        String salt = saltGenerator.newSalt();

        String rangeKey = String.format("%s_%s", dayOfMonth, salt);

        return ENCODER.encodeToString(rangeKey.getBytes());
    }
}