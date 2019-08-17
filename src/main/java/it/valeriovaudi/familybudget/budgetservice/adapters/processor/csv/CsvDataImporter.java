package it.valeriovaudi.familybudget.budgetservice.adapters.processor.csv;


import it.valeriovaudi.familybudget.budgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.budgetservice.domain.model.user.UserName;
import it.valeriovaudi.familybudget.budgetservice.domain.processor.DataImporter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpenseId.randomBudgetExpenseId;
import static java.util.stream.Collectors.toList;

@Slf4j
public class CsvDataImporter implements DataImporter {

    private static final int DATE_IN_CSV_FILE_POSITION = 0;
    private static final int USER_IN_CSV_FILE_POSITION = 1;
    private static final int MONEY_IN_CSV_FILE_POSITION = 2;
    private static final int NOTE_IN_CSV_FILE_POSITION = 3;
    private static final int TAG_IN_CSV_FILE_POSITION = 4;
    private final String separator;

    public CsvDataImporter(String separator) {
        this.separator = separator;
    }

    @Override
    public List<BudgetExpense> loadData(InputStream inputStream) {
        List<BudgetExpense> budgetExpenses = Collections.emptyList();
        try (InputStreamReader reader = new InputStreamReader(inputStream);
             BufferedReader buffer = new BufferedReader(reader)) {
            budgetExpenses = buffer.lines()
                    .map(line -> line.split(separator))
                    .map(splittedLine -> {
                        Date date;
                        UserName userName;
                        Money money;
                        String note, tag;
                        try {
                            date = Date.dateFor(splittedLine[DATE_IN_CSV_FILE_POSITION]);
                            userName = new UserName(splittedLine[USER_IN_CSV_FILE_POSITION]);
                            money = Money.moneyFor(splittedLine[MONEY_IN_CSV_FILE_POSITION]);
                            note = splittedLine[NOTE_IN_CSV_FILE_POSITION]
                                    .replaceAll("__\\\\n__", "\n")
                                    .replaceAll("__\\\\r__", "\r");
                            tag = splittedLine[TAG_IN_CSV_FILE_POSITION];
                        } catch (Exception e) {
                            log.error(String.format("parsing error on line: %s", Arrays.asList(splittedLine)));
                            throw new RuntimeException(e);
                        }
                        return new BudgetExpense(randomBudgetExpenseId(), userName, date, money, note, tag);
                    })
                    .collect(toList());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        log.debug(budgetExpenses.toString());
        return budgetExpenses;
    }
}
