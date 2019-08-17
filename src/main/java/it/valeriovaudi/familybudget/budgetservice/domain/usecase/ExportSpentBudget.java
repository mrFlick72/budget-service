package it.valeriovaudi.familybudget.budgetservice.domain.usecase;

import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Month;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Year;
import it.valeriovaudi.familybudget.budgetservice.domain.processor.DataExporter;

import java.io.InputStream;
import java.util.Collections;

public class ExportSpentBudget {

    private final FindSpentBudget findSpentBudget;
    private final DataExporter dataExporter;

    public ExportSpentBudget(FindSpentBudget findSpentBudget, DataExporter dataExporter) {
        this.findSpentBudget = findSpentBudget;
        this.dataExporter = dataExporter;
    }

    public InputStream exportFrom(Year year, Month month) {
        return findSpentBudget.findBy(month, year, Collections.emptyList()).printBudgetExpenseList(dataExporter);
    }
}
