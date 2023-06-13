package Utils;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelect;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.alibaba.druid.util.JdbcConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author awl
 * @date 2023/6/11 17:08
 */
public class GetAlias {
    String dbType = JdbcConstants.MYSQL;

    public static List<String> getAlias(String sql, String dbType){

        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        SQLStatement stmt = stmtList.get(0);
        List<String> aliasList = new ArrayList<>();

        Set<String> set = new HashSet<>();
        List<SQLSelectItem> list = null;
        if (JdbcConstants.MYSQL == dbType){
            list = ((MySqlSelectQueryBlock)((SQLSelect)((SQLSelectStatement)stmt).getSelect()).getQuery()).getSelectList();
        } else if (JdbcConstants.ORACLE == dbType){
            list = ((OracleSelectQueryBlock)((SQLSelect)((SQLSelectStatement)stmt).getSelect()).getQuery()).getSelectList();
        }
        for(SQLSelectItem item : list){
            String clomn = item.getAlias();
            aliasList.add(item.getAlias());
        }
        return aliasList;
    }
}
