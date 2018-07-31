package priv.rsl.jdbc;

import java.sql.Connection;
import java.sql.SQLException;


public class ConnectionPoolTest {

    /** 
    * @Title: main 
    * @Description: TODO  
    * �������ӳ�������ӵĸ���
    * 
    * �ӹ�����JdbcUtils���õ����ӣ��˴��õ������ӾͲ���һ������ӡ�
    * ����JdbcUtils.getConnection() <-- ����MyDataSource2�����getConnection()
    * <--����MyDataSource2���̳߳�ConnectionsPool <-- MyDataSource2�����createConnection()
    * <-- ͨ��DriverManager.getConnection()��ȡ���������ӣ�
    * ����MyConnection��MyConnectionHandlerȥ��װ����������������close()
    * 
    * 
    * ���Կ����������ͷ����ӣ����õ����Ӵ��������������������׳��쳣��
    com.mysql.jdbc.JDBC4Connection@7591083d
    com.mysql.jdbc.JDBC4Connection@77a567e1
    com.mysql.jdbc.JDBC4Connection@736e9adb
    com.mysql.jdbc.JDBC4Connection@6d21714c
    com.mysql.jdbc.JDBC4Connection@108c4c35
    com.mysql.jdbc.JDBC4Connection@4ccabbaa
    com.mysql.jdbc.JDBC4Connection@4bf558aa
    com.mysql.jdbc.JDBC4Connection@2d38eb89
    com.mysql.jdbc.JDBC4Connection@5fa7e7ff
    com.mysql.jdbc.JDBC4Connection@2d8e6db6
    com.mysql.jdbc.JDBC4Connection@619a5dff
    com.mysql.jdbc.JDBC4Connection@497470ed
    com.mysql.jdbc.JDBC4Connection@e2144e4
    com.mysql.jdbc.JDBC4Connection@18769467
    com.mysql.jdbc.JDBC4Connection@722c41f4
    com.mysql.jdbc.JDBC4Connection@22927a81
    com.mysql.jdbc.JDBC4Connection@50134894
    com.mysql.jdbc.JDBC4Connection@1b4fb997
    com.mysql.jdbc.JDBC4Connection@f2a0b8e
    java.sql.SQLException: û������
    	at priv.rsl.jdbc.datasource.MyDataSource.getConnection(MyDataSource.java:59)
    	at priv.rsl.jdbc.JdbcUtils.getConnection(JdbcUtils.java:40)
    	at priv.rsl.jdbc.ConnectionPoolTest.main(ConnectionPoolTest.java:19)
    	
    	������ÿһ�����ӵ�ʹ�ô�����3�Σ���
    priv.rsl.jdbc.datasource.MyConnection@42110406
    priv.rsl.jdbc.datasource.MyConnection@42110406
    priv.rsl.jdbc.datasource.MyConnection@42110406
    priv.rsl.jdbc.datasource.MyConnection@41906a77
    priv.rsl.jdbc.datasource.MyConnection@41906a77
    priv.rsl.jdbc.datasource.MyConnection@41906a77
    priv.rsl.jdbc.datasource.MyConnection@2cdf8d8a
    priv.rsl.jdbc.datasource.MyConnection@2cdf8d8a
    priv.rsl.jdbc.datasource.MyConnection@2cdf8d8a
    priv.rsl.jdbc.datasource.MyConnection@1698c449
    priv.rsl.jdbc.datasource.MyConnection@1698c449
    priv.rsl.jdbc.datasource.MyConnection@1698c449
    priv.rsl.jdbc.datasource.MyConnection@14514713
    priv.rsl.jdbc.datasource.MyConnection@14514713
    priv.rsl.jdbc.datasource.MyConnection@14514713
    * @param args
    */
    public static void main(String[] args) {
	
	try {
	    for (int i = 0; i < 15; i++) {
		 Connection conn = JdbcUtils.getConnection();
		 System.out.println(conn);
		 JdbcUtils.free(null, null, conn);
	    }
	   
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

}
