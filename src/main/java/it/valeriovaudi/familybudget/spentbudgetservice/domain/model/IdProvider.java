package it.valeriovaudi.familybudget.spentbudgetservice.domain.model;

import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpenseId;

import java.util.UUID;

public class IdProvider {

    public String id() {
        return UUID.randomUUID().toString();
    }

    public BudgetExpenseId budgetExpenseId() {
        return BudgetExpenseId.randomBudgetExpenseId();
    }
}
