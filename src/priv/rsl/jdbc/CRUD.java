package priv.rsl.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import priv.rsl.jdbc.JdbcUtils;

/** 
* @ClassName: CRUD 
* @Description: TODO  
* ����jdbcʵ�ֶ�mysql���ݿ����ݵ���ɾ�Ĳ����
* 
* @author rsl
* @date 2018��4��11�� 
*  
*/
public class CRUD {

    public static void main(String[] args) throws SQLException {
	//create();
	//read();
	//update();
	delete();
    }

    

    /** 
    * @Title: create 
    * @Description: TODO  
    * ������䣺
    * 1��ʹ��st.executeUpdate(sql);���ص���һ�����Σ���ʾ�����б��Ķ���
    * 2��û�н���Ĵ���
    * @param 
    * @return void
    * @throws SQLException
    */
    static void create() throws SQLException {
	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;
	
	try {
	    //��������
	    conn = JdbcUtils.getConnection();

	    // �������
	    st = conn.createStatement();

	    // ִ�����
	    String sql = "insert into user(id,username,birthday,entry_date,job,salary,resume,salary2) "
	    	+ "values(6,'zhaoqi','1995-06-21','2018-02-18','worker',4000,'fsfasdg',500)";
	    int i = st.executeUpdate(sql);

	    // ������
		    System.out.print("i="+i);

	} finally {
	    JdbcUtils.free(rs, st, conn);
	}

    }
    
    /** 
    * @Title: read 
    * @Description: TODO 
    * ��ȡ���ݿ��������
    * 
    * ��ȡ���һ��Ҫ��ϸ����Ҫ��select��Щ��д������ǿ����Ŀɶ���
    *  
    * @param 
    * @return void
    * @throws SQLException
    */
    static void read() throws SQLException {
	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;
	
	try {
	    //��������
	    conn = JdbcUtils.getConnection();

	    // �������
	    st = conn.createStatement();

	    //ִ�����
	    rs = st.executeQuery("select id,username,birthday,entry_date,job,salary,resume,salary2 from user");

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
    * @Title: update 
    * @Description: TODO  
    * �������ݿ������
    * updateҲ�Ƿ��ص��Ƕ����м�¼���޸�
    * 
    * @param 
    * @return void
    * @throws SQLException
    */
    static void update() throws SQLException {
	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;
	try {
	    // ��������
	    conn = JdbcUtils.getConnection();

	    // �������
	    st = conn.createStatement();

	    // ִ�����
	    String sql = "update user set salary=salary+100";
	    int i = st.executeUpdate(sql);

	    // ������
	    System.out.print("i=" + i);
	} finally {
	    JdbcUtils.free(rs, st, conn);
	}

    }
    
    /** 
    * @Title: delete 
    * @Description: TODO  
    * ɾ�����ݿ�ļ�¼
    * @throws SQLException
    */
    static void delete() throws SQLException {
 	Connection conn = null;
 	Statement st = null;
 	ResultSet rs = null;
 	try {
 	    // ��������
 	    conn = JdbcUtils.getConnection();

 	    // �������
 	    st = conn.createStatement();

 	    // ִ�����
 	    String sql = "delete from user where salary2<100";
 	    int i = st.executeUpdate(sql);

 	    // ������
 	    System.out.print("i=" + i);
 	} finally {
 	    JdbcUtils.free(rs, st, conn);
 	}

     }

}
