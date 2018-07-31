package priv.rsl.jdbc.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/** 
* @ClassName: MyDataSource 
* @Description: TODO  
* 用于创建连接池的类，该类一初始化就创建多个连接放在集合中，对外提供获取连接的方法，以及将将连接放回连接池的方法
* 工程细节的优化：
* 1、将连接池加上同步，保证多个线程拿不同的连接
* 2、设置最大连接数，保护数据库
*  
* @author rsl
* @date 2018年3月28日 
*  
*/
public class MyDataSource {
    //将url、user、password封装到该类
    private static String url = "jdbc:mysql://localhost:3306/mydb1?useSSL=false";
    private static String user = "root";
    private static String password = "root";
    
    private static int initCount = 10;
    private static int maxCount = 20;
    private int currentCount = 0;
    
    
    //创建一个集合，用于存放连接
    private LinkedList<Connection> connectionsPool = new LinkedList<Connection>();
    
    /** 
    * <p>Title: 构造函数
    * <p>Description: 将创建initCount个连接，放进连接池中 
    */
    public MyDataSource() {
	try {
	    for (int i = 0; i < initCount; i++) {
		this.connectionsPool.addLast(this.createConnection());
		currentCount++;
	    }
	} catch (SQLException e) {
	    throw new ExceptionInInitializerError();
	}
    }

    /** 
    * @Title: createConnection 
    * @Description: TODO  
    * 创建连接
    * @return
    * @throws SQLException
    */
    private Connection createConnection() throws SQLException {
	return DriverManager.getConnection(url, user, password);
    }
    
    /** 
    * @Title: getConnection 
    * @Description: TODO  
    * 获取连接。该方法对线程加锁，保证线程拿到的连接唯一，并限制最大连接数
    * @return
    * @throws SQLException
    */
    public Connection getConnection() throws SQLException {
	synchronized (connectionsPool) {
	    if(this.connectionsPool.size()>0)
		return this.connectionsPool.removeFirst();
	    
	    if(currentCount<maxCount) {
		this.currentCount++;
		return this.createConnection();
	    }
	    
	    throw new SQLException("没有连接");
	}
    }
    
    /** 
    * @Title: free 
    * @Description: TODO  
    * 将连接重新加到连接池中
    * @param conn
    */
    public void free(Connection conn) {
	this.connectionsPool.addLast(conn);
    }
}
