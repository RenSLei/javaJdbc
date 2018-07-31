package priv.rsl.jdbc;

import java.sql.Connection;
import java.sql.SQLException;


public class ConnectionPoolTest {

    /** 
    * @Title: main 
    * @Description: TODO  
    * 测试连接池里的连接的复用
    * 
    * 从工具类JdbcUtils里拿到连接：此处拿到的连接就不是一般的连接。
    * 首先JdbcUtils.getConnection() <-- 调用MyDataSource2里面的getConnection()
    * <--调用MyDataSource2的线程池ConnectionsPool <-- MyDataSource2里面的createConnection()
    * <-- 通过DriverManager.getConnection()获取真正的连接，
    * 再用MyConnection或MyConnectionHandler去包装真正的连接以拦截close()
    * 
    * 
    * 可以看到，当不释放连接，则当拿的连接大于了最大连接数，则会抛出异常：
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
    java.sql.SQLException: 没有连接
    	at priv.rsl.jdbc.datasource.MyDataSource.getConnection(MyDataSource.java:59)
    	at priv.rsl.jdbc.JdbcUtils.getConnection(JdbcUtils.java:40)
    	at priv.rsl.jdbc.ConnectionPoolTest.main(ConnectionPoolTest.java:19)
    	
    	当限制每一个连接的使用次数（3次）后：
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
