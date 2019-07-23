package it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget;

import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Date;
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
