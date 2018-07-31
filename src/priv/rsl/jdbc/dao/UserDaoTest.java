package priv.rsl.jdbc.dao;

import java.util.Date;

import priv.rsl.jdbc.dao.impl.UserDaoJdbcImpl;//实际是最好不出现这种具体实现的类，为了以后代码的修改更灵活
import priv.rsl.jdbc.domain.User;

public class UserDaoTest {

    /** 
    * @Title: main 
    *
    * @Description: TODO  
    * 测试这种架构：
    * 		User.java:domain对象：里面存放实体，即某种类有哪些属性，需要存放在数据库中的对象
    * 		UserDao.java:接口：里面是业务逻辑层使用的方法名，接口
    * 		UserDaoJdbcImpl:接口的具体实现：里面是用jdbc实现接口的对象。
    * 缺点：
    * 	本方法因为直接出现了具体的实现类：UserDaoJdbcImpl();
	当以后用不同的实现的时候，此处需要修改，那么如何让代码不用修改就做到用不同的实现去访问数据库？
	具体的方法请看UserDaoTest2.java
    *
    * @param args
    *
    *
    */
    public static void main(String[] args) {
	
	UserDao userDao = new UserDaoJdbcImpl();
	
	User user = new User();
	user.setBirthday(new Date());
	user.setMoney(1000.0f);
	user.setName("dao name1");
	user.setPassword("555");
	
//	add:
//	userDao.addUser(user);
//	
//	findUser：
	User u = userDao.findUser(user.getName(),user.getPassword());
	System.out.println(u.getName());

//	update:
//	User uget = userDao.getUser(5);
//	uget.setName("update");
//	userDao.update(uget);
	
//	delete:
	userDao.delete(userDao.getUser(5));
    }

}
