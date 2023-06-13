import Utils.ExportToExcel;
import Utils.JdbcUtils;
import Utils.ParseXml;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;
import java.util.Set;

/**
 * @author awl
 * @date 2023/6/12 1:00
 */
public class Start {

    public static void main(String[] args) {

        try {
            //step1 获取数据库连接池对象
            DataSource dataSource = JdbcUtils.getDataSource();
            //step2 从数据库连接池对象中获取数据库连接对象
            Connection connection = dataSource.getConnection();
            //step3
            Statement statement = connection.createStatement();

            ParseXml parseXml = new ParseXml();
            ExportToExcel exportToExcel = new ExportToExcel();
            Map<String, Map<String, String>> xmlMap = null;
            xmlMap = parseXml.parseXML();
            Set<String> filesSet = xmlMap.keySet();
            for (String fileName : filesSet) {
                exportToExcel.genExcel(fileName,xmlMap.get(fileName),statement);
            }

            // 释放资源：PreparedStatement
            JdbcUtils.releaseResources(statement);
            // 归还连接
            JdbcUtils.releaseResources(connection);
            // 释放资源：数据库连接池
            JdbcUtils.releaseResources(dataSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
