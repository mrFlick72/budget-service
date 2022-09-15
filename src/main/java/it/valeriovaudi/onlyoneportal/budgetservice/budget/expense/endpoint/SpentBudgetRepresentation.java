package it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.endpoint;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode
public class SpentBudgetRepresentation implements Serializable {

    private List<DailyBudgetExpenseRepresentation> dailyBudgetExpenseRepresentationList;
    private List<TotalBySearchTagDetail> totalDetailList;
    private String total;

    public SpentBudgetRepresentation() {
    }

    public SpentBudgetRepresentation(String total,
                                     List<DailyBudgetExpenseRepresentation> dailyBudgetExpenseRepresentationList,
                                     List<TotalBySearchTagDetail> totalDetailList) {
        this.dailyBudgetExpenseRepresentationList = dailyBudgetExpenseRepresentationList;
        this.totalDetailList = totalDetailList;
        this.total = total;
    }
}
