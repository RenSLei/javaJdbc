package priv.rsl.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import priv.rsl.jdbc.domain.User;

public class ORMUserTest {

    public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
	User user = getUser("select id as Id, name as Name, password as Password, birthday as Birthday, "
		+ "money as Money from user where id = 1");
	System.out.println(user);
    }

    /** 
    * @Title: getUser 
    * @Description: TODO  
    * 利用反射的技术，将查询结果封装成特定的对象返回，即查询的数据中的每一条记录都是一个对象，在这里先假定为User对象
    * 先利用一个具体的对象来说明这种思想，即将数据库中的数据映射到具体的对象中去。
    * 映射的依据是，假定数据库中的字段对应对象中的各个属性，且有set和get方法，
    * 过程：
    * 1、连接、使用别名查询，为了和对象的方法对应起来、查询、获取结果集rs
    * 2、将数据库中的每一个字段的名字放到一个字符串数组中，
    * 3、每一个记录就是一个对象，遍历每一个字段，并用set+别名的方式和对象的方法进行一一比对，一旦匹配成功，就调用该方法，从而给对象初始化
    * 
    * @param sql sql语句
    * @return
     * @throws SQLException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
    */
    static User getUser(String sql) throws SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	try {
	    conn = JdbcUtils.getConnection();
	    ps=conn.prepareStatement(sql);
	    rs=ps.executeQuery();
	    
	    //将列名封装到一个字符串数组中
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int count = rsmd.getColumnCount();
	    String[] colNames = new String[count];
	    for (int i = 0; i < count; i++) {
		colNames[i]=rsmd.getColumnLabel(i+1);
	    }
	    
	    //将数据库中的每一个字段的值赋给一个user 对象，并获得该初始化的对象
	    User user = null;
	    if(rs.next()) {
		user = new User();
		Method[] ms = user.getClass().getMethods(); //与user.getClass().getDeclaredMethods()的区别：前者指公共方法，后者获取本类所有方法
		for (int i = 0; i < colNames.length; i++) {
		    String colName = colNames[i];
		    String setMethod ="set"+colName;
		    
		    //遍历user 里面的所有方法，当方法与设定的方法相同时，将该列对应的值传递给该方法，从而将该对象初始化
		    for (Method m : ms) {
			if(setMethod.equals(m.getName())) {
			    m.invoke(user, rs.getObject(colName));
			}
		    }
		}
	    }
	    return user;
	} 
	finally {
	    JdbcUtils.free(rs, ps, conn);
	}
    }
}
