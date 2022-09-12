package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.IdProvider;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;

import java.util.UUID;

public class DataBaseIdProvider implements IdProvider {

    public String id() {
        return UUID.randomUUID().toString();
    }

    public BudgetExpenseId newBudgetExpenseId() {
        return BudgetExpenseId.randomBudgetExpenseId();
    }
}
