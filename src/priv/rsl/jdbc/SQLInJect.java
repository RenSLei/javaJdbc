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
    * 比较 PreparedStatement 和 Statement的区别
    * 1、一个是PreparedStatement 是对字符串进行预处理，可以避免有些特殊字符串的SQL注入问题
    * 2、二是执行效率的问题PreparedStatement 要比Statement快很多 
    * 3、带有参数的要用PreparedStatement 来做
    * @param  name 要查询的字符串
    * @return void
    * @throws SQLException
    */
    static void read(String name) throws SQLException {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    // 创建连接
	    conn = JdbcUtils.getConnection();

	    // 创建语句
	    String sql = "select id,username,birthday,entry_date,job,salary,resume,salary2"
	    	+ " from user where username =?";
	    ps = conn.prepareStatement(sql);
	    ps.setString(1, name);//在第一个?处换成name

	    // 执行语句
	    
	    rs = ps.executeQuery();//注意此处不能有sql参数了，不然就执行的是PreparedStatement 的父接口Statement的方法，执行就会报错 

	    // 处理结果
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
