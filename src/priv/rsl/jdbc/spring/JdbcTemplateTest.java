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
* ʹ��spring������jar�����������ݿ�Ĳ���������jar���ֱ��ǣ�spring-jdbc.jar��commons-logging.jar
* ���ǵ�ԭ��ʵ�ʾ��Ƕ�jdbc�ķ�װ��Ҳ�����൱�������Լ�ͨ������ӿڣ����ӿڵ�ʵ����Ϊ��������������һ��
* 
* ������ܡ���������spring������JdbcTemplate����������ݿ��ѯ�������޸ĵȷ�������Ҫע�⣺
* 1��sql���Ҫ��java������Զ�Ӧ�������Լ���ѯ�ķ�������Ҫ�ͷ�����Ӧ������
* 2�����ݿ�����������淶��java������Թ淶��ͬ������sql�����ʹ�ñ������в�ѯ��
* 
* @author rsl
* @date 2018��4��17�� 
*  
*/
public class JdbcTemplateTest {
    
  //new�ù�����Ķ���,��Ҫ����Դ��Ϊ����
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
    * ͨ���û�������User����
    * ʹ��spring�Ĺ������queryForObject(sql, args, new RowMapper<T>()��������ɣ�
    * ���Լ������DaoTemplate���ƣ���Ҳ��ͨ����ӳ�����ӿ���ʵ�־���Ĳ�����
    * 
    * @param name
    * @return
    */
    static User findUser(String name) {
	
	//�÷�������Ҫ�Ĳ���
	Object[] args = new Object[] {name};
	
	//����sql���
	String sql = "select id,name,password,birthday,money from user where name =?";
	
	//���÷�������д�ӿڵķ���
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
    * ��������������ӳ������ʵ�֣�ֻ��Ҫ����Ӧ�����Class��Ϊ�������ɣ������Ͳ���д�����ʵ��
    * Ч����findUserЧ��һ��
    * @param name
    * @return
    */
    static User findUser1(String name) {
	//�÷�������Ҫ�Ĳ���
	Object[] args = new Object[] {name};
	
	//����sql���
	String sql = "select id,name,password,birthday,money from user where name =?";
	
	//���÷�������д�ӿڵķ���
	Object user = jdbc.queryForObject(sql, args,new BeanPropertyRowMapper<>(User.class) );
	return (User)user;
    }
    
    /** 
    * @Title: findUser2 
    * @Description: TODO  
    * �������е�������ѯ��������ѯ�������
    * 
    * @param id
    * @return
    */
    static List<User> findUser2(int id) {
	//�÷�������Ҫ�Ĳ���
	Object[] args = new Object[] {id};
	
	//����sql���
	String sql = "select id,name,password,birthday,money from user where id<?";
	
	//���÷�������д�ӿڵķ���
	List<User> users = jdbc.query(sql, args,new BeanPropertyRowMapper<>(User.class) );
	return users;
    }
    
    /** 
    * @Title: getUserCount 
    * @Description: TODO  
    * ��ѯ�ж�������¼��������ֵ������Ϊ����
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
    *��ѯһ���������һ��user��¼��ÿһ�е�������Ϊ�����е�ֵ��Ϊֵ�����һ��map�����з��ء�
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
    *ͬ���ģ�Ҳ��queryForList���������������¼��װ�ɶ��Map�����У����ص���List���ϣ�Ԫ�ؾ���Map
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
    *֮ǰ�����ǵ�ʵ����������д��addUser�ķ�����Ҫ�����ӵĸü�¼��idֵ�ó��ಢ�Ҹ�����������Խ��б���
    *ͨ��JdbcTemplate�����ʵ���أ�
    *����������Ҫ�����Ĵ���ʱ���ù�����������ṩ�˸�Ϊ���Ĵ���ʽ�����õ���
    * @param user
    * @return
    */

    static int addUser(final User user) {
	int i = jdbc.execute(new ConnectionCallback<Integer>() {
	    
	    //��ȡ����
	    @Override
	    public Integer doInConnection(Connection con) throws SQLException, DataAccessException {
		String sql = "insert into user(name,password,birthday,money) values (?,?,?,?)";
		PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		ps.setString(1, user.getName());
		ps.setString(2, user.getPassword());
		// ע�⣬sql�����date��user���Date��(util)������,so,Ҫת��
		ps.setDate(3, new java.sql.Date(user.getBirthday().getTime()));

		ps.setFloat(4, user.getMoney());

		ps.executeUpdate();

		ResultSet rs = ps.getGeneratedKeys();
		while (rs.next()) {
		    // userԭ��û��id��������
		    user.setId(rs.getInt(1));
		}
		return 1;
	    }
	});
	
	return i;
    }



}
