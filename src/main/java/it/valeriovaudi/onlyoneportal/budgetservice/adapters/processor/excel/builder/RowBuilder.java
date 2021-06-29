package it.valeriovaudi.onlyoneportal.budgetservice.adapters.processor.excel.builder;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class RowBuilder {

    public CellBuilder newHeaderRow(Sheet sheet) {
        return new CellBuilder(sheet.createRow(sheet.getFirstRowNum()));
    }

    public CellBuilder newRow(Sheet sheet) {
        return new CellBuilder(sheet.createRow(getLastRowNum(sheet)));
    }

    public CellBuilder newRow(Sheet sheet, float heightInPoints) {
        Row row = sheet.createRow(getLastRowNum(sheet));
        row.setHeightInPoints(heightInPoints);
        return new CellBuilder(row);
    }

    private int getLastRowNum(Sheet sheet) {
        int lastRowNum = sheet.getLastRowNum();
        lastRowNum++;
        return lastRowNum;
    }
}
