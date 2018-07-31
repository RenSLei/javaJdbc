package priv.rsl.jdbc.dao.refactor.byExtends;

import java.sql.ResultSet;
import java.sql.SQLException;

import priv.rsl.jdbc.dao.UserDao;
import priv.rsl.jdbc.domain.User;


/** 
* @ClassName: UserDaoImpl 
* @Description: TODO  
* daomain里面是user.java；
* dao里面是对象的方法，是接口；
* 而接口的实现就是UserDaoJdbcImpl.java做的事，发现此类要实现接口的所有方法（CRUD）;
* 
* 我们仔细观察发现，增 、删、改   的方法里都有许多重复的代码，
* 	所以可以将这些共同的代码抽象在一个超类中的方法，让这个类去实现超类，而不同的地方通过参数传递即可
* 		这样，提高了代码的复用性，其实增删改都可以通过update方法来实现。
* 
* 1、首先通过一个update方法就可以完成对任意一个数据库的表的增删改:
* 	而对不同的表而言，只需要XXX(domain类)的XXXImpl(实现XXXDao接口)继承这个超类AbstractDao即可，
* 	通过传递sql语句以及对应的参数。
* 
* 2、其次比较复杂的是查询这个操作，因为涉及的是对变化结果的处理，而抽象类是不会处理具体的类的信息，
* 	且它也不知道该如何处理，那么如何来完成这样的操作呢？详见方法find()
* 
* 
* 注：要使用这个UserDaoImpl类(而不是之前的UserDaoJdbcImpl)去完成生产UserDao对象的话，
* 就必须更改配置文件中的UserDaoClass的值为当前类的路径
* 
* @author rsl
* @date 2018年4月4日 
*  
*/
public class UserDaoImpl extends AbstractDao implements UserDao{
    
    /*
    * Title: update
    * Description: 
    * 重写的UserDao的方法，更新数据库的数据，使用的是超类的方法update()
    * 
    * @param user 
    * @see priv.rsl.jdbc.dao.UserDao#update(priv.rsl.jdbc.domain.User) 
    */
    @Override
    public void update(User user) {
	
	Object[] args = new Object[] {user.getName(),user.getBirthday().getTime(),
		user.getMoney(),user.getMoney(),user.getId()};
	String sql = "update user set name=?,birthday=?,money=? where id=?";
	super.update(sql, args);
	
    }

    /*
    * Title: delete
    * Description: 
    * 重写的UserDao的方法，删除数据库的记录，使用的是超类的方法update()
    * @param user 
    * @see priv.rsl.jdbc.dao.UserDao#delete(priv.rsl.jdbc.domain.User) 
    */
    @Override
    public void delete(User user) {
	Object[] args = new Object[] {user.getId()};
	String sql = "delete from user where id=?";
	super.update(sql, args);
    }
    
    /*
    * Title: addUser
    * Description: 
    * 重写的UserDao的方法，在数据库中的插入记录，使用的是超类的方法update()
    * @param user 
    * @see priv.rsl.jdbc.dao.UserDao#addUser(priv.rsl.jdbc.domain.User) 
    */
    @Override
    public void addUser(User user) {
	
	Object[] args = new Object[] { user.getName(),user.getPassword(),user.getBirthday().getTime(),
		user.getMoney()};
	String sql = "insert into user(name,password,birthday,money) values (?,?,?,?)";
	super.update(sql, args);
    }

    
    
    /*
    * Title: getUser
    * Description: 
    * 见findUser()
    * 
    * @param userId
    * @return 
    * @see priv.rsl.jdbc.dao.UserDao#getUser(int) 
    */
    @Override
    public User getUser(int userId) {
	// TODO Auto-generated method stub
	return null;
    }

    /*
    * Title: findUser
    * Description: 
    * 根据用户名和密码查找user
    * 
    * 和其他增删改操作不同，查询需要处理结果，将查到的结果封装成一个User对象，返回。
    * 
    * @param loginName
    * @param password
    * @return 
    * @see priv.rsl.jdbc.dao.UserDao#findUser(java.lang.String, java.lang.String) 
    */
    @Override
    public User findUser(String loginName, String password) {
	
	Object[] args = new Object[] {loginName,password};
	String sql = "select id,name,password,birthday,money from user where name =? and password =?";
	Object user = super.find(sql, args);
	return (User)user;
    }

    /*
    * Title: rowMapping
    * Description: 
    * 此方法是重写了超类的处理结果集的rowMapper()方法，因为超类是处理不了具体的结果集的，只有子类自己来处理
    * 
    * 与超类的find()方法结合使用
    * 
    * 这种方法称为模板设计模式，即完成一件事有多个步骤，其中有些步骤相同，有些步骤不同，将相同的步骤抽取出来
    * 	形成超类，不同的步骤的信息通过参数传递，当其他事情想要完成类似的操作的时候就只需要继承这个超类就可以了
    * 	然而，有时候这个超类里一些步骤，超类无法完成，必须交给子类去完成，这时候就定义抽象方法，
    * 	交给子类去重写该方法，这样就达到了目的。
    * @param rs
    * @return 
    * @see priv.rsl.jdbc.dao.refactor.AbstractDao#rowMapping(java.sql.ResultSet) 
    */
    @Override
    protected Object rowMapper(ResultSet rs) throws SQLException {
	User user = new User();
	user.setId(rs.getInt("id"));
	user.setName(rs.getString("name"));
	user.setPassword(rs.getString("password"));
	user.setBirthday(rs.getDate("birthday"));
	user.setMoney(rs.getFloat("money"));
	
	return user;
    }
    
    

}





