package priv.rsl.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultSetMetaDataTest {

    public static void main(String[] args) throws SQLException {
	List<Map<String,Object>> datas= read("select id,name,birthday from user where id < 7");
	System.out.println(datas);
    }

    /** 
    * @Title: read 
    * @Description: TODO  
    * ����������Ԫ���ݵ�һЩ��Ϣ��ͨ��ResultSetMetaData rsmd = rs.getMetaData();
    * ��������Ϊ�����ж�Ӧ��ֵ��Ϊֵ����map�����з���
    * ��һ�е����ݷ���map�У������������list�У�����map����list��
    *
    * @param sql
    * @return
    * @throws SQLException
    *
    */
    static List<Map<String,Object>> read(String sql) throws SQLException {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    conn = JdbcUtils.getConnection();

	    ps = conn.prepareStatement(sql);
	    rs = ps.executeQuery();
	    
	    ResultSetMetaData rsmd = rs.getMetaData();
	    
	    int count = rsmd.getColumnCount();//��ȡrs����е��еĸ���
	    String[] colName = new String[count];
	    //���е����ִ浽һ���ַ���������
	    for (int i = 0; i <count; i++) {
//		System.out.print(rsmd.getColumnName(i)+"\t");
//		System.out.println(rsmd.getColumnClassName(i)+"\t");
		colName[i]=rsmd.getColumnName(i+1);
	    }
	    // ������
	    Map<String,Object> data = null;
	    List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
	    
	    while (rs.next()) {
		data =new HashMap<String, Object>();
		//ÿһ�н���һ��ѭ������
		for (int i = 0; i < colName.length; i++) {
		    data.put(colName[i], rs.getObject(colName[i]));
		}
		datas.add(data);
	    }
	    return datas;
	} finally {
	    JdbcUtils.free(rs, ps, conn);
	}
	

    }
}
