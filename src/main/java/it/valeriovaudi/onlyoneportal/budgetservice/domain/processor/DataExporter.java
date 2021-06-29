package it.valeriovaudi.onlyoneportal.budgetservice.domain.processor;


import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;

import java.io.InputStream;
import java.util.List;

@FunctionalInterface
public interface DataExporter {
    InputStream exportData(List<BudgetExpense> budgetExpenseList);
}
