package priv.rsl.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import priv.rsl.jdbc.datasource.MyDataSource2;

/** 
* @ClassName: JdbcUtils 
* @Description: TODO 
* 
* һ��JDBC�Ĺ����࣬���ڷ�װע�������Լ��ر���Դ(ʹ�þ�̬�����;�̬������д)
* 
* �����ඨ��Ϊfinal���ͽ�ֹ�̳�
* ������ʵ���������췽��˽��
* �ṩpublic�ľ�̬��������
* ע�⣺���еĶ�ʹ��java.sql.jdbc�Ľӿ�
* @author rsl
* @date 2018��3��12�� 
*  
*/
public final class JdbcUtils {
    
    private static MyDataSource2 myDataSource = null;
    
    private JdbcUtils() {
	
    }
    //��̬���������һ�����ؾͱ�ִ���ˣ�������һ���ؾ�ע��������
    static {
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    myDataSource = new MyDataSource2();
	} catch (ClassNotFoundException e) {
	    throw new ExceptionInInitializerError();
	}
    }
    
    //�ڴ˴����������ӣ��ṩpublic static �ķ�����������
    public static Connection getConnection() throws SQLException {
	return myDataSource.getConnection();
    }
    
    public static MyDataSource2 getDataSource() {
	return myDataSource;
    }
    public static void free(ResultSet rs , Statement st , Connection conn) {
	try {
	    if (rs != null)
		rs.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (st != null)
		    st.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		if (conn != null)
		    try {
			conn.close();//ʹ���˴���ģʽ������������free����close���ǽ����ӷ������ӳ�
			//myDataSource.free(conn);
		    } catch (Exception e) {
			e.printStackTrace();
		    }
	    }
	}
    }
}







