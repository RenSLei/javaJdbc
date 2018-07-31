package priv.rsl.jdbc.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/** 
* @ClassName: MyDataSource 
* @Description: TODO  
* ���ڴ������ӳص��࣬����һ��ʼ���ʹ���������ӷ��ڼ����У������ṩ��ȡ���ӵķ������Լ��������ӷŻ����ӳصķ���
* ����ϸ�ڵ��Ż���
* 1�������ӳؼ���ͬ������֤����߳��ò�ͬ������
* 2������������������������ݿ�
*  
* @author rsl
* @date 2018��3��28�� 
*  
*/
public class MyDataSource {
    //��url��user��password��װ������
    private static String url = "jdbc:mysql://localhost:3306/mydb1?useSSL=false";
    private static String user = "root";
    private static String password = "root";
    
    private static int initCount = 10;
    private static int maxCount = 20;
    private int currentCount = 0;
    
    
    //����һ�����ϣ����ڴ������
    private LinkedList<Connection> connectionsPool = new LinkedList<Connection>();
    
    /** 
    * <p>Title: ���캯��
    * <p>Description: ������initCount�����ӣ��Ž����ӳ��� 
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
    * ��������
    * @return
    * @throws SQLException
    */
    private Connection createConnection() throws SQLException {
	return DriverManager.getConnection(url, user, password);
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
}
