package priv.rsl.jdbc.dao;

import java.util.Date;

import com.mysql.jdbc.authentication.MysqlClearPasswordPlugin;

import priv.rsl.jdbc.domain.User;

public class UserDaoTest2 {

    /** 
    * @Title: main 
    *
    * @Description: TODO  
    * �������ּܹ���
    * 		User.java:domain����������ʵ�壬��ĳ��������Щ���ԣ���Ҫ��������ݿ��еĶ���
    * 		UserDao.java:�ӿڣ�������ҵ���߼���ʹ�õķ�������û��ʵ��Ľӿ�
    * 		UserDaoJdbcImpl:�ӿڵľ���ʵ�֣���������jdbcʵ�ֽӿڵĶ���
    * �Ľ������ù���ģʽ����������Ҫ������ʵ����
    * 		1��дһ����������ʵ��ͨ��һ�������ļ���ȡ�����ʵ��
    * 		2��Ȼ���ڴ˳����е��ù���ģʽ�ķ����Ϳ��Ի�ȡʵ����
    * 		3������ģʽһ��ʹ�õ���ģʽ��������μ�DaoFactory.java
    * 		4����UserDaoJdbcImpl�м��˻�ȡ��������ֵ������user�е�id�󣬿���ֱ�ӻ�ȡ�ü�¼������ֵ�����Ǻ���Ҫ��	
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
//	findUser��
	User u = userDao.findUser(user.getName(),user.getPassword());
	System.out.println(u);
	
	//4:
	//���ھ��ܻ�ȡ���ö��������idֵ�ˡ���idֵ�Ǻ���Ҫ�ġ�
//	System.out.println(user.getId());

//	update:
//	User uget = userDao.getUser(2);
//	uget.setName("updateBySpring");
//	userDao.update(uget);
	
//	delete:
//	userDao.delete(userDao.getUser(11));
    }

}
