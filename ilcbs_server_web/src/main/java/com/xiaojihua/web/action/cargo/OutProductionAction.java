package com.xiaojihua.web.action.cargo;

import com.xiaojihua.domain.ContractProducts;
import com.xiaojihua.service.ContractProductService;
import com.xiaojihua.utils.DownloadUtil;
import com.xiaojihua.utils.UtilFuns;
import com.xiaojihua.web.action.BaseAction;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.sql.*;
import java.util.List;

@Namespace("/cargo")
public class OutProductionAction extends BaseAction {

    @Autowired
    private ContractProductService contractProductService;

    private String inputDate;

    public String getInputDate() {
        return inputDate;
    }


    public void setInputDate(String inputDate) {
        this.inputDate = inputDate;
    }


    @Action(value="outProductAction_toedit",results={@Result(name="toedit",location="/WEB-INF/pages/cargo/outproduct/jOutProduct.jsp")})
    public String toedit() throws Exception {
        // TODO Auto-generated method stub
        return "toedit";
    }

    /**
     * 导出80万条数据，成功，大概33秒
     * @return
     * @throws Exception
     */
    @Action(value="outProductAction_print")
    public String print() throws Exception {

        long  startTime = System.currentTimeMillis();	//开始时间
        System.out.println("strat execute time: " + startTime);
        //关键语句一使用jdbc不使用HIBERNATE减少对象创建
        ResultSet rs = getDate();

        //关键语句二，使用SXSSFWorkbook支持大数据量的excel创建，其原理可以查看pdf教程
        //使用xlsx，excel2007以后的，支持最大102万的数据
        Workbook book = new SXSSFWorkbook();
        Sheet sheet = book.createSheet();

        // 设置列宽
        sheet.setColumnWidth(1, 25 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 25 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 10 * 256);

        int rowIndex = 0;
        // 大标题
        Row bigTitleRow = sheet.createRow(rowIndex++);
        bigTitleRow.setHeightInPoints(36);
        Cell bigCell = bigTitleRow.createCell(1);
        bigCell.setCellStyle(bigTitle(book));
        // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0,0,1,8));
        // 标题内容  inputDate 2015年7月份出货表
        String titleStr = inputDate.replace("-0", "-").replace("-", "年")+"月份出货表";
        bigCell.setCellValue(titleStr);

        // 小标题
        String[] smartStrs = {"客户","订单号","货号","数量","工厂","工厂交期","船期","贸易条款"};
        Row smartRow = sheet.createRow(rowIndex++);
        smartRow.setHeightInPoints(26);
        for (int i=0; i < smartStrs.length ; i++) {
            Cell smartCell = smartRow.createCell(i+1);
            smartCell.setCellValue(smartStrs[i]); //设置内容
            smartCell.setCellStyle(title(book));  //设置样式
        }
        //关键语句三：公用cellStyle减少循环中创建的对象
        CellStyle cellStyle = text(book);
        while(rs.next()){
            for(int i=0; i<5000; i++){
                Row contentRow = sheet.createRow(rowIndex++);
                contentRow.setHeightInPoints(26);

                Cell cell01 = contentRow.createCell(1);
                cell01.setCellValue(rs.getString("custom_name"));
                cell01.setCellStyle(cellStyle);

                Cell cell02 = contentRow.createCell(2);
                cell02.setCellValue(rs.getString("contract_no"));
                cell02.setCellStyle(cellStyle);

                Cell cell03 = contentRow.createCell(3);
                cell03.setCellValue(rs.getString("product_no"));
                cell03.setCellStyle(cellStyle);

                Cell cell04 = contentRow.createCell(4);
                cell04.setCellValue(rs.getString("cnumber"));
                cell04.setCellStyle(cellStyle);

                Cell cell05 = contentRow.createCell(5);
                cell05.setCellValue(rs.getString("factory_name"));
                cell05.setCellStyle(cellStyle);

                Cell cell06 = contentRow.createCell(6);
                cell06.setCellValue(UtilFuns.dateTimeFormat(rs.getDate("delivery_period")));
                cell06.setCellStyle(cellStyle);

                Cell cell07 = contentRow.createCell(7);
                cell07.setCellValue(UtilFuns.dateTimeFormat(rs.getDate("ship_time")));
                cell07.setCellStyle(cellStyle);

                Cell cell08 = contentRow.createCell(8);
                cell08.setCellValue(rs.getString("trade_terms"));
                cell08.setCellStyle(cellStyle);
            }
            //关键语句四：平衡一下cpu和io的时间，让sxsshfworkbook的机制能充分发挥
            Thread.sleep(1);			//休息一下，防止对CPU占用

        }

        // 写出excel
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HttpServletResponse response = ServletActionContext.getResponse();

        String returnName = titleStr + ".xlsx";
        response.setContentType("application/octet-stream;charset=utf-8");
        returnName = response.encodeURL(new String(returnName.getBytes(),"iso8859-1"));
        response.addHeader("Content-Disposition",   "attachment;filename=" + returnName);
        ServletOutputStream outputStream2 = response.getOutputStream();

        book.write(outputStream2);
        book.close();


        long finishedTime = System.currentTimeMillis();	//处理完成时间
        System.out.println("finished execute  time: " + (finishedTime - startTime)/1000 + "s");

        return NONE;
    }

