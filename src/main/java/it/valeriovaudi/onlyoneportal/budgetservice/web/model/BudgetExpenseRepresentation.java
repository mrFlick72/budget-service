package it.valeriovaudi.onlyoneportal.budgetservice.web.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode
public class BudgetExpenseRepresentation implements Serializable {
    private String id;
    private String date;
    private String amount;
    private String note;
    private String tagKey;
    private String tagValue;

    public BudgetExpenseRepresentation() {
    }

    public BudgetExpenseRepresentation(String id, String date, String amount, String note, String tagKey, String tagValue) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.note = note;
        this.tagKey = tagKey;
        this.tagValue = tagValue;

    }
}