package it.valeriovaudi.familybudget.budgetservice.web.representation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode
public class BudgetRevenueRepresentation implements Serializable {
    private String id;
    private String date;
    private String amount;
    private String note;

    public BudgetRevenueRepresentation() { }

    public BudgetRevenueRepresentation(String id, String date, String amount, String note) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.note = note;
    }
}