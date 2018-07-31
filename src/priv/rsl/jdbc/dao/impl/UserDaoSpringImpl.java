package priv.rsl.jdbc.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import priv.rsl.jdbc.JdbcUtils;
import priv.rsl.jdbc.dao.UserDao;
import priv.rsl.jdbc.domain.User;

/** 
* @ClassName: UserDaoSpringImpl 
* @Description: TODO  
* ����spring���NamedParameterJdbcTemplate��ʵ��UserDao�ӿ�,���ԱȽϴ������
* 
* @author rsl
* @date 2018��4��22�� 
*  
*/
public class UserDaoSpringImpl implements UserDao {
    
    private NamedParameterJdbcTemplate  namedJdbcTemplate = 
	    new NamedParameterJdbcTemplate(JdbcUtils.getDataSource());

    /*
    * Title: addUser
    * Description: ����update������ʵ��
    * 
    * @param user 
    * @see priv.rsl.jdbc.dao.UserDao#addUser(priv.rsl.jdbc.domain.User) 
    */
    @Override
    public void addUser(User user) {
	String sql = 
		"insert into user(name,password,birthday,money) values"
		+ " (:name,:password,:birthday,:money)";
	SqlParameterSource sqs = new BeanPropertySqlParameterSource(user);
	KeyHolder keyHolder = new GeneratedKeyHolder();
	this.namedJdbcTemplate.update(sql, sqs, keyHolder);
	
	user.setId(keyHolder.getKey().intValue());
    }

    @Override
    public User getUser(int userId) {
	String sql = "select id,name,password,birthday,money from user where id=:id";
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("id", userId);
	return this.namedJdbcTemplate.queryForObject(sql, map, new BeanPropertyRowMapper<User>(User.class));
	
    }

    @Override
    public User findUser(String loginName, String password) {
	String sql = 
		"select id,name,password,birthday,money from user where name =:name and passWord =:password";
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("name", loginName);
	map.put("password", password);
	return this.namedJdbcTemplate.queryForObject(sql, map, new BeanPropertyRowMapper<User>(User.class));
    }

    @Override
    public void update(User user) {
	String sql = "update user set name=:name,birthday=:birthday,money=:money where id=:id";
	SqlParameterSource sqs = new BeanPropertySqlParameterSource(user);
	this.namedJdbcTemplate.update(sql, sqs);
    }

    @Override
    public void delete(User user) {
	String sql = "delete from user where id=:id";
	SqlParameterSource sqs = new BeanPropertySqlParameterSource(user);
	this.namedJdbcTemplate.update(sql, sqs);
    }

}
