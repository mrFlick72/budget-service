package it.valeriovaudi.onlyoneportal.budgetservice.web.model;

import java.io.Serializable;
import java.util.List;

public record SpentBudgetRepresentation(String total,
                                        List<DailyBudgetExpenseRepresentation> dailyBudgetExpenseRepresentationList,
                                        List<TotalBySearchTagDetail> totalDetailList

) implements Serializable {
}
