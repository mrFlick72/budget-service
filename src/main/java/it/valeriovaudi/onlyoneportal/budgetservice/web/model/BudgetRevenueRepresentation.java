package it.valeriovaudi.onlyoneportal.budgetservice.web.model;

import java.io.Serializable;

public record BudgetRevenueRepresentation(String id,
                                          String date,
                                          String amount,
                                          String note)
        implements Serializable {
}