package it.valeriovaudi.onlyoneportal.budgetservice.domain.model;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;

import java.util.UUID;

public class IdProvider {

    public String id() {
        return UUID.randomUUID().toString();
    }

    public BudgetExpenseId budgetExpenseId() {
        return BudgetExpenseId.randomBudgetExpenseId();
    }
}
