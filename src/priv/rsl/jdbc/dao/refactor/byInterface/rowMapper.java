package priv.rsl.jdbc.dao.refactor.byInterface;

import java.sql.ResultSet;
import java.sql.SQLException;

/** 
* @ClassName: rowMapper 
* @Description: TODO  
* �Խ��������ƥ���еĽӿ�
* 
* @author rsl
* @date 2018��4��13�� 
*  
*/
public interface rowMapper {

    public Object mapRow(ResultSet rs)throws SQLException;
}
