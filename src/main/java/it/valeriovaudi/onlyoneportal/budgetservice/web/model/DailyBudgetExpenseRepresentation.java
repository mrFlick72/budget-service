package it.valeriovaudi.onlyoneportal.budgetservice.web.model;

import java.util.List;

public record DailyBudgetExpenseRepresentation(List<BudgetExpenseRepresentation> budgetExpenseRepresentationList,
                                               String date,
                                               String total) {
}
