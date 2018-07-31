package priv.rsl.jdbc.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.logging.Logger;

import javax.sql.DataSource;

/** 
* @ClassName: MyDataSource2 
* @Description: TODO  
* 用于创建连接池的类，该类一初始化就创建多个连接放在集合中，对外提供获取连接的方法，以及将将连接放回连接池的方法
* 在原有的工程细节的优化的基础上，
* 与新的类MyConnection组合使用，拦截close（）方法，实现使用代理模式来保持与用户关闭连接的习惯
* 
* 为什么？因为一般通过别人写的工具类来获取连接是不太清除如何实现连接的复用的，所以会按照一般的连接的流程去close，
* 而我们希望在当他们调用close()的时候就将连接放回连接池中，达到连接复用的效果
* 
* 另外，最后MyDataSource2实现了接口DataSource，即我们自己的数据源就是一个标准的DataSource,通过连接池的方式使用连接
* 
* 
* 实际开发中，有很多开源的数据库连接池代码，也是实现了DataSource，如DBCP.
* DBCP通过自己的两个基本类库commons-pool.jar以及commons-collections.jar以及一个commons-dbcp.jar来实现
* 所以需要导入这三个包，然后创建连接DataSource 的对象，通过加载配置文件的形式来将信息加载进来，通过dbcp里面的
* 工厂类BasicDataSource来加载这个配置文件，详细可以参阅：https://blog.csdn.net/CSDN_LQR/article/details/52875985
* 
* 使用DBCP分为三步：
* 1、导入这三个包：commons-pool.jar以及commons-collections.jar以及一个commons-dbcp.jar
* 2、配置参数：可以直接在代码配置，可以通过加载配置文件来配置（通过本类类加载器构造一个输入流，此输入流与配置文件相关联）
* 3、使用工厂类来创建连接：BasicDataSourceFactory.createDataSource(properties);
* 
* @author rsl
* @date 2018年3月28日 
*  
*/
public class MyDataSource2 implements DataSource{
    //将url、user、password封装到该类
    private static String url = "jdbc:mysql://localhost:3306/mydb1?useSSL=false";
    private static String user = "root";
    private static String password = "root";
    
    private static int initCount = 5;
    private static int maxCount = 8;
    int currentCount = 0;
    
    
    //创建一个集合，用于存放连接
    LinkedList<Connection> connectionsPool = new LinkedList<Connection>();
    
    /** 
    * <p>Title: 构造函数
    * <p>Description: 将创建initCount个连接，放进连接池中 
    */
    public MyDataSource2() {
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
	Connection realConn = DriverManager.getConnection(url, user, password);
//	使用MyConnection时的测试：
//	MyConnection myConnection = new MyConnection(realConn,this);
//	return myConnection;
	
//	使用MyConnectionHandler时的代码：
	MyConnectionHandler proxy = new MyConnectionHandler(this);
	return proxy.bind(realConn);
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

    @Override
    public PrintWriter getLogWriter() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public int getLoginTimeout() throws SQLException {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }
}
