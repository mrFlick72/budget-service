package it.valeriovaudi.onlyoneportal.budgetservice.adapters.processor.excel.factory;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CellStyleFactory {

    public static final XSSFColor HEADER_COLOR = new XSSFColor(new java.awt.Color(200, 235, 252));
    public static final XSSFColor FOOTER_COLOR = new XSSFColor(new java.awt.Color(246, 254, 246));
    public static final XSSFColor ROW_COLOR = new XSSFColor(new java.awt.Color(253, 253, 248));

    public CellStyle newStyle(XSSFWorkbook wb, XSSFColor color) {
        XSSFFont font = wb.createFont();
        font.setFontHeight(12);

        XSSFCellStyle style = wb.createCellStyle();
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(color);
        style.setBorderBottom(BorderStyle.THIN);
        style.setFont(font);

        return style;
    }

    public CellStyle newHeaderRowStyle(XSSFWorkbook wb) {
        return newStyle(wb, HEADER_COLOR);
    }

    public CellStyle newContentRowStyle(XSSFWorkbook wb) {
        return newStyle(wb, ROW_COLOR);
    }

    public CellStyle newFooterRowStyle(XSSFWorkbook wb) {
        return newStyle(wb, FOOTER_COLOR);
    }
}
