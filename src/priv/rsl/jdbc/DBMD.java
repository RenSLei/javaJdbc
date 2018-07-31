package priv.rsl.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/** 
* @ClassName: DBMD 
* @Description: TODO  
* Connection里有关于获取数据库的信息，是否支持jdbc某些规范等
* 
* @author rsl
* @date 2018年3月24日 
*  
*/
public class DBMD {

    public static void main(String[] args) throws SQLException {
	//建立连接才能获取
	Connection conn = JdbcUtils.getConnection();
	
	//关于数据库的整体综合信息。
	DatabaseMetaData dbmd = conn.getMetaData();
	System.out.println("dbmd name:"+dbmd.getDatabaseProductName());
	System.out.println("是否支持事务："+dbmd.supportsTransactions());
	System.out.println(dbmd.getDatabaseProductVersion());
	System.out.println(dbmd.getDefaultTransactionIsolation());
	System.out.println(dbmd.getDriverName());
	System.out.println(dbmd.getSQLKeywords());
	System.out.println(dbmd.supportsMultipleTransactions());
	conn.close();
	
    }

}
