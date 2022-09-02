package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;

import java.util.Base64;

class BudgetDynamoDbIdFactory {

    private final static Base64.Encoder ENCODER = Base64.getEncoder();

    public String partitionKeyFor(BudgetExpense budgetExpense) {
        int budgetExpenseYear = budgetExpense.getDate().getLocalDate().getYear();
        int budgetExpenseMonth = budgetExpense.getDate().getLocalDate().getMonthValue();
        String budgetExpenseUser = budgetExpense.getUserName().getContent();

        String partitionKey = String.format("%s_%s_%s", budgetExpenseYear, budgetExpenseMonth, budgetExpenseUser);

        return ENCODER.encodeToString(partitionKey.getBytes());
    }

    public String rangeKeyFor(BudgetExpense budgetExpense) {
        int dayOfMonth = budgetExpense.getDate().getLocalDate().getDayOfMonth();
        String budgetId = budgetExpense.getId().getContent();

        String rangeKey = String.format("%s_%s", dayOfMonth, budgetId);

        return ENCODER.encodeToString(rangeKey.getBytes());
    }

}
