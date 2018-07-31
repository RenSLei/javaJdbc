package priv.rsl.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** 
* @ClassName: UpdateableTest 
* @Description: TODO  
* 1�����£�
* 	�˽�һ�£��ڲ�ѯ���Ľ���п��Զ�ԭʼ���ݽ��и������ݵĲ���������һ�㲻�������£���ʹҵ���߼������
* 	���󣺽�������wangwu�ĸ�Ϊlisi
* 
* 2��senstitive:
* 	��ͬ�����ݿ��Լ���ͬ�����������ᵼ�²���ʵ��jdbc����ȫ�Ĺ��ܹ淶����������û�в��Գ�����
* 	
* @author rsl
* @date 2018��3��24�� 
*  
*/
public class UpdateableTest {

    public static void main(String[] args) throws SQLException, InterruptedException {
	 update(); 
    }

    static void update() throws SQLException, InterruptedException {
	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;

	try {
	    // ��������
	    conn = JdbcUtils.getConnection();

	    // ָ���ɹ������ɸ��£�
	    st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

	    rs = st.executeQuery("select id,name,password,birthday from user where id < 5 ");

	    // update
//	    while (rs.next()) {
//		
//		System.out.println(rs.getInt("id") + " \t" + rs.getString("name") + " \t" + rs.getString("password")
//			+ " \t" + rs.getDate("birthday") + " \t");
//		String name = rs.getString("name");
//		if("wangwu".equals(name)) {
//		    rs.updateString("name", "lisi");
//		    rs.updateRow();
//		}
//	    }
//	    System.out.println("------------------------");
	    
	    //sensitive:һ�߲�ѯ��һ�������������޸����ݿ��е�ֵ���������ܲ��ܸ�֪��������
	    while (rs.next()) {
		System.out.println("show...."+rs.getInt("id")+"....");
		Thread.sleep(10000);
		System.out.println(rs.getInt("id") + " \t" + rs.getString("name") + " \t" + rs.getString("password")
			+ " \t" + rs.getDate("birthday") + " \t");
		}
	   
	} finally {
	    JdbcUtils.free(rs, st, conn);
	}

    }
}
