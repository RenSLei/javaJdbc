package priv.rsl.jdbc.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** 
* @ClassName: DaoFactory 
* @Description: TODO
* ���һ������ģʽ��
* 1�����õ���ģʽ�����Ҳ��ӳټ���
* 	����˽�л����캯�������������ڹ��캯���һ���ʼ���ͻ�ȡ����Ҫ����
* 2���ȴ���һ�������ļ���daoConfig.properties,��������key=UserDaoClass,values=src.priv.rsl.jdbc.dao.impl.UserDaoJdbcImpl
* 3����λ�ȡ�����ļ���ֵ��
* 	����һ���ļ���ȡ�����������ļ�������
* 		1������һ��InputStream is = new FileInputStream("�����ļ����ļ�·��");
*   		2��*��������InputStream is = DaoFactory.class.getClassLodor().getResourcesASStream("�ļ���");
* 4��ʹ��prop�Ļ�ȡֵ�ķ�����ȡ��ֵ
* 5���÷���ķ���ʵ�� ��ļ��أ�
* 	Class.forName("values").newInstance(); 
* @author rsl
* @date 2018��3��19�� 
*  
*/
public class DaoFactory {
    private static UserDao userDao = null;
    //�ȼ���һ�������ʵ��
    private static DaoFactory instance = new DaoFactory();
    
    private DaoFactory() {
	
	    Properties prop = new Properties();
//	    InputStream ips = new FileInputStream("src/daoConfig.properties");
	    
	    //����ʹ�ø÷�������Ϊ�÷�������Ҫ�߱��̶���·����ֻҪ��classpath�¾Ϳ��Լ���
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
