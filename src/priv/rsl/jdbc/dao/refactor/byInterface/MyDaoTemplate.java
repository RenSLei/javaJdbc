package priv.rsl.jdbc.dao.refactor.byInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import priv.rsl.jdbc.JdbcUtils;
import priv.rsl.jdbc.dao.DaoException;

/** 
* @ClassName: AbstractDao 
* @Description: TODO  
* һ������ĳ��࣬������ߴ��븴���ԣ�UserDaoImplȥʵ�ִ��༴���ظ�ʹ�úܶ���ͬ�Ĵ���,
* ����ͬ�ĵط���ͨ����������
* 
* 
* 
* @author rsl
* @date 2018��4��6�� 
*  
*/
public class MyDaoTemplate {
    
    /** 
    * @Title: find 
    * @Description: TODO  
    * �����û�����������в�ѯ�����ص���object,���Ǵ˸�Object������ķ���mapping()����֪����ʲô�࣬
    * ��Ϊsql���Ͳ�����������д�ģ����൱Ȼ֪�����ȥ�������rs���.
    * ���ԣ�������Ĵ���ʽ���ǣ�����һ�����󷽷���������ȥ������������Ȼ�����෵�ؾ���Ķ���
    * @param sql
    * @param args
    * @return
    */
    public Object find(String sql, Object[] args,rowMapper rm) {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    conn = JdbcUtils.getConnection();
	    ps = conn.prepareStatement(sql);
	    
	    // ����Ҫ���õ�ֵ����һ��Object���͵�������,ѭ����ֵ����sql����ռλ����
	    for (int i = 0; i < args.length; i++) {
		ps.setObject(i + 1, args[i]);
	    }
	    rs = ps.executeQuery();
	    Object obj = null;
	    while(rs.next()) {
		obj = rm.mapRow(rs);
	    }
	    return obj;
	    
	} catch (SQLException e) {
	    // �ؼ�
	    throw new DaoException(e.getMessage(), e);
	} finally {
	    JdbcUtils.free(rs, ps, conn);
	}

    }
}
