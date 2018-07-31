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
    * 通过传递SQL语句和参数来执行SQL语句,sql语句是带有参数的语句
    * 以查询为例，使用preparstatement
    * 
    * 有些数据库支持可以获取的参数的信息，有些则不可以，因为需要到数据库查询占位符才知道，成本比较大
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
	    
	    int count = pmd.getParameterCount();//这个count是sql语句里的?的个数
	    for (int i = 1; i <=count; i++) {
//		System.out.println(pmd.getParameterType(i));
		ps.setObject(i, params[i-1]);
	    }
	    rs = ps.executeQuery();
	    
	    // 处理结果
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
