package priv.rsl.jdbc.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** 
* @ClassName: DaoFactory 
* @Description: TODO
* 设计一个工厂模式：
* 1、采用单例模式，并且不延迟加载
* 	采用私有化构造函数，将代码块放在构造函数里，一类初始化就获取到想要的类
* 2、先创建一个配置文件：daoConfig.properties,里面配置key=UserDaoClass,values=src.priv.rsl.jdbc.dao.impl.UserDaoJdbcImpl
* 3、如何获取配置文件的值？
* 	创建一个文件读取流，将配置文件关联：
* 		1）方法一：InputStream is = new FileInputStream("配置文件的文件路径");
*   		2）*方法二：InputStream is = DaoFactory.class.getClassLodor().getResourcesASStream("文件名");
* 4、使用prop的获取值的方法获取该值
* 5、用反射的方法实现 类的加载：
* 	Class.forName("values").newInstance(); 
* @author rsl
* @date 2018年3月19日 
*  
*/
public class DaoFactory {
    private static UserDao userDao = null;
    //先加载一个本类的实例
    private static DaoFactory instance = new DaoFactory();
    
    private DaoFactory() {
	
	    Properties prop = new Properties();
//	    InputStream ips = new FileInputStream("src/daoConfig.properties");
	    
	    //建议使用该方法：因为该方法不需要具备固定的路径，只要在classpath下就可以加载
	    InputStream ips = DaoFactory.class.getClassLoader().getResourceAsStream("daoConfig.properties");
	    try {
		prop.load(ips);
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    
	    String UserDaoClass = prop.getProperty("UserDaoClass");
	    try {
		userDao = (UserDao) Class.forName(UserDaoClass).newInstance();
	    } catch (InstantiationException e) {
		e.printStackTrace();
	    } catch (IllegalAccessException e) {
		e.printStackTrace();
	    } catch (ClassNotFoundException e) {
		e.printStackTrace();
	    }
	    
	
	
    }
    
    static DaoFactory getInstance() {
	return instance;
    }
    
    public UserDao getUserDao() {
	return userDao;
    }
    
    
}
