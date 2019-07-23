package it.valeriovaudi.familybudget.spentbudgetservice.web.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

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
    private List<String> attachments;

    public BudgetExpenseRepresentation() {
    }

    public BudgetExpenseRepresentation(String id, String date, String amount, String note, String tagKey, String tagValue, List<String> attachments) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.note = note;
        this.tagKey = tagKey;
        this.tagValue = tagValue;

        this.attachments = attachments;
    }
}