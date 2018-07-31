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
* 一个JDBC的工具类，用于封装注册驱动以及关闭资源(使用静态代码块和静态方法来写)
* 
* 工具类定义为final类型禁止继承
* 不允许实例化：构造方法私有
* 提供public的静态块来调用
* 注意：所有的都使用java.sql.jdbc的接口
* @author rsl
* @date 2018年3月12日 
*  
*/
public final class JdbcUtils {
    
    private static MyDataSource2 myDataSource = null;
    
    private JdbcUtils() {
	
    }
    //静态代码块在类一被加载就被执行了，所以类一加载就注册了驱动
    static {
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    myDataSource = new MyDataSource2();
	} catch (ClassNotFoundException e) {
	    throw new ExceptionInInitializerError();
	}
    }
    
    //在此处将建立连接，提供public static 的方法来被调用
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
			conn.close();//使用了代理模式，所以无论是free还是close都是将连接放入连接池
			//myDataSource.free(conn);
		    } catch (Exception e) {
			e.printStackTrace();
		    }
	    }
	}
    }
}







