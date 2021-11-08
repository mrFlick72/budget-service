package it.valeriovaudi.onlyoneportal.budgetservice.web.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode
public class BudgetSearchCriteriaRepresentation {

    private Integer month;
    private Integer year;
    private List<String> searchTagList = new ArrayList<>();

    public BudgetSearchCriteriaRepresentation() {
    }

    public BudgetSearchCriteriaRepresentation(Integer month, Integer year, List<String> searchTagList) {
        this.month = month;
        this.year = year;
        this.searchTagList = searchTagList;
    }
}
