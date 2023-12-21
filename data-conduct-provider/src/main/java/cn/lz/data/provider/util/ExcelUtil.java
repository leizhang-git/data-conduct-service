package cn.lz.data.provider.util;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

/**
 * @Author zhanglei
 * @Date 2023/12/21 14:32
 * @Desc apache poi 操作Excel
 */
public class ExcelUtil {

    //sheet名称
    public final static String SHEET_NAME = "default";

    //全局默认宽度
    public final static Integer DEFAULT_WIDTH = 25;

    //全局默认高度
    public final static short DEFAULT_HEIGHT = 450;

    //导出表名称
    public final static String TABLE_NAME = "template";

    /**
     * 导出Excel(xlsx格式)
     * @param type
     * @param response
     * @return
     */
    public static boolean exportExcel(Object obj, String type, HttpServletResponse response) {
        XSSFWorkbook wb = new XSSFWorkbook();
        //创建sheet
        XSSFSheet sheet = wb.createSheet(SHEET_NAME);
        //设置全局默认宽高
        sheet.setDefaultColumnWidth(DEFAULT_WIDTH);
        sheet.setDefaultRowHeight(DEFAULT_HEIGHT);

        //创建单元格字体
        XSSFFont headerFont = wb.createFont();
        //设置字体加粗
        headerFont.setBold(true);

        //==========================Style处理
        //黄色单元格部分
        DefaultIndexedColorMap yellowColorMap = new DefaultIndexedColorMap();
        XSSFCellStyle yellowStyle = wb.createCellStyle();
        //设置颜色
        XSSFColor yellowColor = new XSSFColor(new java.awt.Color(255, 192, 0), yellowColorMap);
        yellowStyle.setFillForegroundColor(yellowColor);
        //填充
        yellowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //字体
        yellowStyle.setFont(headerFont);
        //居中对齐
        yellowStyle.setAlignment(HorizontalAlignment.CENTER);
        //垂直对其
        yellowStyle.setVerticalAlignment(VerticalAlignment.TOP);
        //加锁
        yellowStyle.setLocked(true);

        DefaultIndexedColorMap yellow2ColorMap = new DefaultIndexedColorMap();
        XSSFCellStyle yellow2Style = wb.createCellStyle();
        XSSFColor yellow2Color = new XSSFColor(new java.awt.Color(255, 255, 153), yellow2ColorMap);
        yellow2Style.setFillForegroundColor(yellow2Color);
        yellow2Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        yellow2Style.setAlignment(HorizontalAlignment.CENTER);
        yellow2Style.setVerticalAlignment(VerticalAlignment.TOP);
        yellow2Style.setLocked(true);

        DefaultIndexedColorMap blueColorMap = new DefaultIndexedColorMap();
        XSSFCellStyle blueStyle = wb.createCellStyle();
        XSSFColor blueColor = new XSSFColor(new java.awt.Color(0, 176, 240), blueColorMap);
        blueStyle.setFillForegroundColor(blueColor);
        blueStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        blueStyle.setAlignment(HorizontalAlignment.CENTER);
        blueStyle.setVerticalAlignment(VerticalAlignment.TOP);
        blueStyle.setFont(headerFont);
        blueStyle.setLocked(true);
        //==========================锁定处理
        XSSFCellStyle unLockStyle = wb.createCellStyle();
        unLockStyle.setVerticalAlignment(VerticalAlignment.TOP);
        unLockStyle.setLocked(false);


        //密码
        sheet.protectSheet("ttttt");
        try (ServletOutputStream outputStream = response.getOutputStream()){
            response.setContentType("application/x-download");
            if("xlsx".equalsIgnoreCase(type)) {
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            }else {
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
            }
            response.setHeader("Content-Disposition","attachment;filename="+ URLDecoder.decode(TABLE_NAME,"UTF-8") + "." + type);
            wb.write(outputStream);
        }catch (Exception e) {
            return false;
        }
        return true;
    }
}
