package com.xiaojihua.test;

import com.xiaojihua.utils.UtilFuns;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Date;

public class C06POITest {

    @Test
    public void test1() throws IOException {
        // 1、创建一个工作簿
        Workbook workbook = new HSSFWorkbook();
        // 2、创建一个sheet
        Sheet sheet = workbook.createSheet();
        // 3、创建行对象
        Row row = sheet.createRow(3);//从0开始
        // 4、创建单元格
        Cell cell = row.createCell(3);
        // 5、设置单元格内容
        cell.setCellValue("天行健君子自相不息");
        // 6、设置单元格样式
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short)28);//设置字体大小
        font.setFontName("华文行楷");
        cellStyle.setFont(font);

        cell.setCellStyle(cellStyle);

        // 7、保存、关闭流
        OutputStream out = new FileOutputStream("i:/出货表.xls");
        workbook.write(out);
        out.close();
    }

    @Test
    public void test2() throws IOException {
        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet();
        //设置列宽*256是一般写法，这个是有一个内部公式的但是相当于*256
        sheet.setColumnWidth(1, 25 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 25 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 10 * 256);

        Row row = sheet.createRow(0);
        row.setHeightInPoints(36);
        Cell cell = row.createCell(1);
        cell.setCellValue("2012年8月份出货表");



        //合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0,0,1,8));

        // 居中显示
        CellStyle style = book.createCellStyle();
        Font font = book.createFont();
        //设置字体样式
        // 加粗设置字体大小
        font.setBold(true);
        font.setFontHeightInPoints((short)16);
        font.setFontName("宋体");
        // 横向居中
        style.setAlignment(CellStyle.ALIGN_CENTER);
        // 纵向居中
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFont(font);

        cell.setCellStyle(style);


        book.write(new FileOutputStream("E:/itcast297.xls"));
        book.close();
    }

    @Test
    public void test3() throws ParseException {
        String s = UtilFuns.dateTimeFormat(new Date(), "yyyy-MM");
        System.out.println(s);
    }
}
