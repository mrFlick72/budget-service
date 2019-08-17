package it.valeriovaudi.familybudget.budgetservice.adapters.processor.excel.factory;

import it.valeriovaudi.familybudget.budgetservice.adapters.processor.excel.builder.RowBuilder;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.DailyBudgetExpense;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.SearchTagRepository;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.util.StringUtils;

import static java.util.Optional.ofNullable;

public class DailyBudgetExpenseRowFactory {

    private final RowBuilder rowBuilder;
    private final SearchTagRepository searchTagRepository;

    public DailyBudgetExpenseRowFactory(RowBuilder rowBuilder, SearchTagRepository searchTagRepository) {
        this.rowBuilder = rowBuilder;
        this.searchTagRepository = searchTagRepository;
    }

    public void createDailyRow(Sheet sheet, CellStyle cellStyle, DailyBudgetExpense dailyBudgetExpense) {
        rowBuilder.newRow(sheet)
                .withCell(dailyBudgetExpense.getDate().formattedDate(), cellStyle)
                .withCell("", cellStyle)
                .withCell("", cellStyle)
                .withCell("", cellStyle)
                .withCell(dailyBudgetExpense.getTotal().stringifyAmount(), cellStyle)
                .build();

        dailyBudgetExpense.getBudgetExpenseList().forEach(budgetExpense -> {
            int columnNewLineCounter = StringUtils.countOccurrencesOf(budgetExpense.getNote(), "\n");
            rowBuilder.newRow(sheet, sheet.getDefaultRowHeightInPoints() * columnNewLineCounter)
                    .withCell("", cellStyle)
                    .withCell(budgetExpense.getAmount().stringifyAmount(), cellStyle)
                    .withCell(ofNullable(budgetExpense.getNote()).orElse(""), cellStyle)
                    .withCell(searchTagRepository.findSearchTagBy(budgetExpense.getTag()).getValue(), cellStyle)
                    .withCell("", cellStyle)
                    .build();
        });


    }
}
