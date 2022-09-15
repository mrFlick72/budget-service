package it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import lombok.Getter;

import java.util.Objects;

@Getter
public final class BudgetRevenue {

    //todo make it a BudgetRevenueId
    private final String id;
    private final BudgetRevenueId newId;

    private final String userName;
    private final Date registrationDate;
    private final Money amount;
    private final String note;

    public BudgetRevenue(String id, BudgetRevenueId newId, String userName, Date registrationDate, Money amount, String note) {
        this.id = id;
        this.newId = newId;
        this.userName = userName;
        this.registrationDate = registrationDate;
        this.amount = amount;
        this.note = note;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BudgetRevenue that = (BudgetRevenue) o;
        return Objects.equals(id, that.id) && Objects.equals(userName, that.userName) && Objects.equals(registrationDate, that.registrationDate) && Objects.equals(amount, that.amount) && Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, registrationDate, amount, note);
    }
}
