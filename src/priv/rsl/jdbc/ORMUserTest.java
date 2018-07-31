package priv.rsl.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import priv.rsl.jdbc.domain.User;

public class ORMUserTest {

    public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
	User user = getUser("select id as Id, name as Name, password as Password, birthday as Birthday, "
		+ "money as Money from user where id = 1");
	System.out.println(user);
    }

    /** 
    * @Title: getUser 
    * @Description: TODO  
    * ���÷���ļ���������ѯ�����װ���ض��Ķ��󷵻أ�����ѯ�������е�ÿһ����¼����һ�������������ȼٶ�ΪUser����
    * ������һ������Ķ�����˵������˼�룬�������ݿ��е�����ӳ�䵽����Ķ�����ȥ��
    * ӳ��������ǣ��ٶ����ݿ��е��ֶζ�Ӧ�����еĸ������ԣ�����set��get������
    * ���̣�
    * 1�����ӡ�ʹ�ñ�����ѯ��Ϊ�˺Ͷ���ķ�����Ӧ��������ѯ����ȡ�����rs
    * 2�������ݿ��е�ÿһ���ֶε����ַŵ�һ���ַ��������У�
    * 3��ÿһ����¼����һ�����󣬱���ÿһ���ֶΣ�����set+�����ķ�ʽ�Ͷ���ķ�������һһ�ȶԣ�һ��ƥ��ɹ����͵��ø÷������Ӷ��������ʼ��
    * 
    * @param sql sql���
    * @return
     * @throws SQLException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
    */
    static User getUser(String sql) throws SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	try {
	    conn = JdbcUtils.getConnection();
	    ps=conn.prepareStatement(sql);
	    rs=ps.executeQuery();
	    
	    //��������װ��һ���ַ���������
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int count = rsmd.getColumnCount();
	    String[] colNames = new String[count];
	    for (int i = 0; i < count; i++) {
		colNames[i]=rsmd.getColumnLabel(i+1);
	    }
	    
	    //�����ݿ��е�ÿһ���ֶε�ֵ����һ��user ���󣬲���øó�ʼ���Ķ���
	    User user = null;
	    if(rs.next()) {
		user = new User();
		Method[] ms = user.getClass().getMethods(); //��user.getClass().getDeclaredMethods()������ǰ��ָ�������������߻�ȡ�������з���
		for (int i = 0; i < colNames.length; i++) {
		    String colName = colNames[i];
		    String setMethod ="set"+colName;
		    
		    //����user ��������з��������������趨�ķ�����ͬʱ�������ж�Ӧ��ֵ���ݸ��÷������Ӷ����ö����ʼ��
		    for (Method m : ms) {
			if(setMethod.equals(m.getName())) {
			    m.invoke(user, rs.getObject(colName));
			}
		    }
		}
	    }
	    return user;
	} 
	finally {
	    JdbcUtils.free(rs, ps, conn);
	}
    }
}
