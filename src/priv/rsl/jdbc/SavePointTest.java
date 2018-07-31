package priv.rsl.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

public class SavePointTest {

/**
 * @Title: main
 *
 * @Description: TODO 
 * ��ʾ����㣺SavePoint��ʹ�ã����ûع������ع�������ָ���ĵط���������ȫ���ع���ֻ��ҪrollBack(�ع������Ϳ�����)
 *	��ʾ�������ǣ�
 *		�Ƚ�rsl��money-100,��wangwu��money������Ҫ��ʱ��money���䣬����rsl��money��Ȼ-100
 *
 * ԭ���ݣ�
        +----+----------+----------+------------+-------+
        | id | name     | password | birthday   | money |
        +----+----------+----------+------------+-------+
        |  1 | rsl      | 111      | 1993-03-21 |   400 |
        |  2 | wangwu   | 222      | 1994-04-05 |   250 |
        |  3 | zhangsan | 333      | 1995-03-21 |   200 |
        |  4 | update   | 444      | 1996-03-21 |   100 |
        +----+----------+----------+------------+-------+
        
        ִ�к�
        +----+----------+----------+------------+-------+
        | id | name     | password | birthday   | money |
        +----+----------+----------+------------+-------+
        |  1 | rsl      | 111      | 1993-03-21 |   300 |
        |  2 | wangwu   | 222      | 1994-04-05 |   250 |
        |  3 | zhangsan | 333      | 1995-03-21 |   200 |
        |  4 | update   | 444      | 1996-03-21 |   100 |
        +----+----------+----------+------------+-------+
 * @param args
 * @throws SQLException
 *
 *
 */
    public static void main(String[] args) throws SQLException {
	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;
	Savepoint sp = null;

	try {
	    conn = JdbcUtils.getConnection();

	    // ������
	    conn.setAutoCommit(false);

	    st = conn.createStatement();
//		�Ƚ�rsl���˻���Ǯ�۳�100
	    String sql = "update user set money=money-100 where id=1";
	    st.executeUpdate(sql);
	    
	    //���˴�����һ������㣬�ڻع���ʱ��ִ����֮ǰ�Ĵ���
	    sp= conn.setSavepoint();
	    
	    // ��wangwu���˻����Ǯ��ѯ������
	    sql = "select money from user where id=2";
	    rs = st.executeQuery(sql);
	    if (rs.next()) {
		// ���С��200���ͼ�100��
		if (rs.getFloat("money") > 200)
		    throw new RuntimeException("wangwu�˻�Ǯ����200��");
		sql = "update user set money=money+100 where id=2";
		st.executeUpdate(sql);
	    }
	    // �ύ���񣬴Ӵ������ύ������δ�����һ���,�����ȫ��ִ�оͻ��ͷ�connΪnull
	    conn.commit();
	}
	//������catch����һ��catch��Ϊ�˼���Ƿ������wangwud��money�����׳����쳣����˵����û�����ӣ��ع��������
	catch(RuntimeException e) {
	    //�ж�conn�Ƿ���Ϊnull��sp��Ϊ��
	    if(conn!=null&&sp!=null) {
		conn.rollback(sp);
		//�ع��������ʱ���ύ����
		conn.commit();
	    }
	}
	catch (SQLException e) {
	    // �ж�һ�£����conn���ǿգ���˵���ύ����ʧ�ܣ��ع�����
	    if (conn != null) {
		conn.rollback();
	    }
	    throw e;
	} finally {
	    JdbcUtils.free(rs, st, conn);
	}
    }
}
