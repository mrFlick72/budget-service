package it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;

import java.util.Objects;

public record BudgetRevenue(BudgetRevenueId id, String userName, Date registrationDate, Money amount, String note) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BudgetRevenue that = (BudgetRevenue) o;
        return Objects.equals(id, that.id) && Objects.equals(userName, that.userName) && Objects.equals(registrationDate, that.registrationDate) && Objects.equals(amount, that.amount) && Objects.equals(note, that.note);
    }

}
