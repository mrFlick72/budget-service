package it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.UUID;

@ToString
@EqualsAndHashCode
public class BudgetExpenseId {

    private final String content;

    public BudgetExpenseId(String content) {
        this.content = content;
    }

    public static BudgetExpenseId emptyBudgetExpenseId() {
        return new BudgetExpenseId("");
    }
    public static BudgetExpenseId randomBudgetExpenseId() {
        return new BudgetExpenseId(UUID.randomUUID().toString());
    }

    public String getContent() {
        return content;
    }
}
