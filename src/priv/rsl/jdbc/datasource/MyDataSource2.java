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
* ���ڴ������ӳص��࣬����һ��ʼ���ʹ���������ӷ��ڼ����У������ṩ��ȡ���ӵķ������Լ��������ӷŻ����ӳصķ���
* ��ԭ�еĹ���ϸ�ڵ��Ż��Ļ����ϣ�
* ���µ���MyConnection���ʹ�ã�����close����������ʵ��ʹ�ô���ģʽ���������û��ر����ӵ�ϰ��
* 
* Ϊʲô����Ϊһ��ͨ������д�Ĺ���������ȡ�����ǲ�̫������ʵ�����ӵĸ��õģ����Իᰴ��һ������ӵ�����ȥclose��
* ������ϣ���ڵ����ǵ���close()��ʱ��ͽ����ӷŻ����ӳ��У��ﵽ���Ӹ��õ�Ч��
* 
* ���⣬���MyDataSource2ʵ���˽ӿ�DataSource���������Լ�������Դ����һ����׼��DataSource,ͨ�����ӳصķ�ʽʹ������
* 
* 
* ʵ�ʿ����У��кܶ࿪Դ�����ݿ����ӳش��룬Ҳ��ʵ����DataSource����DBCP.
* DBCPͨ���Լ��������������commons-pool.jar�Լ�commons-collections.jar�Լ�һ��commons-dbcp.jar��ʵ��
* ������Ҫ��������������Ȼ�󴴽�����DataSource �Ķ���ͨ�����������ļ�����ʽ������Ϣ���ؽ�����ͨ��dbcp�����
* ������BasicDataSource��������������ļ�����ϸ���Բ��ģ�https://blog.csdn.net/CSDN_LQR/article/details/52875985
* 
* ʹ��DBCP��Ϊ������
* 1����������������commons-pool.jar�Լ�commons-collections.jar�Լ�һ��commons-dbcp.jar
* 2�����ò���������ֱ���ڴ������ã�����ͨ�����������ļ������ã�ͨ�����������������һ�������������������������ļ��������
* 3��ʹ�ù��������������ӣ�BasicDataSourceFactory.createDataSource(properties);
* 
* @author rsl
* @date 2018��3��28�� 
*  
*/
public class MyDataSource2 implements DataSource{
    //��url��user��password��װ������
    private static String url = "jdbc:mysql://localhost:3306/mydb1?useSSL=false";
    private static String user = "root";
    private static String password = "root";
    
    private static int initCount = 5;
    private static int maxCount = 8;
    int currentCount = 0;
    
    
    //����һ�����ϣ����ڴ������
    LinkedList<Connection> connectionsPool = new LinkedList<Connection>();
    
    /** 
    * <p>Title: ���캯��
    * <p>Description: ������initCount�����ӣ��Ž����ӳ��� 
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
    * ��������
    * @return
    * @throws SQLException
    */
    private Connection createConnection() throws SQLException {
	Connection realConn = DriverManager.getConnection(url, user, password);
//	ʹ��MyConnectionʱ�Ĳ��ԣ�
//	MyConnection myConnection = new MyConnection(realConn,this);
//	return myConnection;
	
//	ʹ��MyConnectionHandlerʱ�Ĵ��룺
	MyConnectionHandler proxy = new MyConnectionHandler(this);
	return proxy.bind(realConn);
    }
    
    /** 
    * @Title: getConnection 
    * @Description: TODO  
    * ��ȡ���ӡ��÷������̼߳�������֤�߳��õ�������Ψһ�����������������
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
	    
	    throw new SQLException("û������");
	}
    }
    
    /** 
    * @Title: free 
    * @Description: TODO  
    * ���������¼ӵ����ӳ���
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
