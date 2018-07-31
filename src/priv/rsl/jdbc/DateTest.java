package priv.rsl.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import priv.rsl.jdbc.JdbcUtils;

public class DateTest {

    public static void main(String[] args) throws SQLException {
	//create("rsl",new Date(1994-02-21),"fsefes");
	read(2);
    }

    static void read(int n) throws SQLException {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	Date Birthday = null;

	try {
	    // ��������
	    conn = JdbcUtils.getConnection();

	    // �������
	    String sql = "select id,username,birthday,entry_date,job,salary,resume,salary2"
		    + " from user where id =?";
	    ps = conn.prepareStatement(sql);
	    ps.setInt(1, n);// �ڵ�һ��?������name

	    // ִ�����

	    rs = ps.executeQuery();// ע��˴�������sql�����ˣ���Ȼ��ִ�е���PreparedStatement �ĸ��ӿ�Statement�ķ�����ִ�оͻᱨ��

	    // ������
	    while(rs.next()) {
		//�����Ϳ��ԣ���Ϊ������(sql)��Date��������(util)��Date��������һЩ��ʽ���Ĵ���
		Birthday = rs.getDate("birthday");
	    }
	    System.out.print(Birthday);

	} finally {
	    JdbcUtils.free(rs, ps, conn);
	}

    }

    /** 
    * @Title: create 
    * @Description: TODO 
    * �����ݿ��в���һ����¼
    * Date������������
    * һ����util�������Date
    * һ����������Date����sql
    * ��ʹ�õ�ʱ��Ҫ�ر�ע�⵼�������� 
    * @param username
    * @param birthday
    * @param job 
    * @throws SQLException
    * @return void
    * @throws 
    */
    static void create(String username,Date birthday,String job) throws SQLException {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    // ��������
	    conn = JdbcUtils.getConnection();

	    // �������
	    String sql = "insert into user (username,birthday,job) values (?,?,?)";
	    ps = conn.prepareStatement(sql);
	    ps.setString(1, username);// �ڵ�һ��?������username
	    
	    //Ϊʲô����ֱ��ps.setDate(2, birthday);��
	    //��Ϊ���ǵĲ�����Date�ǵ����util�����Date����java.sql�����
	    //ʵ��������Ϊsql���Date�Ǽ̳е�util���Date�����Բ���
	    ps.setDate(2, new java.sql.Date(birthday.getTime()));

	    ps.setString(3, job);

	    // ִ�����

	    int i = ps.executeUpdate();// ע��˴�������sql�����ˣ���Ȼ��ִ�е���PreparedStatement �ĸ��ӿ�Statement�ķ�����ִ�оͻᱨ��

	    // ������
	    System.out.println("i=" + i);

	} finally {
	    JdbcUtils.free(rs, ps, conn);
	}

    }
}
