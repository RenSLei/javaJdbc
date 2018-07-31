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
* 一个JDBC的工具类，用于封装注册驱动以及关闭资源(使用单例模式来设计)
* 所谓单例模式就是只有一个实例：
* 这里采用延迟加载，且加锁
* 注意static的变化
* 注意：所有的都使用java.sql.jdbc的接口
* @author rsl
* @date 2018年3月12日 
*  
*/
public final class JdbcUtilsSing {
    //定义私有的声明
    private String url = "jdbc:mysql://localhost:3306/mydb1?useSSL=false";
    private String user = "root";
    private String password = "root";
    private static JdbcUtilsSing instance = null;
    
    private JdbcUtilsSing() {
    }
    
    /** 
    * @Title: getInstance 
    * @Description: TODO  
    * 两次判断：
    * 1是为了不用每次都加锁再判断，这样使效率快
    * 2是防止两个null线程同时进入，第一个先实例化后，第二个若不判断，就会再次实例化
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
    
    //静态代码块在类一被加载就被执行了，所以类一加载就注册了驱动
    static {
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
	    throw new ExceptionInInitializerError();
	}
    }
    
    //在此处将建立连接，提供public的方法来被调用
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







