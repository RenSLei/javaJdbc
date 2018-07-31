package priv.rsl.jdbc.dao.refactor.byInterface;

import java.sql.ResultSet;
import java.sql.SQLException;

import priv.rsl.jdbc.domain.User;


/** 
* @ClassName: UserDaoImpl 
* @Description: TODO  
* daomain������user.java��
* dao�����Ƕ���ķ������ǽӿڣ�
* ���ӿڵ�ʵ�־���UserDaoJdbcImpl.java�����£����ִ���Ҫʵ�ֽӿڵ����з�����CRUD��;
* 
* ����������Ǽٶ���UserDao��ʵ���࣬��һ��find()������˵��Ҫ˵����֪ʶ��
* 
* ��refactor���е�����ʾ���У����ǿ��Կ�������Ȼ��ȡ�̳г���ķ���������ߴ���ĸ����ԣ����ǻ��������Ե�ȱ�ݣ�
* ȱ�ݾ������ǵ�һ��sql����Ҫ��Ӧһ�������н������ദ�������ĳ��󷽷���������󽵵͵Ĵ���������
* 
* ���������ʾ��������һ�ָĽ���ʽ��
* 
* 1������һ�����ܺ�AbstractDao�������Ƶĵ��ǳ������MyDaoTemplate����UserDaoImpl�д��������ʵ����
* 	�Ե�����Ӧ��find����
* 2���ǳ������MyDaoTemplate�����find���������һ���������ӿ�rowMapper���ӿ�����һ������mapRow(rs)��
* 	ר������ƥ������������еķ������������������ģ�岻֪����ô������Ҫ��������ȥ����ķ���
* 
* 3�����ԣ���UserDaoImpl�У�
* 	a.������һ������ʵ�ִ˽ӿڵ��������������ģ�����find������ʱ��͸�new��ʵ�����ʵ������
* 	b.����ֱ�Ӵ��ݲ�����ʱ��������������ʽ��д�ӿ�����ķ�����
* 	�������Ե��ر���������ֻ����֮ǰ����,�̶��ķ���һ��user����
* 	������ֻ�����id��ȡ�û�����ֱ��ʹ��b����ʽ�������뿴���롣
* 
* ��֮���ӿ���Ϊ�����ķ�ʽ����ģ����(������MyDaoTemplate,������֮ǰ��AbstractDao���������࣬�������ǳ�����)
* �е��øýӿڵķ�������ʵ�����е���ģ����Ķ�Ӧ�ķ�������sql��䣬�������ӿ���������������������ʽ���ݣ�
* ����������ߴ���������
* 
* @author rsl
* @date 2018��4��4�� 
*  
*/
public class UserDaoImpl {
    MyDaoTemplate template = new MyDaoTemplate();

    /**
     * @Title: findUser
     * @Description: TODO
     * ���ݵ�¼�����������(��ȡ)�û�
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
    * ���������idȥ��ȡ�û�����ֻ��Ҫ�ı�ʵ�����ݼ���
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





