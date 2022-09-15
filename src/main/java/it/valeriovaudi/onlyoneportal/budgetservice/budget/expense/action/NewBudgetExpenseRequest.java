package it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.action;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class NewBudgetExpenseRequest {
    private final Date date;
    private final Money amount;
    private final String note;
    private final String tag;

    public NewBudgetExpenseRequest(Date date, Money amount, String note, String tag) {
        this.date = date;
        this.amount = amount;
        this.note = note;
        this.tag = tag;
    }
}
