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
    * 结果集里面的元数据的一些信息，通过ResultSetMetaData rsmd = rs.getMetaData();
    * 将列名作为键，列对应的值作为值存入map集合中返回
    * 将一行的数据放在map中，将结果集放在list中，即将map放在list中
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
	    
	    int count = rsmd.getColumnCount();//获取rs结果中的列的个数
	    String[] colName = new String[count];
	    //将列的名字存到一个字符串数组中
	    for (int i = 0; i <count; i++) {
//		System.out.print(rsmd.getColumnName(i)+"\t");
//		System.out.println(rsmd.getColumnClassName(i)+"\t");
		colName[i]=rsmd.getColumnName(i+1);
	    }
	    // 处理结果
	    Map<String,Object> data = null;
	    List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
	    
	    while (rs.next()) {
		data =new HashMap<String, Object>();
		//每一行进行一次循环遍历
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
