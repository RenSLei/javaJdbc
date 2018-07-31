package priv.rsl.jdbc.dao;

import java.util.Date;

import priv.rsl.jdbc.dao.impl.UserDaoJdbcImpl;//ʵ������ò��������־���ʵ�ֵ��࣬Ϊ���Ժ������޸ĸ����
import priv.rsl.jdbc.domain.User;

public class UserDaoTest {

    /** 
    * @Title: main 
    *
    * @Description: TODO  
    * �������ּܹ���
    * 		User.java:domain����������ʵ�壬��ĳ��������Щ���ԣ���Ҫ��������ݿ��еĶ���
    * 		UserDao.java:�ӿڣ�������ҵ���߼���ʹ�õķ��������ӿ�
    * 		UserDaoJdbcImpl:�ӿڵľ���ʵ�֣���������jdbcʵ�ֽӿڵĶ���
    * ȱ�㣺
    * 	��������Ϊֱ�ӳ����˾����ʵ���ࣺUserDaoJdbcImpl();
	���Ժ��ò�ͬ��ʵ�ֵ�ʱ�򣬴˴���Ҫ�޸ģ���ô����ô��벻���޸ľ������ò�ͬ��ʵ��ȥ�������ݿ⣿
	����ķ����뿴UserDaoTest2.java
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
//	findUser��
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
