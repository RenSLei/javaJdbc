package priv.rsl.jdbc.dao;

import priv.rsl.jdbc.domain.User;

/** 
* @ClassName: UserDao 
* @Description: TODO  
* 设计UserDao对象，用于给业务逻辑层提供的功能的接口，如增加用户、查询、注册、更新
* 所传递的参数是domain中的具体对象,
* 当业务逻辑层需要增加对象的时候就要调用该接口中的addUser方法，
* 
* @author rsl
* @date 2018年3月18日
*  
*/
public interface UserDao {
    
    public void addUser(User user);
    
    public User getUser(int userId);
    
    public User findUser(String loginName,String password);
    
    public void update(User user);
    
    public void delete(User user);
    
}
