package it.valeriovaudi.familybudget.budgetservice.domain.usecase;


import it.valeriovaudi.familybudget.budgetservice.domain.processor.DataImporter;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.BudgetExpenseRepository;

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
