package it.valeriovaudi.onlyoneportal.budgetservice.adapters.processor.excel.builder;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

public class CellBuilder {

    private final Row row;
    private int  cellNumber =  0;

    CellBuilder(Row row) {
        this.row = row;
    }

    public CellBuilder withCell(String content, CellStyle cellStyle) {
        Cell cell = row.createCell(cellNumber, CellType.STRING);
        cell.setCellValue(content);
        cell.setCellStyle(cellStyle);
        cellNumber++;

        return this;
    }

    public void build(){
        // insert for clean code
    }
}
