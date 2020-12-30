package it.valeriovaudi.familybudget.budgetservice.web.representation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@EqualsAndHashCode
public class DailyBudgetExpenseRepresentation {

    private List<BudgetExpenseRepresentation> budgetExpenseRepresentationList;
    private String date;
    private String total;

    public DailyBudgetExpenseRepresentation() { }

    public DailyBudgetExpenseRepresentation(List<BudgetExpenseRepresentation> budgetExpenseRepresentationList,
                                            String date, String total) {
        this.budgetExpenseRepresentationList = budgetExpenseRepresentationList;
        this.date = date;
        this.total = total;
    }
}
