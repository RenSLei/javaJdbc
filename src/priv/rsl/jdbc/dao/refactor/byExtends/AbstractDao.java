package priv.rsl.jdbc.dao.refactor.byExtends;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import priv.rsl.jdbc.JdbcUtils;
import priv.rsl.jdbc.dao.DaoException;

/** 
* @ClassName: AbstractDao 
* @Description: TODO  
* 一个抽象的超类，用于提高代码复用性，UserDaoImpl去实现此类即可重复使用很多相同的代码,
* 而不同的地方就通过参数传递
* 
* 
* 
* @author rsl
* @date 2018年4月6日 
*  
*/
public abstract class AbstractDao {
    
    /** 
    * @Title: update 
    * @Description: TODO  
    * 一个与具体的对象没有任何关系的方法(增删改)，可以操作不同的类的对象，只需要改变sql语句以及参数就可以
    * 注意，调用者需要明确参数
    * @param sql
    * @param args
    */
    public int update(String sql,Object[] args) {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    
	    conn = JdbcUtils.getConnection();
	    
	    ps = conn.prepareStatement(sql);
	    
	    //将需要设置的值放在一个Object类型的数组中,循环将值放入sql语句的占位符中
	    for (int i = 0; i < args.length; i++) {
		ps.setObject(i+1, args[i]);
	    }
	    return  ps.executeUpdate();

	} catch (SQLException e) {
	    // 关键
	    throw new DaoException(e.getMessage(), e);
	} finally {
	    JdbcUtils.free(rs, ps, conn);
	}

    }
    
    /** 
    * @Title: find 
    * @Description: TODO  
    * 根据用户名和密码进行查询，返回的是object,但是此个Object在子类的方法mapping()中是知道是什么类，
    * 因为sql语句和参数都是子类写的，子类当然知道如何去处理这个rs结果.
    * 所以，在这里的处理方式就是，定义一个抽象方法，让子类去处理这个结果，然后子类返回具体的对象
    * @param sql
    * @param args
    * @return
    */
    public Object find(String sql, Object[] args) {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    conn = JdbcUtils.getConnection();
	    ps = conn.prepareStatement(sql);
	    
	    // 将需要设置的值放在一个Object类型的数组中,循环将值放入sql语句的占位符中
	    for (int i = 0; i < args.length; i++) {
		ps.setObject(i + 1, args[i]);
	    }
	    rs = ps.executeQuery();
	    Object obj = null;
	    while(rs.next()) {
		obj = rowMapper(rs);
	    }
	    return obj;
	    
	} catch (SQLException e) {
	    // 关键
	    throw new DaoException(e.getMessage(), e);
	} finally {
	    JdbcUtils.free(rs, ps, conn);
	}

    }

   abstract protected Object rowMapper(ResultSet rs) throws SQLException;
}
