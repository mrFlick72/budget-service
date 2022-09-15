package it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model;


import it.valeriovaudi.onlyoneportal.budgetservice.budget.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;

import java.util.List;
import java.util.Objects;

public record DailyBudgetExpense(List<BudgetExpense> budgetExpenseList, Date date, Money total) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyBudgetExpense that = (DailyBudgetExpense) o;
        return Objects.equals(budgetExpenseList, that.budgetExpenseList) && Objects.equals(date, that.date) && Objects.equals(total, that.total);
    }

}
