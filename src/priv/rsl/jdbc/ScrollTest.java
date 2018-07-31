package priv.rsl.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** 
* @ClassName: ScrollTest 
* @Description: TODO  
* 1、讲解可滚动的结果集：
* 	即结果ResultSet里不仅可以往下滚动，可以有其他的滚动方式
* 	在创建createStatement()的时候，可以指定特定的滚动方式：以参数的形式指定
* 2、分页：
* 	对于有分页功能的数据库来说，就选择数据库自带的分页功能，这样效率很高。
* 
* 	而对于没有该功能的来说，只有使用接口中的方法进行手动分页，代码如下：分页
* 	需求是从306条记录处往后取10条记录出来，对于mysql而言，只需要在sql语句后面加上限制limit 306,10就可以了
* 	
* @author rsl
* @date 2018年3月24日 
*  
*/
public class ScrollTest {

    public static void main(String[] args) throws SQLException {
	scroll();
    }

    static void scroll() throws SQLException {
	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;

	try {
	    // 创建连接
	    conn = JdbcUtils.getConnection();
	    
	    //指定可以滚动
	    st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
	    
	    rs = st.executeQuery("select id,name,password,birthday from user");

	    // 处理结果
	    while (rs.next()) {
		    System.out.println(rs.getInt("id")+ " \t" +
			    rs.getString("name")+ " \t" +rs.getString("password")+ " \t" +
			    rs.getDate("birthday")+ " \t");
	    }
	    System.out.println("------------------------");
	    
	    //定位到第几行！！！！注意是定位到rs查询结果中的第4行
	    rs.absolute(4);
	    //测试结果往回滚：是可以往回滚的
	    if(rs.previous())
		   System.out.println(rs.getInt("id")+ " \t" +
			    rs.getString("name")+ " \t" +rs.getString("password")+ " \t" +
			    rs.getDate("birthday")+ " \t");
	    
	    System.out.println("------------------------");
	    
	    //分页：
	    //定位到第306条记录：
	    rs.absolute(306);
	    int i=0;
	    while(rs.next()&&i<10) {
		i++;
		System.out.println(rs.getInt("id") + " \t" + rs.getString("name") + " \t" + rs.getString("password")
			+ " \t" + rs.getDate("birthday") + " \t");

	    }

	} finally {
	    JdbcUtils.free(rs, st, conn);
	}

    }
}
