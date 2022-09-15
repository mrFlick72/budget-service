package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.IdProvider;

public class DynamoDbIdProvider implements IdProvider {

    public String id() {
        return null;
    }

    public BudgetExpenseId newBudgetExpenseId() {
        return null;
    }
}
