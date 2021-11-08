package it.valeriovaudi.onlyoneportal.budgetservice.web.model;

import java.io.Serializable;
import java.util.List;


public record BudgetExpenseRepresentation(String id,
                                          String date,
                                          String amount,
                                          String note,
                                          String tagKey,
                                          String tagValue,
                                          List<String> attachments) implements Serializable {

}