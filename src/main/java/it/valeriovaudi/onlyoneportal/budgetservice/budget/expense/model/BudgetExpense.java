package it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(exclude = {"id"})
public final class BudgetExpense {
    private final BudgetExpenseId id;
    private final UserName userName;
    private final Date date;
    private final Money amount;
    private final String note;
    private final String tag;

    public BudgetExpense(BudgetExpenseId id, UserName userName, Date date, Money amount, String note, String tag) {
        this.id = id;
        this.userName = userName;
        this.date = date;
        this.amount = amount;
        this.note = note;
        this.tag = tag;
    }

}