    private ResultSet getDate() throws Exception{
        //设置数据库链接，使用JDBC
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String url = "jdbc:oracle:thin:@192.168.237.128:1521:orcl";
        String userName = "itcast297";
        String password = "itcast297";
        Connection connection = DriverManager.getConnection(url, userName, password);

        String sql = "select c.custom_name,c.contract_no,cp.product_no,cp.cnumber,cp.factory_name,c.delivery_period,c.ship_time,c.trade_terms from contract_product_c cp left join contract_c c on cp.contract_id = c.contract_id where to_char(c.ship_time,'yyyy-MM')=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setObject(1,inputDate);
        ResultSet resultSet = ps.executeQuery();

        return resultSet;
    }

    /**
     * 测试导出80w条数据--长时间导不出来
     * @return
     * @throws Exception
     */
    @Action(value="outProductAction_bigData_print")
    public String bigData_print() throws Exception {
        // TODO Auto-generated method stub
        Workbook book = new SXSSFWorkbook();
        Sheet sheet = book.createSheet();

        // 设置列宽
        sheet.setColumnWidth(1, 25 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 25 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 10 * 256);

        int rowIndex = 0;
        // 大标题
        Row bigTitleRow = sheet.createRow(rowIndex++);
        bigTitleRow.setHeightInPoints(36);
        Cell bigCell = bigTitleRow.createCell(1);
        bigCell.setCellStyle(bigTitle(book));
        // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0,0,1,8));
        // 标题内容  inputDate 2015年7月份出货表
        String titleStr = inputDate.replace("-0", "-").replace("-", "年")+"月份出货表";
        bigCell.setCellValue(titleStr);

        // 小标题
        String[] smartStrs = {"客户","订单号","货号","数量","工厂","工厂交期","船期","贸易条款"};
        Row smartRow = sheet.createRow(rowIndex++);
        smartRow.setHeightInPoints(26);
        for (int i=0; i < smartStrs.length ; i++) {
            Cell smartCell = smartRow.createCell(i+1);
            smartCell.setCellValue(smartStrs[i]); //设置内容
            smartCell.setCellStyle(title(book));  //设置样式
        }

        // 内容

        List<ContractProducts> cpList = contractProductService.findCpByShipTime(inputDate);
        System.out.println("======="+cpList.size());

        for (int i = 0; i < 5000; i++) {
            for (ContractProducts cp : cpList) {
                Row contentRow = sheet.createRow(rowIndex++);
                contentRow.setHeightInPoints(26);

                Cell cell01 = contentRow.createCell(1);
                cell01.setCellValue(cp.getContract().getCustomName());
                cell01.setCellStyle(text(book));

                Cell cell02 = contentRow.createCell(2);
                cell02.setCellValue(cp.getContract().getContractNo());
                cell02.setCellStyle(text(book));

                Cell cell03 = contentRow.createCell(3);
                cell03.setCellValue(cp.getProductNo());
                cell03.setCellStyle(text(book));

                Cell cell04 = contentRow.createCell(4);
                cell04.setCellValue(cp.getCnumber());
                cell04.setCellStyle(text(book));

                Cell cell05 = contentRow.createCell(5);
                cell05.setCellValue(cp.getFactoryName());
                cell05.setCellStyle(text(book));

                Cell cell06 = contentRow.createCell(6);
                cell06.setCellValue(UtilFuns.dateTimeFormat(cp.getContract().getDeliveryPeriod()));
                cell06.setCellStyle(text(book));

                Cell cell07 = contentRow.createCell(7);
                cell07.setCellValue(UtilFuns.dateTimeFormat(cp.getContract().getShipTime()));
                cell07.setCellStyle(text(book));

                Cell cell08 = contentRow.createCell(8);
                cell08.setCellValue(cp.getContract().getTradeTerms());
                cell08.setCellStyle(text(book));
            }
        }




        // 写出excel
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HttpServletResponse response = ServletActionContext.getResponse();
        String returnName = titleStr + ".xls";
        response.setContentType("application/octet-stream;charset=utf-8");
        returnName = response.encodeURL(new String(returnName.getBytes(),"iso8859-1"));
        response.addHeader("Content-Disposition",   "attachment;filename=" + returnName);
        ServletOutputStream outputStream2 = response.getOutputStream();

        book.write(outputStream2);
        book.close();

        return NONE;
    }

