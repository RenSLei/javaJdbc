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
 * 做一个测试：打开事务、提交事务、回滚事务
 *
 * 事务的出现正是它那四个特点的体现，在实际生活中很有很大的应用的价值
 * 现数据库中有一个表：
 *  +----+----------+----------+------------+-------+
 | id | name     | password | birthday   | money |
 +----+----------+----------+------------+-------+
 |  1 | rsl      | 111      | 1993-03-21 |   400 |
 |  2 | wangwu   | 222      | 1994-04-05 |   300 |
 |  3 | zhangsan | 333      | 1995-03-21 |   200 |
 |  4 | update   | 444      | 1996-03-21 |   100 |
 +----+----------+----------+------------+-------+

 * 现就做一个测试，当wangwu的钱少于200时，且rsl的钱大于300时，rsl就给wangwu转100元钱，按照这个表，实际是不需要转的，
 * 但当wangwu的钱为200时，这两步就要一起执行了。如：
 *  +----+----------+----------+------------+-------+
 | id | name     | password | birthday   | money |
 +----+----------+----------+------------+-------+
 |  1 | rsl      | 111      | 1993-03-21 |   400 |
 |  2 | wangwu   | 222      | 1994-04-05 |   150 |
 |  3 | zhangsan | 333      | 1995-03-21 |   200 |
 |  4 | update   | 444      | 1996-03-21 |   100 |
 +----+----------+----------+------------+-------+
 *
 * 测试符合要求的情况：
 *  +----+----------+----------+------------+-------+
 | id | name     | password | birthday   | money |
 +----+----------+----------+------------+-------+
 |  1 | rsl      | 111      | 1993-03-21 |   400 |
 |  2 | wangwu   | 222      | 1994-04-05 |   150 |
 |  3 | zhangsan | 333      | 1995-03-21 |   200 |
 |  4 | update   | 444      | 1996-03-21 |   100 |
 +----+----------+----------+------------+-------+
 *
 * 结果：
 +----+----------+----------+------------+-------+
 | id | name     | password | birthday   | money |
 +----+----------+----------+------------+-------+
 |  1 | rsl      | 111      | 1993-03-21 |   300 |
 |  2 | wangwu   | 222      | 1994-04-05 |   250 |
 |  3 | zhangsan | 333      | 1995-03-21 |   200 |
 |  4 | update   | 444      | 1996-03-21 |   100 |
 +----+----------+----------+------------+-------+

    测试不符合要求的情况：
 +----+----------+----------+------------+-------+
 | id | name     | password | birthday   | money |
 +----+----------+----------+------------+-------+
 |  1 | rsl      | 111      | 1993-03-21 |   400 |
 |  2 | wangwu   | 222      | 1994-04-05 |   250 |
 |  3 | zhangsan | 333      | 1995-03-21 |   200 |
 |  4 | update   | 444      | 1996-03-21 |   100 |
 +----+----------+----------+------------+-------+

   结果：
 +----+----------+----------+------------+-------+
 | id | name     | password | birthday   | money |
 +----+----------+----------+------------+-------+
 |  1 | rsl      | 111      | 1993-03-21 |   400 |
 |  2 | wangwu   | 222      | 1994-04-05 |   250 |
 |  3 | zhangsan | 333      | 1995-03-21 |   200 |
 |  4 | update   | 444      | 1996-03-21 |   100 |
 +----+----------+----------+------------+-------+

   结果不变！如果不用事务的方式，那么此次会造成rsl的钱少100，而wangwu得钱不会变。
   总之，事务就是将代码放在一起执行或者一起不执行

   注意：表的引擎是InnoDB的就支持事务以及外键的约束       ENGINE=InnoDB
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

                //打开事务，从此处开始到指定的点，代码要么全部执行，要么都不执行
                conn.setAutoCommit(false);

                st = conn.createStatement();
                //先将rsl的账户里的钱查询出来：
                String sql = "select money from user where id=1";
                rs = st.executeQuery(sql);
                if(rs.next()) {
                        //如果rsl的钱大于300，就减100：
                        if(rs.getFloat("money")<300)
                                throw new RuntimeException("rsl账户钱少于300！");
                        sql = "update user set money=money-100 where id=1";
                        st.executeUpdate(sql);
                }
                //将wangwu的账户里的钱查询出来：
                sql = "select money from user where id=2";
                rs = st.executeQuery(sql);
                if(rs.next()) {
                        //如果wangwu的账户里的钱小于200，就加100：
                        if(rs.getFloat("money")>200)
                                throw new RuntimeException("wangwu账户钱大于200！");
                        sql = "update user set money=money+100 where id=2";
                        st.executeUpdate(sql);
                }
//	    提交事务，从打开事务到提交事务这段代码是一体的
                conn.commit();//若提交事务成功，则释放当前conn所持有的所有数据库锁
        }catch(SQLException e) {
                //判断一下，如果conn不是空，则说明提交事务失败，回滚事务
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
