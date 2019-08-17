package it.valeriovaudi.familybudget.budgetservice.domain.model;

import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpenseId;

import java.util.UUID;

public class IdProvider {

    public String id() {
        return UUID.randomUUID().toString();
    }

    public BudgetExpenseId budgetExpenseId() {
        return BudgetExpenseId.randomBudgetExpenseId();
    }
}
