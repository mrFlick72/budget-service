package it.valeriovaudi.onlyoneportal.budgetservice.domain.model;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpenseId;

public interface IdProvider {

    String id();

    BudgetExpenseId newBudgetExpenseId();
}
