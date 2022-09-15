package it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserName;

import java.util.Objects;

public record BudgetExpense(BudgetExpenseId id, UserName userName, Date date, Money amount, String note, String tag) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BudgetExpense that = (BudgetExpense) o;
        return Objects.equals(id, that.id) && Objects.equals(userName, that.userName) && Objects.equals(date, that.date) && Objects.equals(amount, that.amount) && Objects.equals(note, that.note) && Objects.equals(tag, that.tag);
    }

}