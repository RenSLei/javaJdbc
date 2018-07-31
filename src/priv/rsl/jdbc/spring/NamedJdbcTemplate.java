package priv.rsl.jdbc.spring;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import priv.rsl.jdbc.JdbcUtils;
import priv.rsl.jdbc.domain.User;

/** 
* @ClassName: NamedJdbcTemplate 
* @Description: TODO  
* ֧������������JdbcTemplate
* 
* Ϊʲô�д��ࣿ��Ϊ����JdbcTemplate���У���sql���Ĳ����ж��ʱ�����÷������ݲ�����ͨ��Object���������ݵ�
* ���Ҫ�������Ԫ��˳����sql�еĲ���˳��һһ��Ӧ���Ƚ����׳������Ҳ�����
* 
* ���Ծͳ�����  ����һ���࣬�������ʵ�ǰ�װ��JdbcTemplate�ģ�ֻ���ڲ�����һ�齫ԭ��sql����еĲ�������
* �ٽ������Ͷ�Ӧ��ֵ��Ӧ������װΪ��
* 1��map��Ϊ��������Ҫ�ֶ��Ľ�key��sql�еĲ�����������value�������ж�Ӧ���Ե�ֵ����ӵ�һ��map��ȥ���ٴ������map
* 2�����߽����װΪ��������Դ������Դ��ͨ��һ���ӿڵ�ʵ�֣�����һ��ʵ�־���Bean���ԵĲ���Դ��
* 	�������ڶ�����ȥ����sql����еĲ������֣���ʱ��������ֱ���Ͷ����е���������һ�������������
* 	��ֵ�ó�������sql����еĲ�����������Ϊһ������Դ�����ݣ�
* 
* @author rsl
* @date 2018��4��20�� 
*  
*/
public class NamedJdbcTemplate {
    
    static NamedParameterJdbcTemplate named = 
	    new NamedParameterJdbcTemplate(JdbcUtils.getDataSource());
    
    public static void main(String[] args) {
	
    }
    //��һ�������û��ķ���˵����
    /** 
    * @Title: findUser 
    * @Description: TODO  
    * ʹ�ô������Ĳ�������:namedParameterJdbcTemplate��ʵ�ֲ�ѯ����
    * ʹ���ֶ�������map������Ϊ����֮һ�ķ�ʽ����sql��ʹ�ã�+�����ķ�ʽ���Լ�map��ʹ��<����,ֵ>
    * 
    * @param user
    * @return
    */
    static User findUser(User user) {

	// ����sql���
	String sql = "select id,name,password,birthday,money from user where id<:id and money>:m";
	
	//��������ֵ��װΪMap���ϣ���Ϊ����
	Map<String,Object> map = new HashMap<String,Object>();
	map.put("id", user.getId());
	map.put("m", user.getMoney());
	
	// ���ò�ѯ��������map��Ϊ����
	Object u = named.queryForObject(sql, map, new BeanPropertyRowMapper<>(User.class));
	return (User)u;
    }
    
    /** 
    * @Title: findUser1 
    * @Description: TODO  
    * ʹ�ô������Ĳ�������:namedParameterJdbcTemplate��ʵ�ֲ�ѯ����
    * ʹ�ò���Դ��һ��ʵ��Bean����Դʵ�֣���Ϊ����֮һ�ķ�ʽ���ϸ�Ҫ��sql��ʹ��  ��+ ����������
    * 
    * @param user
    * @return
    */
    static User findUser1(User user) {
  	// ����sql���
  	String sql = "select id,name,password,birthday,money from user where id<:id and money>:money";
  	
  	//��������Դ��һ��ʵ��ʵ��,���ڲ����ڲ�����ͨ�����似������sql����Ҫ�����Ե�ֵ�ó���
  	SqlParameterSource sps = new BeanPropertySqlParameterSource(user);
  	
  	// ���ò�ѯ������Bean���Բ���Դ��Ϊ����
  	Object u = named.queryForObject(sql, sps, new BeanPropertyRowMapper<>(User.class));
  	return (User)u;

      }
    
    /** 
    * @Title: addUser 
    * @Description: TODO  
    * ��Ӽ�¼�ķ�������ͨ��������ȡ�ü�¼������ֵ
    * @param user
    */
    static void addUser(User user) {
  	// ����sql���
  	String sql ="insert into user(name,password,birthday,money) values (:name,:password,:birthday,:money)";
  	
  	//��������Դ��һ��ʵ��ʵ��,���ڲ����ڲ�����ͨ�����似������sql����Ҫ�����Ե�ֵ�ó���
  	SqlParameterSource sps = new BeanPropertySqlParameterSource(user);
  	
  	// ���ò�ѯ�������������ó���
  	KeyHolder keyHolder = new GeneratedKeyHolder();
  	named.update(sql, sps,keyHolder);
  	
  	//��ø���Ӽ�¼������ֵ
  	int id = keyHolder.getKey().intValue();
  	user.setId(id);
  	
//  	����������Ǹ�����������ôkeyHolder�ṩ�˷�����keyHolder.getKeys();�÷�����map���ϣ�key��������value���е�ֵ��
//  	Map<String, Object> map = keyHolder.getKeys();

      }

}
