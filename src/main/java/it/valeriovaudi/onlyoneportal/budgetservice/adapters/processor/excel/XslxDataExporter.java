package it.valeriovaudi.onlyoneportal.budgetservice.adapters.processor.excel;


import it.valeriovaudi.onlyoneportal.budgetservice.adapters.processor.excel.factory.CellStyleFactory;
import it.valeriovaudi.onlyoneportal.budgetservice.adapters.processor.excel.factory.DailyBudgetExpenseRowFactory;
import it.valeriovaudi.onlyoneportal.budgetservice.adapters.processor.excel.factory.FooterFactory;
import it.valeriovaudi.onlyoneportal.budgetservice.adapters.processor.excel.factory.HeaderFactory;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.SpentBudget;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.processor.DataExporter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Slf4j
public class XslxDataExporter implements DataExporter {

    private final FooterFactory footerFactory;
    private final HeaderFactory headerFactory;
    private final DailyBudgetExpenseRowFactory dailyBudgetExpenseRowFactory;
    private final CellStyleFactory cellStyleFactory;

    public XslxDataExporter(FooterFactory footerFactory,
                            HeaderFactory headerFactory,
                            DailyBudgetExpenseRowFactory dailyBudgetExpenseRowFactory,
                            CellStyleFactory cellStyleFactory) {
        this.footerFactory = footerFactory;
        this.headerFactory = headerFactory;
        this.dailyBudgetExpenseRowFactory = dailyBudgetExpenseRowFactory;
        this.cellStyleFactory = cellStyleFactory;
    }

    @Override
    public InputStream exportData(List<BudgetExpense> budgetExpenseList) {
        InputStream inputStream = null;
        try {
            SpentBudget spentBudget = new SpentBudget(budgetExpenseList, null);
            Path xlsxPath = Files.createTempFile(UUID.randomUUID().toString(), ".xslx");
            try (OutputStream outputStream = Files.newOutputStream(xlsxPath);
                 XSSFWorkbook wb = new XSSFWorkbook()) {

                Sheet sheet = wb.createSheet();
                headerFactory.createHeader(sheet, cellStyleFactory.newHeaderRowStyle(wb));

                spentBudget.dailyBudgetExpenseList()
                        .forEach(dailyBudgetExpense ->
                                dailyBudgetExpenseRowFactory.createDailyRow(sheet, cellStyleFactory.newContentRowStyle(wb), dailyBudgetExpense));

                footerFactory.createFooter(sheet, spentBudget.total().stringifyAmount(), cellStyleFactory.newFooterRowStyle(wb));

                IntStream.range(0, 4).forEach(sheet::autoSizeColumn);

                wb.write(outputStream);
                inputStream = new FileInputStream(xlsxPath.toFile());
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return inputStream;
    }
}