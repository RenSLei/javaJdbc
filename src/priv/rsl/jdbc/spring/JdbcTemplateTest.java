package priv.rsl.jdbc.spring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import priv.rsl.jdbc.JdbcUtils;
import priv.rsl.jdbc.domain.User;

/** 
* @ClassName: JdbcTemplateTest 
* @Description: TODO  
* 使用spring的两个jar包来进行数据库的操作。两个jar包分别是：spring-jdbc.jar和commons-logging.jar
* 他们的原理实际就是对jdbc的封装，也就是相当于我们自己通过定义接口，将接口的实现作为参数来处理结果集一样
* 
* 该类介绍、并测试了spring框架里的JdbcTemplate工具类对数据库查询、增加修改等方法，需要注意：
* 1、sql语句要和java类的属性对应起来，以及查询的返回类型要和方法对应起来。
* 2、数据库的列名命名规范和java类的属性规范不同可以在sql语句中使用别名进行查询。
* 
* @author rsl
* @date 2018年4月17日 
*  
*/
public class JdbcTemplateTest {
    
  //new该工具类的对象,需要数据源作为参数
  static JdbcTemplate jdbc = new JdbcTemplate(JdbcUtils.getDataSource());
  	
    public static void main(String[] args) {
//	User user = findUser1("rsl");
//	System.out.println(user);
//	
//	System.out.println(findUser2(3));
//	
//	System.out.println(getUserCount());
//	
//	System.out.println(getDataMap(1));
//	
//	System.out.println(getDataList(5));
	
	User user = new User();
	user.setBirthday(new Date());
	user.setMoney(1000.0f);
	user.setName("addUserBySpring");
	user.setPassword("****");
	addUser(user);
	
    }
    
    /** 
    * @Title: findUser 
    * @Description: TODO  
    * 通过用户名查找User对象：
    * 使用spring的工具类的queryForObject(sql, args, new RowMapper<T>()方法来完成，
    * 与自己定义的DaoTemplate相似，即也是通过行映射器接口来实现具体的操作。
    * 
    * @param name
    * @return
    */
    static User findUser(String name) {
	
	//该方法所需要的参数
	Object[] args = new Object[] {name};
	
	//参数sql语句
	String sql = "select id,name,password,birthday,money from user where name =?";
	
	//调用方法并重写接口的方法
	Object user = jdbc.queryForObject(sql, args, new RowMapper<Object>() {

	    @Override
	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setName(rs.getString("name"));
		user.setPassword(rs.getString("password"));
		user.setBirthday(rs.getDate("birthday"));
		user.setMoney(rs.getFloat("money"));
		
		return user;
	    }

	});
	return (User)user;
    }
    
    /** 
    * @Title: findUser1 
    * @Description: TODO  
    * 工具类里面有行映射器的实现，只需要把相应的类的Class作为参数即可，这样就不用写具体的实现
    * 效果和findUser效果一样
    * @param name
    * @return
    */
    static User findUser1(String name) {
	//该方法所需要的参数
	Object[] args = new Object[] {name};
	
	//参数sql语句
	String sql = "select id,name,password,birthday,money from user where name =?";
	
	//调用方法并重写接口的方法
	Object user = jdbc.queryForObject(sql, args,new BeanPropertyRowMapper<>(User.class) );
	return (User)user;
    }
    
    /** 
    * @Title: findUser2 
    * @Description: TODO  
    * 工具类中的其他查询方法：查询多个对象
    * 
    * @param id
    * @return
    */
    static List<User> findUser2(int id) {
	//该方法所需要的参数
	Object[] args = new Object[] {id};
	
	//参数sql语句
	String sql = "select id,name,password,birthday,money from user where id<?";
	
	//调用方法并重写接口的方法
	List<User> users = jdbc.query(sql, args,new BeanPropertyRowMapper<>(User.class) );
	return users;
    }
    
    /** 
    * @Title: getUserCount 
    * @Description: TODO  
    * 查询有多少条记录，将返回值类型作为参数
    * 
    * @return
    */
    static int getUserCount() {
	String sql ="select count(*) from user;";
	return jdbc.queryForObject(sql, Integer.class);
    }
    
    /** 
    * @Title: getData 
    * @Description: TODO  
    *查询一个结果，即一个user记录，每一列的列名作为键，列的值作为值来存进一个map集合中返回。
    *
    * @param id
    * @return
    */
    static Map getDataMap(int id) {
	String sql = "select id,name,password,birthday,money from user where id="+id;
	return jdbc.queryForMap(sql);
    }
    
    /** 
    * @Title: getData 
    * @Description: TODO  
    *同样的，也有queryForList方法，即将多个记录封装成多个Map集合中，返回的是List集合，元素就是Map
    * @param id
    * @return
    */
    static List getDataList(int n) {
   	String sql = "select id,name,password,birthday,money from user where id<"+n;
   	return jdbc.queryForList(sql);
       }
    
    /** 
    * @Title: addUser 
    * @Description: TODO  
    *之前在我们的实现类里面有写过addUser的方法，要求将增加的该记录的id值拿出类并且赋给对象的属性进行保存
    *通过JdbcTemplate类如何实现呢？
    *即当我们想要更灵活的处理时，该工具类给我们提供了更为灵活的处理方式，即拿到该
    * @param user
    * @return
    */

    static int addUser(final User user) {
	int i = jdbc.execute(new ConnectionCallback<Integer>() {
	    
	    //获取连接
	    @Override
	    public Integer doInConnection(Connection con) throws SQLException, DataAccessException {
		String sql = "insert into user(name,password,birthday,money) values (?,?,?,?)";
		PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		ps.setString(1, user.getName());
		ps.setString(2, user.getPassword());
		// 注意，sql里面的date是user里的Date类(util)的子类,so,要转换
		ps.setDate(3, new java.sql.Date(user.getBirthday().getTime()));

		ps.setFloat(4, user.getMoney());

		ps.executeUpdate();

		ResultSet rs = ps.getGeneratedKeys();
		while (rs.next()) {
		    // user原来没有id，现在有
		    user.setId(rs.getInt(1));
		}
		return 1;
	    }
	});
	
	return i;
    }



}
