package it.valeriovaudi.onlyoneportal.budgetservice.support;


import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.infrastructure.dynamodb.SaltGenerator;

import java.util.UUID;


public class BudgetFixture {

    public static final String SALT = "A_SALT";
    public static final SaltGenerator saltGenerator = () -> SALT;

    public static BudgetExpenseId emptyBudgetExpenseId() {
        return new BudgetExpenseId("");
    }

    public static BudgetExpenseId randomBudgetExpenseId() {
        return new BudgetExpenseId(UUID.randomUUID().toString());
    }
}
