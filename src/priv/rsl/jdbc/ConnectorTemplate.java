package priv.rsl.jdbc;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;//�������������������import����classpath�µ�jar�����еİ�
import com.mysql.jdbc.Statement;//�������������������import����classpath�µ�jar�����еİ�
/** 
* @ClassName: JdbcBaseInstance 
* @Description: TODO  
* �������ݵ�һ�㲽�裺
* һ��ע��������ֻ��һ�Σ�
* ��������ʵ����JDBC�ӿڵ��ࡣ
* ע���������Ǹ���DriverManagerҪ����һ����������,���������֣�
* 
*1��DriverManager.registerDriver(new com.mysql.jdbc.Driver());
* 		���ͣ���com.mysql.jdbc.Driver�������������һ���࣬Դ�������£�
* 		
* 		public class Driver extends NonRegisteringDriver implements java.sql.Driver {
*    		//Register ourselves with the DriverManager
*    		static {
*        		try {
*            			java.sql.DriverManager.registerDriver(new Driver());
*        		} catch (SQLException E) {
*            	throw new RuntimeException("Can't register driver!");
*        	}
*    			}
* 		* Construct a new driver and register it with DriverManager
* 		* 
*		* @throws SQLException
*		*             if a database error occurs.
*	�Ķ���������Driver ���Դ�������֪����
*	1)������ʵ����java.sql.Driver�ӿ�(�����������Ķ�������)��
*	2)��������һ����̬����飬����һ�����ؾͻ�ִ��DriverManager.registerDriver(new Driver())������һ���������
*
*���ۣ����ַ��������DriverManager�в�������һ��������������Ծ������������������� 
������˵���ǣ� 
a.���ص�ʱ��ע��һ��������ʵ������ʱ����ע��һ�Ρ��������Ρ� 
b.����ʵ������com.mysql.jdbc.Driver.class�����±��뵼�����(����Ҫ�������import��ȥ)���Ӷ�����������������������������չ���롣 

*2��System.setProperty("jdbc.drivers","com.mysql.jdbc.Driver"); 
*ͨ��ϵͳ����������ע������
*���Ҫע������������System.setProperty("jdbc.drivers","com.mysql.jdbc.Driver:com.oracle.jdbc.Driver"); 
*��Ȼ����Ծ���������������������ע�᲻̫���㣬���Ժ���ʹ�á� 
*
*3��Class.forName("com.mysql.jdbc.Driver");
*�Ƽ����ַ�ʽ������Ծ����������������������ǲ���import package�ˣ��� 
*��ʵ���ֻ�ǰ�com.mysql.jdbc.Driver.class�����װ�ؽ�ȥ�����ǹؼ������ھ�̬��,������Ϊ�������飬�����ڼ��ص�ʱ��Ͱ�����ע���ȥ�ˣ�
*
* �����������ӣ�Connection��
* �൱���Ǵ��ţ���������
* 
* ��������ִ��SQL����䣨Statement��
* �൱������
* 
* �ġ�ִ�����
* ��Ҫִ�е�������Statement�н���ִ��
* 
* �塢����ִ�еĽ����ResultSet��
* ��������������
* 
* �����ͷ���Դ
* �����ȴ�����رյ�ԭ�������Դ�Ĺر�,���ݿ�������Խ�࣬���ݿ�ϵͳԽ��
*
* @author rsl
* @date 2018��3��8�� ����5:05:54 
*  
*/
public class ConnectorTemplate {

    public static void main(String[] args) throws Exception {
	//test();
	template();
    }
    
    
    
    /** 
    * @Title: template 
    * @Description: TODO  
    * �Ľ�֮���ǣ�
    * �������������쳣try-finally���ر���Դtry-finally
    * 
    * ���ǣ�ע������ֻ��Ҫִ��һ�ξ����ˣ����Ҳ���ÿ�ζ�ȥд�������ӵĹر���Դ�Ĵ���
    * ����ע�������͹ر���Դ�Ϳ��Ե�����װһ����,������������ʹ��:���JdbcUtils��
    * @param @throws Exception
    * @return void
    * @throws 
    */
    static void template() throws Exception {
	//���������ڹ������У�Ҫ�޸�ֻ��Ҫ�޸���һ�������Ϣ�Ϳ�����
	java.sql.Connection conn = null;
	java.sql.Statement st = null;
	ResultSet rs = null;
	
	//�����쳣
	try {
	    
	    /*ע�������Ѿ��ڹ�������ʵ����
	    Class.forName("com.mysql.jdbc.Driver");
	    */
	    
	    //��������
	    conn = JdbcUtils.getConnection();
	    //conn = JdbcUtilsSing.getInstance().getConnection();

	    //�������
	    st = conn.createStatement();

	    //ִ�����
	    rs = st.executeQuery("select * from user");

	    //������
	    while (rs.next()) {
		for (int i = 1; i <= 8; i++) {
		    System.out.print(rs.getObject(i) + " \t");
		}
		System.out.println("\n");
	    }

	} finally {
	    JdbcUtils.free(rs, st, conn);
	}
    }
    
    /** 
    * @Title: test 
    * @Description: TODO 
    * һ�ּ򵥵ġ��������������ݵĹ��̣����Ͻ���Ҳ�����ر��Ż��Ĵ��� 
    * @throws SQLException
    * @throws ClassNotFoundException
    * @return void
    *
    */
    static void test() throws SQLException, ClassNotFoundException {
	//1.ע������
	//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
	//System.setProperty("jdbc.drivers", "com.mysql.jdbc.Driver");
	Class.forName("com.mysql.jdbc.Driver");//�Ƽ���ʽ
	
	
	//2.��������
	//��ͬ�����ݿ��в�ͬ��ʽ��url��ʽ���Ҳ���Ҫȥ������ֻ��Ҫ�鿴�ĵ��Ϳ����� 
	String url = "jdbc:mysql://localhost:3306/mydb1";
	String user = "root";
	String password = "root";
	Connection conn = (Connection) DriverManager.getConnection(url,user,password);
	
	//3.�������
	java.sql.Statement st = (Statement) conn.createStatement();
	
	//4.ִ�����
	ResultSet rs =st.executeQuery("select * from user");
	
	//5.������
	while(rs.next()) {
	    for (int i = 1; i <9; i++) {
		System.out.print(rs.getObject(i)+"   \t  ");
	    }
	    System.out.println("\n");
	}
	rs.close();
	st.close();
	conn.close();
    }

}



