    /**
     * 使用模版的方式生成excel数据
     * 这样避免了设置繁杂的exeel样式
     * @return
     * @throws Exception
     */
    @Action(value="outProductAction_template_print")
    public String template_print() throws Exception {
        String realPath = ServletActionContext.getServletContext().getRealPath("/make/xlsprint/tOUTPRODUCT.xlsx");
        FileInputStream in = new FileInputStream(realPath);
        //根据excel模版路径创建workbook
        Workbook book = new XSSFWorkbook(in);
        Sheet sheet = book.getSheetAt(0);

        int rowIndex = 0;
        //设置大标题
        Row bigTitleRow = sheet.getRow(rowIndex++);
        Cell bigCell = bigTitleRow.getCell(1);
        String titleStr = inputDate.replace("-0","-").replace("-","年")+"月份出货表";
        bigCell.setCellValue(titleStr);

        //设置小标题
        rowIndex++;

        //设置内容
        //获取模板中每个单元格的样式
        CellStyle cellStyle1 = sheet.getRow(rowIndex).getCell(1).getCellStyle();
        CellStyle cellStyle2 = sheet.getRow(rowIndex).getCell(2).getCellStyle();
        CellStyle cellStyle3 = sheet.getRow(rowIndex).getCell(3).getCellStyle();
        CellStyle cellStyle4 = sheet.getRow(rowIndex).getCell(4).getCellStyle();
        CellStyle cellStyle5 = sheet.getRow(rowIndex).getCell(5).getCellStyle();
        CellStyle cellStyle6 = sheet.getRow(rowIndex).getCell(6).getCellStyle();
        CellStyle cellStyle7 = sheet.getRow(rowIndex).getCell(7).getCellStyle();
        CellStyle cellStyle8 = sheet.getRow(rowIndex).getCell(8).getCellStyle();

        //题外话：获取单元格内容
        String cellValue = sheet.getRow(rowIndex).getCell(1).getStringCellValue();
        System.out.println("获取到的模版内容行的第一个单元格内容：" + cellValue);

        List<ContractProducts> cpList = contractProductService.findCpByShipTime(inputDate);
        for(ContractProducts p : cpList){
            Row contentRow = sheet.createRow(rowIndex++);
            contentRow.setHeightInPoints(26);

            Cell cell01 = contentRow.createCell(1);
            cell01.setCellValue(p.getContract().getCustomName());
            cell01.setCellStyle(cellStyle1);

            Cell cell02 = contentRow.createCell(2);
            cell02.setCellValue(p.getContract().getContractNo());
            cell02.setCellStyle(cellStyle2);

            Cell cell03= contentRow.createCell(3);
            cell03.setCellValue(p.getProductNo());
            cell03.setCellStyle(cellStyle3);

            Cell cell04 = contentRow.createCell(4);
            cell04.setCellValue(p.getCnumber());
            cell04.setCellStyle(cellStyle4);

            Cell cell05 = contentRow.createCell(5);
            cell05.setCellValue(p.getFactoryName());
            cell05.setCellStyle(cellStyle5);

            Cell cell06 = contentRow.createCell(6);
            cell06.setCellValue(UtilFuns.dateTimeFormat(p.getContract().getDeliveryPeriod()));
            cell06.setCellStyle(cellStyle6);

            Cell cell07 = contentRow.createCell(7);
            cell07.setCellValue(UtilFuns.dateTimeFormat(p.getContract().getShipTime()));
            cell07.setCellStyle(cellStyle7);

            Cell cell08 = contentRow.createCell(8);
            cell08.setCellValue(p.getContract().getTradeTerms());
            cell08.setCellStyle(cellStyle8);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HttpServletResponse response = ServletActionContext.getResponse();
        book.write(os);
        DownloadUtil util = new DownloadUtil();
        util.download(os,response,titleStr + ".xlsx");

        return NONE;
    }

    /**
     * 手工通过poi设置excel以及数据，并且下载
     * @return
     * @throws Exception
     */
    @Action(value="outProductAction_oldprint")
    public String oldprint() throws Exception {
        // TODO Auto-generated method stub
        HSSFWorkbook book = new HSSFWorkbook();
        HSSFSheet sheet = book.createSheet();

        // 设置列宽
        sheet.setColumnWidth(1, 25 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 25 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 10 * 256);

        int rowIndex = 0;
        // 大标题
        HSSFRow bigTitleRow = sheet.createRow(rowIndex++);
        bigTitleRow.setHeightInPoints(36);
        HSSFCell bigCell = bigTitleRow.createCell(1);
        bigCell.setCellStyle(bigTitle(book));
        // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0,0,1,8));
        // 标题内容  inputDate 2015年7月份出货表
        String titleStr = inputDate.replace("-0", "-").replace("-", "年")+"月份出货表";
        bigCell.setCellValue(titleStr);

        // 小标题
        String[] smartStrs = {"客户","订单号","货号","数量","工厂","工厂交期","船期","贸易条款"};
        HSSFRow smartRow = sheet.createRow(rowIndex++);
        smartRow.setHeightInPoints(26);
        for (int i=0; i < smartStrs.length ; i++) {
            HSSFCell smartCell = smartRow.createCell(i+1);
            smartCell.setCellValue(smartStrs[i]); //设置内容
            smartCell.setCellStyle(title(book));  //设置样式
        }

        // 内容
        List<ContractProducts> cpList = contractProductService.findCpByShipTime(inputDate);
        System.out.println("======="+cpList.size());

        /*
        *   这里有个问题原来教程中是在每个Cell的setCellStyle方法中都调用了一下text(book)方法，
        *   这样做会导致导出的excel打不开，提示文件损坏或者是此文件中的某些文本格式可能已经更改，
        *   因为它已经超出最多允许的字体数。关闭其他文档再试一次可能有用。
        *   这是因为每次循环的时候到创建了样式对象和Font对象导致
        *   将其提到for循环外边，作为公用变量进行使用，就可以了
        * */
        CellStyle cellStyle = text(book);
        for(ContractProducts product : cpList){
            HSSFRow contentRow = sheet.createRow(rowIndex++);
            contentRow.setHeightInPoints(26);

            HSSFCell cell01 = contentRow.createCell(1);
            cell01.setCellValue(product.getContract().getCustomName());
            cell01.setCellStyle(cellStyle);

            HSSFCell cell02 = contentRow.createCell(2);
            cell02.setCellValue(product.getContract().getContractNo());
            cell02.setCellStyle(cellStyle);

            HSSFCell cell03 = contentRow.createCell(3);
            cell03.setCellValue(product.getProductNo());
            cell03.setCellStyle(cellStyle);

            HSSFCell cell04 = contentRow.createCell(4);
            cell04.setCellValue(product.getCnumber());
            cell04.setCellStyle(cellStyle);

            HSSFCell cell05 = contentRow.createCell(5);
            cell05.setCellValue(product.getFactoryName());
            cell05.setCellStyle(cellStyle);

            HSSFCell cell06 = contentRow.createCell(6);
            cell06.setCellValue(UtilFuns.dateTimeFormat(product.getContract().getDeliveryPeriod()));
            cell06.setCellStyle(cellStyle);

            HSSFCell cell07 = contentRow.createCell(7);
            cell07.setCellValue(UtilFuns.dateTimeFormat(product.getContract().getShipTime()));
            cell07.setCellStyle(cellStyle);

            HSSFCell cell08 = contentRow.createCell(8);
            cell08.setCellValue(product.getContract().getTradeTerms());
            cell08.setCellStyle(cellStyle);


        }

        // 写出excel
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HttpServletResponse response = ServletActionContext.getResponse();

        DownloadUtil util = new DownloadUtil();
        book.write(os);
        util.download(os, response, titleStr + ".xls");

        return NONE;
    }


    //大标题的样式
    public CellStyle bigTitle(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)16);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);					//字体加粗

        style.setFont(font);

        style.setAlignment(CellStyle.ALIGN_CENTER);					//横向居中
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);		//纵向居中

        return style;
    }
    //小标题的样式
    public CellStyle title(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short)12);

        style.setFont(font);

        style.setAlignment(CellStyle.ALIGN_CENTER);					//横向居中
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);		//纵向居中

        style.setBorderTop(CellStyle.BORDER_THIN);					//上细线
        style.setBorderBottom(CellStyle.BORDER_THIN);				//下细线
        style.setBorderLeft(CellStyle.BORDER_THIN);					//左细线
        style.setBorderRight(CellStyle.BORDER_THIN);				//右细线

        return style;
    }

    //文字样式
    public CellStyle text(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short)10);

        style.setFont(font);

        style.setAlignment(CellStyle.ALIGN_LEFT);					//横向居左
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);		//纵向居中

        style.setBorderTop(CellStyle.BORDER_THIN);					//上细线
        style.setBorderBottom(CellStyle.BORDER_THIN);				//下细线
        style.setBorderLeft(CellStyle.BORDER_THIN);					//左细线
        style.setBorderRight(CellStyle.BORDER_THIN);				//右细线*/

        return style;
    }

}
