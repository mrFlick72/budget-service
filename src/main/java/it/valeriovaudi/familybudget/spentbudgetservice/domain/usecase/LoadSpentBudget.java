package it.valeriovaudi.familybudget.spentbudgetservice.domain.usecase;


import it.valeriovaudi.familybudget.spentbudgetservice.domain.processor.DataImporter;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.BudgetExpenseRepository;

import java.io.InputStream;

public class LoadSpentBudget {

    private final BudgetExpenseRepository budgetExpenseRepository;
    private final DataImporter dataImporter;

    public LoadSpentBudget(BudgetExpenseRepository budgetExpenseRepository,
                           DataImporter dataImporter) {
        this.budgetExpenseRepository = budgetExpenseRepository;
        this.dataImporter = dataImporter;
    }

    public void importFrom(InputStream inputStream) {
        dataImporter.loadData(inputStream).forEach(budgetExpenseRepository::save);
    }
}
