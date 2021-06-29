package it.valeriovaudi.onlyoneportal.budgetservice.adapters.processor.excel.factory;

import it.valeriovaudi.onlyoneportal.budgetservice.adapters.processor.excel.builder.RowBuilder;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;

public class HeaderFactory {

    private final RowBuilder rowBuilder;

    public HeaderFactory(RowBuilder rowBuilder) {
        this.rowBuilder = rowBuilder;
    }

    public void createHeader(Sheet sheet, CellStyle cellStyle) {
        rowBuilder.newHeaderRow(sheet)
                .withCell("Data", cellStyle)
                .withCell("Amount", cellStyle)
                .withCell("Note", cellStyle)
                .withCell("Tag", cellStyle)
                .withCell("Total", cellStyle)
                .build();
    }
}
