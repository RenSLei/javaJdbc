package priv.rsl.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ORMTest {

    public static void main(String[] args) throws IllegalAccessException, 
    IllegalArgumentException, InvocationTargetException, SQLException, InstantiationException {
	ORMBean bean = (ORMBean) getObject("select id as Id, name as Name, "
		+ "password as Password, birthday as Birthday, "
		+ "money as Money from user where id = 1",ORMBean.class);
	System.out.println(bean);
    }

    /** 
    * @Title: getUser 
    * @Description: TODO  
    * 
    * ORM:Object Relation Mapping ����������������ݿ�֮��ӳ���Ԫ���ݣ�
    * �Զ���͸���ذ�JavaӦ�ó����еĶ���־û�����ϵ���ݿ��еı�
    * 
    * ӳ��������ǣ��ٶ����ݿ��е��ֶζ�Ӧ�����еĸ������ԣ�����set��get������
    * ����һ���ղ����Ĺ��캯����Ŀ���ǿ���ֱ��ͨ��Class������newInstance������Ҫ��ͨ������õ����캯�������죩
    * ���̣�
    * 1�����ӡ�ʹ�ñ�����ѯ��Ϊ�˺Ͷ���ķ�����Ӧ��������ѯ����ȡ�����rs
    * 2�������ݿ��е�ÿһ���ֶε����ַŵ�һ���ַ��������У�
    * 3��ÿһ����¼����һ�����󣬱���ÿһ���ֶΣ�����set+�����ķ�ʽ�Ͷ���ķ�������һһ�ȶԣ�
    * 	һ��ƥ��ɹ����͵��ø÷������Ӷ��������ʼ��
    * 
    * @param sql sql���
    * @return
    * @throws SQLException 
    * @throws InvocationTargetException 
    * @throws IllegalArgumentException 
    * @throws InstantiationException 
    */
    static<T> T getObject(String sql,Class<T> clazz) 
	    throws SQLException, IllegalAccessException, 
	    IllegalArgumentException, InvocationTargetException, 
	    InstantiationException {
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
	    T  object = null;
	    if(rs.next()) {
		
		object = clazz.newInstance();
		
		//��user.getClass().getDeclaredMethods()������ǰ��ָ�������������߻�ȡ�������з���
		Method[] ms = clazz.getMethods(); 
		for (int i = 0; i < colNames.length; i++) {
		    String colName = colNames[i];
		    String setMethod ="set"+colName;
		    
		    //����user ��������з��������������趨�ķ�����ͬʱ�������ж�Ӧ��ֵ���ݸ��÷������Ӷ����ö����ʼ��
		    for (Method m : ms) {
			if(setMethod.equals(m.getName())) {
			    m.invoke(object, rs.getObject(colName));
			}
		    }
		}
	    }
	    return object;
	} 
	finally {
	    JdbcUtils.free(rs, ps, conn);
	}
    }
}
