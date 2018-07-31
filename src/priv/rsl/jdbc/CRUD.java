package priv.rsl.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import priv.rsl.jdbc.JdbcUtils;

/** 
* @ClassName: CRUD 
* @Description: TODO  
* 利用jdbc实现对mysql数据库数据的增删改查操作
* 
* @author rsl
* @date 2018年4月11日 
*  
*/
public class CRUD {

    public static void main(String[] args) throws SQLException {
	//create();
	//read();
	//update();
	delete();
    }

    

    /** 
    * @Title: create 
    * @Description: TODO  
    * 创建语句：
    * 1、使用st.executeUpdate(sql);返回的是一个整形，表示多少行被改动；
    * 2、没有结果的处理
    * @param 
    * @return void
    * @throws SQLException
    */
    static void create() throws SQLException {
	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;
	
	try {
	    //创建连接
	    conn = JdbcUtils.getConnection();

	    // 创建语句
	    st = conn.createStatement();

	    // 执行语句
	    String sql = "insert into user(id,username,birthday,entry_date,job,salary,resume,salary2) "
	    	+ "values(6,'zhaoqi','1995-06-21','2018-02-18','worker',4000,'fsfasdg',500)";
	    int i = st.executeUpdate(sql);

	    // 处理结果
		    System.out.print("i="+i);

	} finally {
	    JdbcUtils.free(rs, st, conn);
	}

    }
    
    /** 
    * @Title: read 
    * @Description: TODO 
    * 读取数据库里的数据
    * 
    * 读取语句一定要详细化，要把select哪些列写出来增强程序的可读性
    *  
    * @param 
    * @return void
    * @throws SQLException
    */
    static void read() throws SQLException {
	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;
	
	try {
	    //创建连接
	    conn = JdbcUtils.getConnection();

	    // 创建语句
	    st = conn.createStatement();

	    //执行语句
	    rs = st.executeQuery("select id,username,birthday,entry_date,job,salary,resume,salary2 from user");

	    //处理结果
	    while (rs.next()) {
		for (int i = 1; i <= 8; i++) {
		    System.out.print(rs.getObject(i) + " \t");
		}
		System.out.println("\n");
	    }

	} finally {
	    JdbcUtils.free(rs, st, conn);
	}

    }

    /** 
    * @Title: update 
    * @Description: TODO  
    * 更改数据库的数据
    * update也是返回的是多少行记录被修改
    * 
    * @param 
    * @return void
    * @throws SQLException
    */
    static void update() throws SQLException {
	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;
	try {
	    // 创建连接
	    conn = JdbcUtils.getConnection();

	    // 创建语句
	    st = conn.createStatement();

	    // 执行语句
	    String sql = "update user set salary=salary+100";
	    int i = st.executeUpdate(sql);

	    // 处理结果
	    System.out.print("i=" + i);
	} finally {
	    JdbcUtils.free(rs, st, conn);
	}

    }
    
    /** 
    * @Title: delete 
    * @Description: TODO  
    * 删除数据库的记录
    * @throws SQLException
    */
    static void delete() throws SQLException {
 	Connection conn = null;
 	Statement st = null;
 	ResultSet rs = null;
 	try {
 	    // 创建连接
 	    conn = JdbcUtils.getConnection();

 	    // 创建语句
 	    st = conn.createStatement();

 	    // 执行语句
 	    String sql = "delete from user where salary2<100";
 	    int i = st.executeUpdate(sql);

 	    // 处理结果
 	    System.out.print("i=" + i);
 	} finally {
 	    JdbcUtils.free(rs, st, conn);
 	}

     }

}
