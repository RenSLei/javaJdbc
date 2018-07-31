package priv.rsl.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** 
* @ClassName: JdbcUtils 
* @Description: TODO 
* 
* һ��JDBC�Ĺ����࣬���ڷ�װע�������Լ��ر���Դ(ʹ�õ���ģʽ�����)
* ��ν����ģʽ����ֻ��һ��ʵ����
* ��������ӳټ��أ��Ҽ���
* ע��static�ı仯
* ע�⣺���еĶ�ʹ��java.sql.jdbc�Ľӿ�
* @author rsl
* @date 2018��3��12�� 
*  
*/
public final class JdbcUtilsSing {
    //����˽�е�����
    private String url = "jdbc:mysql://localhost:3306/mydb1?useSSL=false";
    private String user = "root";
    private String password = "root";
    private static JdbcUtilsSing instance = null;
    
    private JdbcUtilsSing() {
    }
    
    /** 
    * @Title: getInstance 
    * @Description: TODO  
    * �����жϣ�
    * 1��Ϊ�˲���ÿ�ζ��������жϣ�����ʹЧ�ʿ�
    * 2�Ƿ�ֹ����null�߳�ͬʱ���룬��һ����ʵ�����󣬵ڶ��������жϣ��ͻ��ٴ�ʵ����
    * @param 
    * @return JdbcUtilsSing
    * @throws 
    */
    public static JdbcUtilsSing getInstance() {
	if(instance == null) {
	    synchronized (JdbcUtilsSing.class) {
		if(instance == null) {
		    instance = new JdbcUtilsSing();
		}
	    }
	}
	return instance;
    }
    
    //��̬���������һ�����ؾͱ�ִ���ˣ�������һ���ؾ�ע��������
    static {
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
	    throw new ExceptionInInitializerError();
	}
    }
    
    //�ڴ˴����������ӣ��ṩpublic�ķ�����������
    public Connection getConnection() throws SQLException {
	return DriverManager.getConnection(url, user, password);
	
    }
    public  void free(ResultSet rs , Statement st , Connection conn) {
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
			conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    }
	    }
	}
    }
}







