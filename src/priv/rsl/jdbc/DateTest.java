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
	    // 创建连接
	    conn = JdbcUtils.getConnection();

	    // 创建语句
	    String sql = "select id,username,birthday,entry_date,job,salary,resume,salary2"
		    + " from user where id =?";
	    ps = conn.prepareStatement(sql);
	    ps.setInt(1, n);// 在第一个?处换成name

	    // 执行语句

	    rs = ps.executeQuery();// 注意此处不能有sql参数了，不然就执行的是PreparedStatement 的父接口Statement的方法，执行就会报错

	    // 处理结果
	    while(rs.next()) {
		//这样就可以，因为是子类(sql)的Date赋给父类(util)的Date，且作了一些格式化的处理
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
    * 在数据库中插入一条记录
    * Date类有两个包：
    * 一个是util包里面的Date
    * 一个是其子类Date属于sql
    * 在使用的时候要特别注意导包的问题 
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
	    // 创建连接
	    conn = JdbcUtils.getConnection();

	    // 创建语句
	    String sql = "insert into user (username,birthday,job) values (?,?,?)";
	    ps = conn.prepareStatement(sql);
	    ps.setString(1, username);// 在第一个?处换成username
	    
	    //为什么不能直接ps.setDate(2, birthday);？
	    //因为我们的参数的Date是导入的util包里的Date不是java.sql包里的
	    //实际上是因为sql里的Date是继承的util里的Date，所以不能
	    ps.setDate(2, new java.sql.Date(birthday.getTime()));

	    ps.setString(3, job);

	    // 执行语句

	    int i = ps.executeUpdate();// 注意此处不能有sql参数了，不然就执行的是PreparedStatement 的父接口Statement的方法，执行就会报错

	    // 处理结果
	    System.out.println("i=" + i);

	} finally {
	    JdbcUtils.free(rs, ps, conn);
	}

    }
}
