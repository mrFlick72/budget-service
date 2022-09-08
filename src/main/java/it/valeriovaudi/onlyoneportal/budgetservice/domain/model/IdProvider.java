package it.valeriovaudi.onlyoneportal.budgetservice.domain.model;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;

public interface IdProvider {

    String id();

    BudgetExpenseId newBudgetExpenseId();
}
