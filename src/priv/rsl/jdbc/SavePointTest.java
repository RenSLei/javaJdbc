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
 * 演示保存点：SavePoint的使用，即让回滚操作回滚到我们指定的地方，而不是全部回滚，只需要rollBack(回滚点对象就可以了)
 *	演示的需求是：
 *		先将rsl的money-100,当wangwu的money不满足要求时，money不变，但是rsl的money依然-100
 *
 * 原数据：
        +----+----------+----------+------------+-------+
        | id | name     | password | birthday   | money |
        +----+----------+----------+------------+-------+
        |  1 | rsl      | 111      | 1993-03-21 |   400 |
        |  2 | wangwu   | 222      | 1994-04-05 |   250 |
        |  3 | zhangsan | 333      | 1995-03-21 |   200 |
        |  4 | update   | 444      | 1996-03-21 |   100 |
        +----+----------+----------+------------+-------+
        
        执行后：
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

	    // 打开事务
	    conn.setAutoCommit(false);

	    st = conn.createStatement();
//		先将rsl的账户的钱扣除100
	    String sql = "update user set money=money-100 where id=1";
	    st.executeUpdate(sql);
	    
	    //将此处设置一个保存点，在回滚的时候执行者之前的代码
	    sp= conn.setSavepoint();
	    
	    // 将wangwu的账户里的钱查询出来：
	    sql = "select money from user where id=2";
	    rs = st.executeQuery(sql);
	    if (rs.next()) {
		// 如果小于200，就加100：
		if (rs.getFloat("money") > 200)
		    throw new RuntimeException("wangwu账户钱大于200！");
		sql = "update user set money=money+100 where id=2";
		st.executeUpdate(sql);
	    }
	    // 提交事务，从打开事务到提交事务这段代码是一体的,且如果全部执行就会释放conn为null
	    conn.commit();
	}
	//用两个catch，第一个catch是为了检查是否会增加wangwud的money，若抛出此异常，就说明是没有增加，回滚到保存点
	catch(RuntimeException e) {
	    //判断conn是否是为null且sp不为空
	    if(conn!=null&&sp!=null) {
		conn.rollback(sp);
		//回滚到保存点时再提交事务
		conn.commit();
	    }
	}
	catch (SQLException e) {
	    // 判断一下，如果conn不是空，则说明提交事务失败，回滚事务
	    if (conn != null) {
		conn.rollback();
	    }
	    throw e;
	} finally {
	    JdbcUtils.free(rs, st, conn);
	}
    }
}
