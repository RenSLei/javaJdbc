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
     * Title: addUser Description: ��д���� ע�⣺�˴����ص����쳣�Ĵ���
     * SQLException�Ǳ���ʱ�쳣����catch������������Ĺؼ��ǼȲ��ܼ򵥵��׳�����������Ⱦ�˽ӿڣ�Ҳ���ܼ򵥵Ĵ�ӡ��ջ��Ϣ����Ϊ
     * ҵ���߼����ȡ�����ִ���������룬�Ӷ�������س��������������ǽ�����쳣ת��Ϊ����ʱ�쳣��
     * �ڽӿڵİ������һ������ʱ�쳣DaoException�̳�RuntimeException��Ȼ��catchʱ�׳����쳣��
     * ���ҵ���߼�����ÿ��Խ����쳣����� �������ܴ���Ͳ��ùܣ��������׳�
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
	    // ��������
	    conn = JdbcUtils.getConnection();

	    String sql = "insert into user(name,password,birthday,money) values (?,?,?,?)";
	    ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

	    ps.setString(1, user.getName());
	    ps.setString(2, user.getPassword());
	    // ע�⣬sql�����date��user���Date��(util)������,so,Ҫת��
	    ps.setDate(3, new java.sql.Date(user.getBirthday().getTime()));

	    ps.setFloat(4, user.getMoney());

	    ps.executeUpdate();
	    
	    rs=ps.getGeneratedKeys();
	    while(rs.next()) {
		//userԭ��û��id��������
		user.setId(rs.getInt(1));
	    }

	} catch (SQLException e) {
	    // �ؼ�
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
	    // ��������
	    conn = JdbcUtils.getConnection();

	    // �������
	    String sql = "select id,name,password,birthday,money from user where id=?";
	    ps = conn.prepareStatement(sql);
	    ps.setInt(1, userId);

	    // ִ�����
	    rs = ps.executeQuery();

	    // ������
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
	    // ��������
	    conn = JdbcUtils.getConnection();

	    // �������
	    String sql = "select id,name,password,birthday,money from user where name =? and password =?";
	    ps = conn.prepareStatement(sql);
	    ps.setString(1, loginName);
	    ps.setString(2, password);
	    
	    // ִ�����
	    rs = ps.executeQuery();

	    // ������
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
	    // ��������
	    conn = JdbcUtils.getConnection();

	    String sql = "update user set name=?,birthday=?,money=? where id=?";
	    ps = conn.prepareStatement(sql);

	    ps.setString(1, user.getName());
	    ps.setDate(2, new java.sql.Date(user.getBirthday().getTime()));
	    ps.setFloat(3, user.getMoney());
	    ps.setInt(4, user.getId());

	    ps.executeUpdate();

	} catch (SQLException e) {
	    // �ؼ� 
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
