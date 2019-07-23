package it.valeriovaudi.familybudget.spentbudgetservice.domain.processor;


import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpense;

import java.io.InputStream;
import java.util.List;

@FunctionalInterface
public interface DataImporter {

    List<BudgetExpense> loadData(InputStream inputStream);
}
