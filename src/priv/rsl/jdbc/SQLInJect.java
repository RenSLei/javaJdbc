package priv.rsl.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import priv.rsl.jdbc.JdbcUtils;

public class SQLInJect {

    public static void main(String[] args) throws SQLException {
	read("lisi");
    }

    /** 
    * @Title: read 
    * @Description: TODO
    * �Ƚ� PreparedStatement �� Statement������
    * 1��һ����PreparedStatement �Ƕ��ַ�������Ԥ�������Ա�����Щ�����ַ�����SQLע������
    * 2������ִ��Ч�ʵ�����PreparedStatement Ҫ��Statement��ܶ� 
    * 3�����в�����Ҫ��PreparedStatement ����
    * @param  name Ҫ��ѯ���ַ���
    * @return void
    * @throws SQLException
    */
    static void read(String name) throws SQLException {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    // ��������
	    conn = JdbcUtils.getConnection();

	    // �������
	    String sql = "select id,username,birthday,entry_date,job,salary,resume,salary2"
	    	+ " from user where username =?";
	    ps = conn.prepareStatement(sql);
	    ps.setString(1, name);//�ڵ�һ��?������name

	    // ִ�����
	    
	    rs = ps.executeQuery();//ע��˴�������sql�����ˣ���Ȼ��ִ�е���PreparedStatement �ĸ��ӿ�Statement�ķ�����ִ�оͻᱨ�� 

	    // ������
	    while (rs.next()) {
		for (int i = 1; i <= 8; i++) {
		    System.out.print(rs.getObject(i) + " \t");
		}
		System.out.println("\n");
	    }

	} finally {
	    JdbcUtils.free(rs, ps, conn);
	}

    }

}
