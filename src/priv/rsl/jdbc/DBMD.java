package priv.rsl.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/** 
* @ClassName: DBMD 
* @Description: TODO  
* Connection���й��ڻ�ȡ���ݿ����Ϣ���Ƿ�֧��jdbcĳЩ�淶��
* 
* @author rsl
* @date 2018��3��24�� 
*  
*/
public class DBMD {

    public static void main(String[] args) throws SQLException {
	//�������Ӳ��ܻ�ȡ
	Connection conn = JdbcUtils.getConnection();
	
	//�������ݿ�������ۺ���Ϣ��
	DatabaseMetaData dbmd = conn.getMetaData();
	System.out.println("dbmd name:"+dbmd.getDatabaseProductName());
	System.out.println("�Ƿ�֧������"+dbmd.supportsTransactions());
	System.out.println(dbmd.getDatabaseProductVersion());
	System.out.println(dbmd.getDefaultTransactionIsolation());
	System.out.println(dbmd.getDriverName());
	System.out.println(dbmd.getSQLKeywords());
	System.out.println(dbmd.supportsMultipleTransactions());
	conn.close();
	
    }

}
