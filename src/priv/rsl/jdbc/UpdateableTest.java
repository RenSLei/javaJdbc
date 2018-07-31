package priv.rsl.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** 
* @ClassName: UpdateableTest 
* @Description: TODO  
* 1、更新：
* 	了解一下，在查询出的结果中可以对原始数据进行更新数据的操作，但是一般不这样更新，会使业务逻辑层混乱
* 	需求：将名字是wangwu的改为lisi
* 
* 2、senstitive:
* 	不同的数据库以及不同的驱动，都会导致不能实现jdbc的完全的功能规范，所以这里没有测试出来。
* 	
* @author rsl
* @date 2018年3月24日 
*  
*/
public class UpdateableTest {

    public static void main(String[] args) throws SQLException, InterruptedException {
	 update(); 
    }

    static void update() throws SQLException, InterruptedException {
	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;

	try {
	    // 创建连接
	    conn = JdbcUtils.getConnection();

	    // 指定可滚动、可更新：
	    st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

	    rs = st.executeQuery("select id,name,password,birthday from user where id < 5 ");

	    // update
//	    while (rs.next()) {
//		
//		System.out.println(rs.getInt("id") + " \t" + rs.getString("name") + " \t" + rs.getString("password")
//			+ " \t" + rs.getDate("birthday") + " \t");
//		String name = rs.getString("name");
//		if("wangwu".equals(name)) {
//		    rs.updateString("name", "lisi");
//		    rs.updateRow();
//		}
//	    }
//	    System.out.println("------------------------");
	    
	    //sensitive:一边查询，一边利用命令行修改数据库中的值，看看它能不能感知到？？？
	    while (rs.next()) {
		System.out.println("show...."+rs.getInt("id")+"....");
		Thread.sleep(10000);
		System.out.println(rs.getInt("id") + " \t" + rs.getString("name") + " \t" + rs.getString("password")
			+ " \t" + rs.getDate("birthday") + " \t");
		}
	   
	} finally {
	    JdbcUtils.free(rs, st, conn);
	}

    }
}
