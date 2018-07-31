package priv.rsl.jdbc.dao.refactor.byExtends;

import java.sql.ResultSet;
import java.sql.SQLException;

import priv.rsl.jdbc.dao.UserDao;
import priv.rsl.jdbc.domain.User;


/** 
* @ClassName: UserDaoImpl 
* @Description: TODO  
* daomain������user.java��
* dao�����Ƕ���ķ������ǽӿڣ�
* ���ӿڵ�ʵ�־���UserDaoJdbcImpl.java�����£����ִ���Ҫʵ�ֽӿڵ����з�����CRUD��;
* 
* ������ϸ�۲췢�֣��� ��ɾ����   �ķ����ﶼ������ظ��Ĵ��룬
* 	���Կ��Խ���Щ��ͬ�Ĵ��������һ�������еķ������������ȥʵ�ֳ��࣬����ͬ�ĵط�ͨ���������ݼ���
* 		����������˴���ĸ����ԣ���ʵ��ɾ�Ķ�����ͨ��update������ʵ�֡�
* 
* 1������ͨ��һ��update�����Ϳ�����ɶ�����һ�����ݿ�ı����ɾ��:
* 	���Բ�ͬ�ı���ԣ�ֻ��ҪXXX(domain��)��XXXImpl(ʵ��XXXDao�ӿ�)�̳��������AbstractDao���ɣ�
* 	ͨ������sql����Լ���Ӧ�Ĳ�����
* 
* 2����αȽϸ��ӵ��ǲ�ѯ�����������Ϊ�漰���ǶԱ仯����Ĵ������������ǲ��ᴦ�����������Ϣ��
* 	����Ҳ��֪������δ�����ô�������������Ĳ����أ��������find()
* 
* 
* ע��Ҫʹ�����UserDaoImpl��(������֮ǰ��UserDaoJdbcImpl)ȥ�������UserDao����Ļ���
* �ͱ�����������ļ��е�UserDaoClass��ֵΪ��ǰ���·��
* 
* @author rsl
* @date 2018��4��4�� 
*  
*/
public class UserDaoImpl extends AbstractDao implements UserDao{
    
    /*
    * Title: update
    * Description: 
    * ��д��UserDao�ķ������������ݿ�����ݣ�ʹ�õ��ǳ���ķ���update()
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
    * ��д��UserDao�ķ�����ɾ�����ݿ�ļ�¼��ʹ�õ��ǳ���ķ���update()
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
    * ��д��UserDao�ķ����������ݿ��еĲ����¼��ʹ�õ��ǳ���ķ���update()
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
    * ��findUser()
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
    * �����û������������user
    * 
    * ��������ɾ�Ĳ�����ͬ����ѯ��Ҫ�����������鵽�Ľ����װ��һ��User���󣬷��ء�
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
    * �˷�������д�˳���Ĵ���������rowMapper()��������Ϊ�����Ǵ����˾���Ľ�����ģ�ֻ�������Լ�������
    * 
    * �볬���find()�������ʹ��
    * 
    * ���ַ�����Ϊģ�����ģʽ�������һ�����ж�����裬������Щ������ͬ����Щ���費ͬ������ͬ�Ĳ����ȡ����
    * 	�γɳ��࣬��ͬ�Ĳ������Ϣͨ���������ݣ�������������Ҫ������ƵĲ�����ʱ���ֻ��Ҫ�̳��������Ϳ�����
    * 	Ȼ������ʱ�����������һЩ���裬�����޷���ɣ����뽻������ȥ��ɣ���ʱ��Ͷ�����󷽷���
    * 	��������ȥ��д�÷����������ʹﵽ��Ŀ�ġ�
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





