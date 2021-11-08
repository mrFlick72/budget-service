package it.valeriovaudi.onlyoneportal.budgetservice.web.model;

import java.util.List;

public record BudgetSearchCriteriaRepresentation(Integer month,
                                                 Integer year,
                                                 List<String> searchTagList) {

}
