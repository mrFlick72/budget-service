package it.valeriovaudi.onlyoneportal.budgetservice.adapters.processor.pdf;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.DailyBudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.SpentBudget;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.processor.DataExporter;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.SearchTagRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Slf4j
public class PdfDataExporter implements DataExporter {

    public static final Color HEADER_COLOR = new DeviceRgb(200, 235, 252);
    public static final Color FOOTER_COLOR = new DeviceRgb(246, 254, 246);
    public static final Color ROW_COLOR = new DeviceRgb(253, 253, 248);

    private final SearchTagRepository searchTagRepository;

    public PdfDataExporter(SearchTagRepository searchTagRepository) {
        this.searchTagRepository = searchTagRepository;
    }

    @Override
    public InputStream exportData(List<BudgetExpense> budgetExpenseList) {
        InputStream inputStream = null;
        try {
            SpentBudget spentBudget = new SpentBudget(budgetExpenseList, null);
            Path pdfPath = Files.createTempFile(UUID.randomUUID().toString(), ".pdf");
            try (OutputStream outputStream = Files.newOutputStream(pdfPath);
                 PdfWriter writer = new PdfWriter(outputStream);
                 PdfDocument pdf = new PdfDocument(writer);
                 Document document = new Document(pdf)) {

                document.setFontSize(14);

                List<DailyBudgetExpense> dailyBudgetExpenses = spentBudget.dailyBudgetExpenseList();
                Paragraph header = new Paragraph(String.format("BudgetRevenue Expense List from %s to %s",
                        dailyBudgetExpenses.get(0).getDate().formattedDate(),
                        dailyBudgetExpenses.get(dailyBudgetExpenses.size() - 1).getDate().formattedDate()));
                header.setHorizontalAlignment(HorizontalAlignment.CENTER).setBold();
                document.add(header);

                Table table = new Table(5).setWidth(PageSize.A4.getWidth()*0.80f).setAutoLayout();
                table.setHorizontalAlignment(HorizontalAlignment.CENTER);
                table.setPaddingRight(25);
                table.setMarginRight(25);

                table.addHeaderCell(newCell("Data", HEADER_COLOR))
                        .addHeaderCell(newCell("Amount", HEADER_COLOR))
                        .addHeaderCell(newCell("Note", HEADER_COLOR))
                        .addHeaderCell(newCell("Tag", HEADER_COLOR))
                        .addHeaderCell(newCell("Total", HEADER_COLOR));


                for (DailyBudgetExpense dailyBudgetExpense : spentBudget.dailyBudgetExpenseList()) {
                    table.addCell(newCell(dailyBudgetExpense.getDate().formattedDate(), ROW_COLOR))
                            .addCell(newCell("", ROW_COLOR))
                            .addCell(newCell("", ROW_COLOR))
                            .addCell(newCell("", ROW_COLOR))
                            .addCell(newCell(dailyBudgetExpense.getTotal().stringifyAmount(), ROW_COLOR))
                            .startNewRow();

                    for (BudgetExpense budgetExpense : dailyBudgetExpense.getBudgetExpenseList()) {
                        table.addCell(newCell("", ROW_COLOR))
                                .addCell(newCell(budgetExpense.getAmount().stringifyAmount(), ROW_COLOR))
                                .addCell(newCell(ofNullable(budgetExpense.getNote()).orElse(""), ROW_COLOR))
                                .addCell(newCell(searchTagRepository.findSearchTagBy(budgetExpense.getTag()).getValue(), ROW_COLOR))
                                .addCell(newCell("", ROW_COLOR))
                                .startNewRow();
                    }
                }

                table.addCell(newCell("Total:", FOOTER_COLOR))
                        .addCell(newCell("", FOOTER_COLOR))
                        .addCell(newCell("", FOOTER_COLOR))
                        .addCell(newCell("", FOOTER_COLOR))
                        .addCell(newCell(spentBudget.total().stringifyAmount(), FOOTER_COLOR));

                document.add(table);

                inputStream = new FileInputStream(pdfPath.toFile());
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return inputStream;
    }

    private Cell newCell(String content, Color color) {
        return new Cell().add(new Paragraph(content))
                .setBorder(Border.NO_BORDER)
                .setBorderBottom(new SolidBorder(1))
                .setBackgroundColor(color).setFontSize(10);
    }
}
