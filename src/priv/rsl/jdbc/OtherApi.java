package priv.rsl.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

/** 
* @ClassName: OtherApi 
* @Description: TODO  
* 
* 介绍jdbc的接口的功能：取出当前插入记录的主键id值并赋给当前的主键
* 也可以通过在数据库mysql中写存储过程，然后调用，这样比较麻烦，对sql语句要求较高，jdbc提供了一个api可以较容易的实现：
* 通过一个创建记录的例子来说明这个过程：
* 	ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
* 	说明：对于mysql来说可以不用加后面的参数就可以拿到主键，但是对于其他的数据库或者驱动就不一定了，所以还是加上这个参数比较有保证
* 
* 	以及int id=0;
	    rs=ps.getGeneratedKeys();
	    while(rs.next()) {
		id=rs.getInt(1);
	    }
	    将主键拿出来
	    
	测试：原始表的数据：
        +----+----------+----------+------------+-------+
        | id | name     | password | birthday   | money |
        +----+----------+----------+------------+-------+
        |  1 | rsl      | 111      | 1993-03-21 |   300 |
        |  2 | wangwu   | 222      | 1994-04-05 |   250 |
        |  3 | zhangsan | 333      | 1995-03-21 |   200 |
        |  4 | update   | 444      | 1996-03-21 |   100 |
        +----+----------+----------+------------+-------+
        
       执行后：
       	返回：id=5
       	表的数据：
        +----+----------+--------------+------------+-------+
        | id | name     | password     | birthday   | money |
        +----+----------+--------------+------------+-------+
        |  1 | rsl      | 111          | 1993-03-21 |   300 |
        |  2 | wangwu   | 222          | 1994-04-05 |   250 |
        |  3 | zhangsan | 333          | 1995-03-21 |   200 |
        |  4 | update   | 444          | 1996-03-21 |   100 |
        |  5 | get_key  | key_password | 2018-03-23 |   500 |
        +----+----------+--------------+------------+-------+
        
        所以在实现UserDao的时候，可以将addUser()方法获取到的id赋给其参数的User，这样就保证了每一个对象在java中都是有id的
* 
* 
* @author rsl
* @date 2018年3月23日 
*  
*/
public class OtherApi {

    public static void main(String[] args) throws SQLException {
	System.out.println("id="+create());
    }

    static int create() throws SQLException {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    // 创建连接
	    conn = JdbcUtils.getConnection();

	    String sql = "insert into user (name,password,birthday,money) values (?,?,?,?)";
	    
	    //对于mysql来说可以不用加后面的参数就可以拿到主键，但是对于其他的数据库或者驱动就不一定了，所以还是加上这个参数比较有保证
	    ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
	    
	    //设置值
	    ps.setString(1,"get_key");
	    ps.setString(2,"key_password" );
	    ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
	    ps.setFloat(4, 500.0f);

	    ps.executeUpdate();

	    //将主键拿出来
	    int id=0;
	    rs=ps.getGeneratedKeys();
	    while(rs.next()) {
		id=rs.getInt(1);
	    }
	    return id;

	} finally {
	    JdbcUtils.free(rs, ps, conn);
	}

    }    
}
