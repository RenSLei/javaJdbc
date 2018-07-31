package priv.rsl.jdbc.dao.refactor.byInterface;

import java.sql.ResultSet;
import java.sql.SQLException;

import priv.rsl.jdbc.domain.User;


/** 
* @ClassName: UserDaoImpl 
* @Description: TODO  
* daomain里面是user.java；
* dao里面是对象的方法，是接口；
* 而接口的实现就是UserDaoJdbcImpl.java做的事，发现此类要实现接口的所有方法（CRUD）;
* 
* 我们这个类是假定是UserDao的实现类，就一个find()方法来说明要说明的知识。
* 
* 在refactor包中的两个示例中，我们可以看到，虽然采取继承超类的方法可以提高代码的复用性，但是还是有明显的缺陷，
* 缺陷就是我们的一个sql语句就要对应一个超类中交给子类处理结果集的抽象方法，这样大大降低的代码的灵活性
* 
* 这个包中演示的是另外一种改进方式：
* 
* 1、构造一个功能和AbstractDao功能类似的但非抽象的类MyDaoTemplate，在UserDaoImpl中创建该类的实例，
* 	以调用相应的find方法
* 2、非抽象的类MyDaoTemplate里面的find方法里多了一个参数：接口rowMapper，接口里有一个方法mapRow(rs)，
* 	专门用于匹配结果集里面的行的方法，而这个方法正是模板不知道怎么处理需要交给子类去处理的方法
* 
* 3、所以，在UserDaoImpl中：
* 	a.可以用一个类来实现此接口的这个方法，调用模板里的find方法的时候就该new该实现类的实例即可
* 	b.可以直接传递参数的时候采用匿名类的形式重写接口里面的方法，
* 	这样就显得特别灵活，而不是只会像之前那样,固定的返回一个user对象。
* 	比如我只想根据id获取用户名，直接使用b的形式，具体请看代码。
* 
* 总之，接口作为参数的方式，在模板类(这里是MyDaoTemplate,即代替之前的AbstractDao这个超类的类，但它不是抽象类)
* 中调用该接口的方法，在实现类中调用模板类的对应的方法，将sql语句，参数，接口这个参数采用匿名类的形式传递，
* 这样会大大提高代码的灵活性
* 
* @author rsl
* @date 2018年4月4日 
*  
*/
public class UserDaoImpl {
    MyDaoTemplate template = new MyDaoTemplate();

    /**
     * @Title: findUser
     * @Description: TODO
     * 根据登录名和密码查找(获取)用户
     * 
     * @param loginName
     * @param password
     * @return
     */
    public User findUser(String loginName, String password) {

	Object[] args = new Object[] { loginName, password };
	String sql = "select id,name,password,birthday,money from user where name =? and password =?";
	Object user = this.template.find(sql, args, new rowMapper() {
	    @Override
	    public Object mapRow(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setName(rs.getString("name"));
		user.setPassword(rs.getString("password"));
		user.setBirthday(rs.getDate("birthday"));
		user.setMoney(rs.getFloat("money"));

		return user;
	    }
	});
	return (User) user;
    }

    /** 
    * @Title: getName 
    * @Description: TODO  
    * 当我想根据id去获取用户名，只需要改变实现内容即可
    * 
    * @param loginName
    * @param password
    * @return
    */
    public String getName(int id) {

	Object[] args = new Object[] {id};
	String sql = "select name from user where id =?";
	Object name = this.template.find(sql, args, new rowMapper() {
	    @Override
	    public Object mapRow(ResultSet rs) throws SQLException {
		return rs.getString("name");
	    }
	});
	return (String) name;
    }
}





