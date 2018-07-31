package priv.rsl.jdbc.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import priv.rsl.jdbc.JdbcUtils;
import priv.rsl.jdbc.dao.DaoException;
import priv.rsl.jdbc.dao.UserDao;
import priv.rsl.jdbc.domain.User;

public class UserDaoJdbcImpl implements UserDao {

    /*
     * Title: addUser Description: 重写方法 注意：此处的重点是异常的处理：
     * SQLException是编译时异常必须catch处理，所以问题的关键是既不能简单的抛出，这样就污染了接口，也不能简单的打印堆栈信息，因为
     * 业务逻辑层获取会继续执行其他代码，从而导致相关程序出错，所以最好是将这个异常转换为运行时异常：
     * 在接口的包中设计一个运行时异常DaoException继承RuntimeException，然后catch时抛出此异常：
     * 如果业务逻辑层觉得可以将此异常处理就 处理，不能处理就不用管，就往外抛出
     * 
     * @param user
     * 
     * @see priv.rsl.jdbc.dao.UserDao#addUser(priv.rsl.jdbc.domain.User)
     */
    @Override
    public void addUser(User user) {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    // 创建连接
	    conn = JdbcUtils.getConnection();

	    String sql = "insert into user(name,password,birthday,money) values (?,?,?,?)";
	    ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

	    ps.setString(1, user.getName());
	    ps.setString(2, user.getPassword());
	    // 注意，sql里面的date是user里的Date类(util)的子类,so,要转换
	    ps.setDate(3, new java.sql.Date(user.getBirthday().getTime()));

	    ps.setFloat(4, user.getMoney());

	    ps.executeUpdate();
	    
	    rs=ps.getGeneratedKeys();
	    while(rs.next()) {
		//user原来没有id，现在有
		user.setId(rs.getInt(1));
	    }

	} catch (SQLException e) {
	    // 关键
	    throw new DaoException(e.getMessage(), e);
	} finally {
	    JdbcUtils.free(rs, ps, conn);
	}
    }

    @Override
    public User getUser(int userId) {
	User user = null;
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    // 创建连接
	    conn = JdbcUtils.getConnection();

	    // 创建语句
	    String sql = "select id,name,password,birthday,money from user where id=?";
	    ps = conn.prepareStatement(sql);
	    ps.setInt(1, userId);

	    // 执行语句
	    rs = ps.executeQuery();

	    // 处理结果
	    while (rs.next()) {
		user = mappingUser(rs);

	    }

	} catch (SQLException e) {
	    throw new DaoException(e.getMessage(), e);
	} finally {
	    JdbcUtils.free(rs, ps, conn);
	}

	return user;

    }

    @Override
    public User findUser(String loginName, String password) {
	User user = null;
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    // 创建连接
	    conn = JdbcUtils.getConnection();

	    // 创建语句
	    String sql = "select id,name,password,birthday,money from user where name =? and password =?";
	    ps = conn.prepareStatement(sql);
	    ps.setString(1, loginName);
	    ps.setString(2, password);
	    
	    // 执行语句
	    rs = ps.executeQuery();

	    // 处理结果
	    while (rs.next()) {
		user = mappingUser(rs);
		
	    }

	} catch(SQLException e) {
		throw new DaoException(e.getMessage(),e);
	    }
	finally {
	    JdbcUtils.free(rs, ps, conn);
	}

	return user;
    }

    private User mappingUser(ResultSet rs) throws SQLException {
	User user;
	user = new User();
	user.setId(rs.getInt("id"));
	user.setName(rs.getString("name"));
	user.setBirthday(rs.getDate("birthday"));
	user.setMoney(rs.getFloat("money"));
	user.setPassword(rs.getString("password"));
	return user;
    }

    @Override
    public void update(User user) {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    // 创建连接
	    conn = JdbcUtils.getConnection();

	    String sql = "update user set name=?,birthday=?,money=? where id=?";
	    ps = conn.prepareStatement(sql);

	    ps.setString(1, user.getName());
	    ps.setDate(2, new java.sql.Date(user.getBirthday().getTime()));
	    ps.setFloat(3, user.getMoney());
	    ps.setInt(4, user.getId());

	    ps.executeUpdate();

	} catch (SQLException e) {
	    // 关键 
	    throw new DaoException(e.getMessage(), e);
	} finally {
	    JdbcUtils.free(rs, ps, conn);
	}
    
    }

    @Override
    public void delete(User user) {

	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {
	    conn = JdbcUtils.getConnection();
	    
	    String sql = "delete from user where id=?";

	    ps = conn.prepareStatement(sql);
	    ps.setInt(1, user.getId());
	    ps.executeUpdate();
	} catch (SQLException e) {
	    throw new DaoException(e.getMessage(), e);
	} finally {
	    JdbcUtils.free(rs, ps, conn);
	}
    }
}
