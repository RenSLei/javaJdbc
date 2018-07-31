package priv.rsl.jdbc.dao;

import java.util.Date;

import com.mysql.jdbc.authentication.MysqlClearPasswordPlugin;

import priv.rsl.jdbc.domain.User;

public class UserDaoTest2 {

    /** 
    * @Title: main 
    *
    * @Description: TODO  
    * 测试这种架构：
    * 		User.java:domain对象：里面存放实体，即某种类有哪些属性，需要存放在数据库中的对象
    * 		UserDao.java:接口：里面是业务逻辑层使用的方法名，没有实体的接口
    * 		UserDaoJdbcImpl:接口的具体实现：里面是用jdbc实现接口的对象。
    * 改进：利用工厂模式生产出符合要求的类的实例：
    * 		1、写一个工厂类来实现通过一个配置文件获取该类的实例
    * 		2、然后在此程序中调用工厂模式的方法就可以获取实例了
    * 		3、工厂模式一般使用单例模式，具体请参见DaoFactory.java
    * 		4、在UserDaoJdbcImpl中加了获取主键并赋值到参数user中的id后，可以直接获取该记录的主键值，这是很重要的	
    *
    * @param args
    *
    *
    */
    public static void main(String[] args) {
	
	UserDao userDao =DaoFactory.getInstance().getUserDao();
	User user = new User();
	user.setBirthday(new Date());
	user.setMoney(1000.0f);
	user.setName("UserDaoSpringImpl_Test");
	user.setPassword("****");
	
//	add:
//	userDao.addUser(user);
//	
//	findUser：
	User u = userDao.findUser(user.getName(),user.getPassword());
	System.out.println(u);
	
	//4:
	//现在就能获取到该对象插入后的id值了。该id值是很重要的。
//	System.out.println(user.getId());

//	update:
//	User uget = userDao.getUser(2);
//	uget.setName("updateBySpring");
//	userDao.update(uget);
	
//	delete:
//	userDao.delete(userDao.getUser(11));
    }

}
