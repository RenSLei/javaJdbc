package priv.rsl.jdbc;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParameterMetaTest {

    public static void main(String[] args) throws SQLException {
	
	String sql="select id,name,password,birthday from user where id < ? and name=? and money>? ";
	
	Object[] params =new Object[] {6,"lisi",100};
	read(sql,params );
    }

    /** 
    * @Title: read 
    *
    * @Description: TODO  
    * ͨ������SQL���Ͳ�����ִ��SQL���,sql����Ǵ��в��������
    * �Բ�ѯΪ����ʹ��preparstatement
    * 
    * ��Щ���ݿ�֧�ֿ��Ի�ȡ�Ĳ�������Ϣ����Щ�򲻿��ԣ���Ϊ��Ҫ�����ݿ��ѯռλ����֪�����ɱ��Ƚϴ�
    *
    * @param sql
    * @param params
    * @throws SQLException
    *
    *
    */
    static void read(String sql,Object[] params) throws SQLException {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    conn = JdbcUtils.getConnection();
	    
	    ps = conn.prepareStatement(sql);
	    ParameterMetaData pmd = ps.getParameterMetaData();
	    
	    int count = pmd.getParameterCount();//���count��sql������?�ĸ���
	    for (int i = 1; i <=count; i++) {
//		System.out.println(pmd.getParameterType(i));
		ps.setObject(i, params[i-1]);
	    }
	    rs = ps.executeQuery();
	    
	    // ������
	    while (rs.next()) {
		System.out.println(rs.getInt("id") + " \t" + 
			rs.getString("name") + " \t" + rs.getString("password")+
			" \t" + rs.getDate("birthday") + " \t");
		}

	} finally {
	    JdbcUtils.free(rs, ps, conn);
	}

    }
}
