package it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class BudgetRevenue {

    //todo make it a BudgetRevenueId
    private final String id;

    //todo make it a UserName
    private final String userName;
    private final Date registrationDate;
    private final Money amount;
    private final String note;

    public BudgetRevenue(String id, String userName, Date registrationDate, Money amount, String note) {
        this.id = id;
        this.userName = userName;
        this.registrationDate = registrationDate;
        this.amount = amount;
        this.note = note;
    }
}
