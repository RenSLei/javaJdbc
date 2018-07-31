package priv.rsl.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AcidTest {

/**
 * @Title: main
 *
 * @Description: TODO
 * ��һ�����ԣ��������ύ���񡢻ع�����
 *
 * ����ĳ������������ĸ��ص�����֣���ʵ�������к��кܴ��Ӧ�õļ�ֵ
 * �����ݿ�����һ����
 *  +----+----------+----------+------------+-------+
 | id | name     | password | birthday   | money |
 +----+----------+----------+------------+-------+
 |  1 | rsl      | 111      | 1993-03-21 |   400 |
 |  2 | wangwu   | 222      | 1994-04-05 |   300 |
 |  3 | zhangsan | 333      | 1995-03-21 |   200 |
 |  4 | update   | 444      | 1996-03-21 |   100 |
 +----+----------+----------+------------+-------+

 * �־���һ�����ԣ���wangwu��Ǯ����200ʱ����rsl��Ǯ����300ʱ��rsl�͸�wangwuת100ԪǮ�����������ʵ���ǲ���Ҫת�ģ�
 * ����wangwu��ǮΪ200ʱ����������Ҫһ��ִ���ˡ��磺
 *  +----+----------+----------+------------+-------+
 | id | name     | password | birthday   | money |
 +----+----------+----------+------------+-------+
 |  1 | rsl      | 111      | 1993-03-21 |   400 |
 |  2 | wangwu   | 222      | 1994-04-05 |   150 |
 |  3 | zhangsan | 333      | 1995-03-21 |   200 |
 |  4 | update   | 444      | 1996-03-21 |   100 |
 +----+----------+----------+------------+-------+
 *
 * ���Է���Ҫ��������
 *  +----+----------+----------+------------+-------+
 | id | name     | password | birthday   | money |
 +----+----------+----------+------------+-------+
 |  1 | rsl      | 111      | 1993-03-21 |   400 |
 |  2 | wangwu   | 222      | 1994-04-05 |   150 |
 |  3 | zhangsan | 333      | 1995-03-21 |   200 |
 |  4 | update   | 444      | 1996-03-21 |   100 |
 +----+----------+----------+------------+-------+
 *
 * �����
 +----+----------+----------+------------+-------+
 | id | name     | password | birthday   | money |
 +----+----------+----------+------------+-------+
 |  1 | rsl      | 111      | 1993-03-21 |   300 |
 |  2 | wangwu   | 222      | 1994-04-05 |   250 |
 |  3 | zhangsan | 333      | 1995-03-21 |   200 |
 |  4 | update   | 444      | 1996-03-21 |   100 |
 +----+----------+----------+------------+-------+

    ���Բ�����Ҫ��������
 +----+----------+----------+------------+-------+
 | id | name     | password | birthday   | money |
 +----+----------+----------+------------+-------+
 |  1 | rsl      | 111      | 1993-03-21 |   400 |
 |  2 | wangwu   | 222      | 1994-04-05 |   250 |
 |  3 | zhangsan | 333      | 1995-03-21 |   200 |
 |  4 | update   | 444      | 1996-03-21 |   100 |
 +----+----------+----------+------------+-------+

   �����
 +----+----------+----------+------------+-------+
 | id | name     | password | birthday   | money |
 +----+----------+----------+------------+-------+
 |  1 | rsl      | 111      | 1993-03-21 |   400 |
 |  2 | wangwu   | 222      | 1994-04-05 |   250 |
 |  3 | zhangsan | 333      | 1995-03-21 |   200 |
 |  4 | update   | 444      | 1996-03-21 |   100 |
 +----+----------+----------+------------+-------+

   ������䣡�����������ķ�ʽ����ô�˴λ����rsl��Ǯ��100����wangwu��Ǯ����䡣
   ��֮��������ǽ��������һ��ִ�л���һ��ִ��

   ע�⣺���������InnoDB�ľ�֧�������Լ������Լ��       ENGINE=InnoDB
 * @param args
 * @throws SQLException
 *
 *
 */
@SuppressWarnings("resource")
public static void main(String[] args) throws SQLException {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
                conn = JdbcUtils.getConnection();

                //�����񣬴Ӵ˴���ʼ��ָ���ĵ㣬����Ҫôȫ��ִ�У�Ҫô����ִ��
                conn.setAutoCommit(false);

                st = conn.createStatement();
                //�Ƚ�rsl���˻����Ǯ��ѯ������
                String sql = "select money from user where id=1";
                rs = st.executeQuery(sql);
                if(rs.next()) {
                        //���rsl��Ǯ����300���ͼ�100��
                        if(rs.getFloat("money")<300)
                                throw new RuntimeException("rsl�˻�Ǯ����300��");
                        sql = "update user set money=money-100 where id=1";
                        st.executeUpdate(sql);
                }
                //��wangwu���˻����Ǯ��ѯ������
                sql = "select money from user where id=2";
                rs = st.executeQuery(sql);
                if(rs.next()) {
                        //���wangwu���˻����ǮС��200���ͼ�100��
                        if(rs.getFloat("money")>200)
                                throw new RuntimeException("wangwu�˻�Ǯ����200��");
                        sql = "update user set money=money+100 where id=2";
                        st.executeUpdate(sql);
                }
//	    �ύ���񣬴Ӵ������ύ������δ�����һ���
                conn.commit();//���ύ����ɹ������ͷŵ�ǰconn�����е��������ݿ���
        }catch(SQLException e) {
                //�ж�һ�£����conn���ǿգ���˵���ύ����ʧ�ܣ��ع�����
                if(conn!=null) {
                        conn.rollback();
                }
                throw e;
        }
        finally {
                JdbcUtils.free(rs, st, conn);
        }
}

}
