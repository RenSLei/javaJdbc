package priv.rsl.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ORMTest {

    public static void main(String[] args) throws IllegalAccessException, 
    IllegalArgumentException, InvocationTargetException, SQLException, InstantiationException {
	ORMBean bean = (ORMBean) getObject("select id as Id, name as Name, "
		+ "password as Password, birthday as Birthday, "
		+ "money as Money from user where id = 1",ORMBean.class);
	System.out.println(bean);
    }

    /** 
    * @Title: getUser 
    * @Description: TODO  
    * 
    * ORM:Object Relation Mapping 利用描述对象和数据库之间映射的元数据，
    * 自动且透明地把Java应用程序中的对象持久化到关系数据库中的表。
    * 
    * 映射的依据是，假定数据库中的字段对应对象中的各个属性，且有set和get方法，
    * 且有一个空参数的构造函数（目的是可以直接通过Class反射来newInstance，否则要先通过反射得到构造函数来构造）
    * 过程：
    * 1、连接、使用别名查询，为了和对象的方法对应起来、查询、获取结果集rs
    * 2、将数据库中的每一个字段的名字放到一个字符串数组中，
    * 3、每一个记录就是一个对象，遍历每一个字段，并用set+别名的方式和对象的方法进行一一比对，
    * 	一旦匹配成功，就调用该方法，从而给对象初始化
    * 
    * @param sql sql语句
    * @return
    * @throws SQLException 
    * @throws InvocationTargetException 
    * @throws IllegalArgumentException 
    * @throws InstantiationException 
    */
    static<T> T getObject(String sql,Class<T> clazz) 
	    throws SQLException, IllegalAccessException, 
	    IllegalArgumentException, InvocationTargetException, 
	    InstantiationException {
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
	    T  object = null;
	    if(rs.next()) {
		
		object = clazz.newInstance();
		
		//与user.getClass().getDeclaredMethods()的区别：前者指公共方法，后者获取本类所有方法
		Method[] ms = clazz.getMethods(); 
		for (int i = 0; i < colNames.length; i++) {
		    String colName = colNames[i];
		    String setMethod ="set"+colName;
		    
		    //遍历user 里面的所有方法，当方法与设定的方法相同时，将该列对应的值传递给该方法，从而将该对象初始化
		    for (Method m : ms) {
			if(setMethod.equals(m.getName())) {
			    m.invoke(object, rs.getObject(colName));
			}
		    }
		}
	    }
	    return object;
	} 
	finally {
	    JdbcUtils.free(rs, ps, conn);
	}
    }
}
