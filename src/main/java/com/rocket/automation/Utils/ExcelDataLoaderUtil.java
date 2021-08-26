package com.rocket.automation.Utils;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.rocket.automation.Utils.Constant.EXCEL_PATH;

public class ExcelDataLoaderUtil {
    @DataProvider(name = "DataFeed")
    public static Object[][] dataUtil() throws IOException {
        String dir = System.getProperty("user.dir");

        FileInputStream src = new FileInputStream(dir+EXCEL_PATH);
        XSSFWorkbook book = new XSSFWorkbook(src);
        XSSFSheet sheet = book.getSheetAt(0);
        int maxRow = sheet.getLastRowNum();
        int maxCellValue = sheet.getRow(0).getLastCellNum();
        // Initialize your array size depends on your no of row and columns
        Object[][] data = new Object[maxRow][maxCellValue];
        // Read no of rows
        for (int i = 1; i < maxRow + 1; i++) {
            XSSFRow row = sheet.getRow(i);
            //Read no of columns
            for (int j = 0; j < maxCellValue; j++) {
                // Our intension to store values like [0][1] so using
                // i-1 since i starts from i=1
                XSSFCell cell = row.getCell(j);
                int cellType = cell.getCellType();
                if (cellType == 0) {
                    data[i - 1][j] = cell.toString();
                } else if (cellType == 1) {
                    data[i - 1][j] = cell.toString();
                } else {
                    data[i - 1][j] = "No Data";
                }
            }
        }
        return data;
    }

    public List<String> getExcelValues(String fileName, String Option, int SheetNumber, int ColumnNumber) {
        List<String> Excel_Values = new ArrayList<>();
        try {
            String dir = System.getProperty("user.dir");
            int Sheet_Num = SheetNumber;
            FileInputStream fis = new FileInputStream(new File(dir + Constant.EXCEL_PATH + fileName));
            XSSFRow row;
            XSSFCell cell;
            HSSFRow row1;
            HSSFCell cell1;
            String fileExtensionName = fileName.substring(fileName.indexOf("."));
            if (fileExtensionName.equals(".xlsx")) {
                XSSFWorkbook Book = new XSSFWorkbook(fis);
                XSSFSheet Sheet = Book.getSheetAt(Sheet_Num);
                int RowCount = Sheet.getLastRowNum();
                switch (Option) {
                    case "Yes":
                        for (int i = 1; i < RowCount + 1; i++) {
                            row = Sheet.getRow(i);
                            cell = row.getCell(ColumnNumber);
                            Excel_Values.add(cell.getStringCellValue());
                        }
                        break;
                    case "No":
                        for (int i = 0; i < RowCount + 1; i++) {
                            row = Sheet.getRow(i);
                            cell = row.getCell(ColumnNumber);
                            Excel_Values.add(cell.getStringCellValue());
                        }
                        break;
                }
            } else if (fileExtensionName.equals(".xls")) {
                HSSFWorkbook Book = new HSSFWorkbook(fis);
                HSSFSheet Sheet = Book.getSheetAt(Sheet_Num);
                int RowCount = Sheet.getLastRowNum();
                switch (Option) {
                    case "Yes":
                        for (int i = 1; i < RowCount + 1; i++) {
                            row1 = Sheet.getRow(i);
                            cell1 = row1.getCell(ColumnNumber);
                            Excel_Values.add(cell1.getStringCellValue());
                        }
                        break;
                    case "No":
                        for (int i = 0; i < RowCount + 1; i++) {
                            row1 = Sheet.getRow(i);
                            cell1 = row1.getCell(ColumnNumber);
                            Excel_Values.add(cell1.getStringCellValue());
                        }
                        break;
                }
            }

        } catch (Exception e) {
            Assert.fail(e.getMessage());

        }
        return Excel_Values.stream().distinct().collect(Collectors.toList());
    }
}
