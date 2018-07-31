package priv.rsl.jdbc.spring;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import priv.rsl.jdbc.JdbcUtils;
import priv.rsl.jdbc.domain.User;

/** 
* @ClassName: NamedJdbcTemplate 
* @Description: TODO  
* 支持命名参数的JdbcTemplate
* 
* 为什么有此类？因为：在JdbcTemplate类中，当sql语句的参数有多个时，调用方法传递参数是通过Object数组来传递的
* 这就要求数组的元素顺序与sql中的参数顺序一一对应，比较容易出错，而且不方便
* 
* 所以就出现了  这样一个类，这个类其实是包装了JdbcTemplate的，只是在参数这一块将原来sql语句中的参数命名
* 再将参数和对应的值对应起来封装为：
* 1、map作为参数，需要手动的将key（sql中的参数别名）和value（对象中对应属性的值）添加到一个map中去，再传递这个map
* 2、或者将其封装为其他参数源。参数源是通过一个接口的实现，其中一个实现就是Bean属性的参数源，
* 	它可以在对象中去根据sql语句中的参数名字（这时候这个名字必须和对象中的属性名字一样）将这个属性
* 	的值拿出来赋给sql语句中的参数，这样作为一个参数源来传递）
* 
* @author rsl
* @date 2018年4月20日 
*  
*/
public class NamedJdbcTemplate {
    
    static NamedParameterJdbcTemplate named = 
	    new NamedParameterJdbcTemplate(JdbcUtils.getDataSource());
    
    public static void main(String[] args) {
	
    }
    //用一个查找用户的方法说明：
    /** 
    * @Title: findUser 
    * @Description: TODO  
    * 使用带命名的参数的类:namedParameterJdbcTemplate来实现查询功能
    * 使用手动创建的map集合作为参数之一的方式，在sql中使用：+别名的方式，以及map中使用<别名,值>
    * 
    * @param user
    * @return
    */
    static User findUser(User user) {

	// 参数sql语句
	String sql = "select id,name,password,birthday,money from user where id<:id and money>:m";
	
	//将别名和值封装为Map集合，作为参数
	Map<String,Object> map = new HashMap<String,Object>();
	map.put("id", user.getId());
	map.put("m", user.getMoney());
	
	// 调用查询方法，将map作为参数
	Object u = named.queryForObject(sql, map, new BeanPropertyRowMapper<>(User.class));
	return (User)u;
    }
    
    /** 
    * @Title: findUser1 
    * @Description: TODO  
    * 使用带命名的参数的类:namedParameterJdbcTemplate来实现查询功能
    * 使用参数源的一个实现Bean参数源实现，作为参数之一的方式。严格要求sql中使用  ：+ 对象属性名
    * 
    * @param user
    * @return
    */
    static User findUser1(User user) {
  	// 参数sql语句
  	String sql = "select id,name,password,birthday,money from user where id<:id and money>:money";
  	
  	//创建参数源的一个实现实例,其内部是在参数中通过反射技术来将sql中需要的属性的值拿出来
  	SqlParameterSource sps = new BeanPropertySqlParameterSource(user);
  	
  	// 调用查询方法，Bean属性参数源作为参数
  	Object u = named.queryForObject(sql, sps, new BeanPropertyRowMapper<>(User.class));
  	return (User)u;

      }
    
    /** 
    * @Title: addUser 
    * @Description: TODO  
    * 添加记录的方法，并通过方法获取该记录的主键值
    * @param user
    */
    static void addUser(User user) {
  	// 参数sql语句
  	String sql ="insert into user(name,password,birthday,money) values (:name,:password,:birthday,:money)";
  	
  	//创建参数源的一个实现实例,其内部是在参数中通过反射技术来将sql中需要的属性的值拿出来
  	SqlParameterSource sps = new BeanPropertySqlParameterSource(user);
  	
  	// 调用查询方法，将主键拿出来
  	KeyHolder keyHolder = new GeneratedKeyHolder();
  	named.update(sql, sps,keyHolder);
  	
  	//获得该添加记录的主键值
  	int id = keyHolder.getKey().intValue();
  	user.setId(id);
  	
//  	如果该主键是复合主键，那么keyHolder提供了方法：keyHolder.getKeys();该方法是map集合，key是列名，value是列的值。
//  	Map<String, Object> map = keyHolder.getKeys();

      }

}
