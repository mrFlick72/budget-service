package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.IdProvider;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;

import java.util.UUID;

public class DynamoDbIdProvider implements IdProvider {

    public String id() {
        return null;
    }

    public BudgetExpenseId newBudgetExpenseId() {
        return null;
    }
}
