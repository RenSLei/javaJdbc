package priv.rsl.jdbc.dao.refactor.byInterface;

import java.sql.ResultSet;
import java.sql.SQLException;

/** 
* @ClassName: rowMapper 
* @Description: TODO  
* 对结果集进行匹配行的接口
* 
* @author rsl
* @date 2018年4月13日 
*  
*/
public interface rowMapper {

    public Object mapRow(ResultSet rs)throws SQLException;
}
