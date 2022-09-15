package it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model;


import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@EqualsAndHashCode
public class DailyBudgetExpense {

    private List<BudgetExpense> budgetExpenseList;
    private Date date;
    private Money total;

    public DailyBudgetExpense() { }

    public DailyBudgetExpense(List<BudgetExpense> budgetExpenseList,
                              Date date, Money total) {

        this.budgetExpenseList = budgetExpenseList;
        this.date = date;
        this.total = total;
    }
}
