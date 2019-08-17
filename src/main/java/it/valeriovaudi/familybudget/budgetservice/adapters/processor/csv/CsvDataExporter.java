package it.valeriovaudi.familybudget.budgetservice.adapters.processor.csv;

import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.budgetservice.domain.processor.DataExporter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Slf4j
public class CsvDataExporter implements DataExporter {

    private final String separator;

    public CsvDataExporter(String separator) {
        this.separator = separator;
    }

    @Override
    public InputStream exportData(List<BudgetExpense> budgetExpenseList) {
        InputStream inputStream = null;
        try {
            Path csv = Files.createTempFile(UUID.randomUUID().toString(), ".data");
            try (OutputStream outputStream = Files.newOutputStream(csv);
                 PrintWriter printWriter = new PrintWriter(outputStream)) {

                int count = 0;
                List<String> formatFileRaw = formatFileRaw(budgetExpenseList);
                for (String line : formatFileRaw) {
                    if (count == formatFileRaw.size() - 1) {
                        printWriter.print(line);
                    } else {
                        printWriter.println(line);
                    }
                    count++;
                }
                inputStream = new FileInputStream(csv.toFile());
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return inputStream;
    }

    List<String> formatFileRaw(List<BudgetExpense> budgetExpenseList) {
        return budgetExpenseList.stream()
                .map(budgetExpense ->
                        budgetExpense.getDate().formattedDate() +
                                separator +
                                budgetExpense.getUserName().getContent() +
                                separator +
                                budgetExpense.getAmount().stringifyAmount() +
                                separator +
                                Optional.ofNullable(budgetExpense.getNote()).orElse("")
                                        .replaceAll("\n", "__\\\\n__")
                                        .replaceAll("\r", "__\\\\r__") +
                                separator +
                                budgetExpense.getTag())
                .collect(toList());
    }
}
