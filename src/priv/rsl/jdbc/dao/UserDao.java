package priv.rsl.jdbc.dao;

import priv.rsl.jdbc.domain.User;

/** 
* @ClassName: UserDao 
* @Description: TODO  
* ���UserDao�������ڸ�ҵ���߼����ṩ�Ĺ��ܵĽӿڣ��������û�����ѯ��ע�ᡢ����
* �����ݵĲ�����domain�еľ������,
* ��ҵ���߼�����Ҫ���Ӷ����ʱ���Ҫ���øýӿ��е�addUser������
* 
* @author rsl
* @date 2018��3��18��
*  
*/
public interface UserDao {
    
    public void addUser(User user);
    
    public User getUser(int userId);
    
    public User findUser(String loginName,String password);
    
    public void update(User user);
    
    public void delete(User user);
    
}
