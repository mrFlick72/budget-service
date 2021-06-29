package it.valeriovaudi.onlyoneportal.budgetservice.adapters.processor.excel.factory;

import it.valeriovaudi.onlyoneportal.budgetservice.adapters.processor.excel.builder.RowBuilder;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;

public class FooterFactory {

    private final RowBuilder rowBuilder;

    public FooterFactory(RowBuilder rowBuilder) {
        this.rowBuilder = rowBuilder;
    }

    public void createFooter(Sheet sheet, String total, CellStyle cellStyle) {
        rowBuilder.newRow(sheet)
                .withCell("Total:", cellStyle)
                .withCell("", cellStyle)
                .withCell("", cellStyle)
                .withCell("", cellStyle)
                .withCell(total, cellStyle)
                .build();
    }
}
