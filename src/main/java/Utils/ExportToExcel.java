package Utils;

import Utils.GetAlias;
import Utils.JdbcUtils;
import com.alibaba.druid.util.JdbcConstants;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import javax.sql.DataSource;
import javax.sql.RowSet;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author awl
 * @date 2023/6/11 16:10
 */
public class ExportToExcel {

    String dbType = JdbcConstants.MYSQL;
    //String dbType_oracle = JdbcConstants.ORACLE;

    public void genExcel(String filename, Map<String,String> sheetsMap,Statement statement) throws SQLException, IOException {

        /*//step1 获取数据库连接池对象
        DataSource dataSource = JdbcUtils.getDataSource();
        //step2 从数据库连接池对象中获取数据库连接对象
        Connection connection = dataSource.getConnection();
        //step3
        Statement statement = connection.createStatement();*/

        //step4 创建Excel文件
        XSSFWorkbook workbook = new XSSFWorkbook();
        //step4
        Set<String> sheets = sheetsMap.keySet();
        for (String sheetName : sheets) {
            String sqlValue = sheetsMap.get(sheetName);
            ResultSet resultSet = statement.executeQuery(sqlValue);
            XSSFSheet sheet = workbook.createSheet(sheetName);
            // 写入数据
            int rowNum = 0;
            Row row = sheet.createRow(rowNum++);
            Cell cell = null;
            //设置表头格式
            XSSFCellStyle cellNameStyle = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setFontName("微软雅黑");
            font.setFontHeightInPoints((short) 12);
            font.setBold(true);
            cellNameStyle.setFont(font);
            cellNameStyle.setAlignment(HorizontalAlignment.CENTER);//左右居中
            cellNameStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellNameStyle.setWrapText(true);
            cellNameStyle.setBorderBottom(BorderStyle.THIN);
            cellNameStyle.setBorderLeft(BorderStyle.THIN);
            cellNameStyle.setBorderRight(BorderStyle.THIN);
            cellNameStyle.setBorderTop(BorderStyle.THIN);
            //写入表头
            List<String> alias = GetAlias.getAlias(sqlValue, dbType);
            for (int i = 0; i < alias.size(); i++) {
                cell = row.createCell(i);
                cell.setCellStyle(cellNameStyle);
                cell.setCellValue(alias.get(i));
            }
            //设置表体格式
            XSSFCellStyle cellReportStyle = workbook.createCellStyle();
            XSSFFont reportFont = workbook.createFont();
            reportFont.setFontName("微软雅黑");
            reportFont.setFontHeightInPoints((short) 12);
            cellReportStyle.setFont(reportFont);
            cellReportStyle.setAlignment(HorizontalAlignment.CENTER);//左右居中
            cellReportStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellReportStyle.setWrapText(true);
            cellReportStyle.setBorderBottom(BorderStyle.THIN);
            cellReportStyle.setBorderLeft(BorderStyle.THIN);
            cellReportStyle.setBorderRight(BorderStyle.THIN);
            cellReportStyle.setBorderTop(BorderStyle.THIN);
            //写入数据
            while (resultSet.next()) {
                row = sheet.createRow(rowNum++);
                for (int i = 0; i < alias.size(); i++) {
                    cell = row.createCell(i);
                    cell.setCellStyle(cellReportStyle);
                    cell.setCellValue(resultSet.getString(alias.get(i)));
                }
            }
            for (int k = 0; k < alias.size(); k++) {
                sheet.autoSizeColumn(k);
            }
            this.setSizeColumn(sheet,alias.size());
        }

        // 获取桌面路径
        FileSystemView fsv = FileSystemView.getFileSystemView();
        String desktop = fsv.getHomeDirectory().getPath();
        String filePath = desktop + "/" + filename;
        File file = new File(filePath);
        // 保存文件
        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);
        out.close();

        /*// 释放资源：PreparedStatement
        JdbcUtils.releaseResources(statement);
        // 归还连接
        JdbcUtils.releaseResources(connection);
        // 释放资源：数据库连接池
        JdbcUtils.releaseResources(dataSource);*/

    }

    // 自适应宽度(中文支持)
    private void setSizeColumn(XSSFSheet sheet, int size) {
        for (int columnNum = 0; columnNum < size; columnNum++) {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                XSSFRow currentRow;
                //当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }

                if (currentRow.getCell(columnNum) != null) {
                    XSSFCell currentCell = currentRow.getCell(columnNum);
                    if (currentCell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            sheet.setColumnWidth(columnNum, columnWidth * 256);
        }
    }

}
