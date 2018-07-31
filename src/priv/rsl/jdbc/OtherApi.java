package priv.rsl.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

/** 
* @ClassName: OtherApi 
* @Description: TODO  
* 
* ����jdbc�ĽӿڵĹ��ܣ�ȡ����ǰ�����¼������idֵ��������ǰ������
* Ҳ����ͨ�������ݿ�mysql��д�洢���̣�Ȼ����ã������Ƚ��鷳����sql���Ҫ��ϸߣ�jdbc�ṩ��һ��api���Խ����׵�ʵ�֣�
* ͨ��һ��������¼��������˵��������̣�
* 	ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
* 	˵��������mysql��˵���Բ��üӺ���Ĳ����Ϳ����õ����������Ƕ������������ݿ���������Ͳ�һ���ˣ����Ի��Ǽ�����������Ƚ��б�֤
* 
* 	�Լ�int id=0;
	    rs=ps.getGeneratedKeys();
	    while(rs.next()) {
		id=rs.getInt(1);
	    }
	    �������ó���
	    
	���ԣ�ԭʼ������ݣ�
        +----+----------+----------+------------+-------+
        | id | name     | password | birthday   | money |
        +----+----------+----------+------------+-------+
        |  1 | rsl      | 111      | 1993-03-21 |   300 |
        |  2 | wangwu   | 222      | 1994-04-05 |   250 |
        |  3 | zhangsan | 333      | 1995-03-21 |   200 |
        |  4 | update   | 444      | 1996-03-21 |   100 |
        +----+----------+----------+------------+-------+
        
       ִ�к�
       	���أ�id=5
       	������ݣ�
        +----+----------+--------------+------------+-------+
        | id | name     | password     | birthday   | money |
        +----+----------+--------------+------------+-------+
        |  1 | rsl      | 111          | 1993-03-21 |   300 |
        |  2 | wangwu   | 222          | 1994-04-05 |   250 |
        |  3 | zhangsan | 333          | 1995-03-21 |   200 |
        |  4 | update   | 444          | 1996-03-21 |   100 |
        |  5 | get_key  | key_password | 2018-03-23 |   500 |
        +----+----------+--------------+------------+-------+
        
        ������ʵ��UserDao��ʱ�򣬿��Խ�addUser()������ȡ����id�����������User�������ͱ�֤��ÿһ��������java�ж�����id��
* 
* 
* @author rsl
* @date 2018��3��23�� 
*  
*/
public class OtherApi {

    public static void main(String[] args) throws SQLException {
	System.out.println("id="+create());
    }

    static int create() throws SQLException {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    // ��������
	    conn = JdbcUtils.getConnection();

	    String sql = "insert into user (name,password,birthday,money) values (?,?,?,?)";
	    
	    //����mysql��˵���Բ��üӺ���Ĳ����Ϳ����õ����������Ƕ������������ݿ���������Ͳ�һ���ˣ����Ի��Ǽ�����������Ƚ��б�֤
	    ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
	    
	    //����ֵ
	    ps.setString(1,"get_key");
	    ps.setString(2,"key_password" );
	    ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
	    ps.setFloat(4, 500.0f);

	    ps.executeUpdate();

	    //�������ó���
	    int id=0;
	    rs=ps.getGeneratedKeys();
	    while(rs.next()) {
		id=rs.getInt(1);
	    }
	    return id;

	} finally {
	    JdbcUtils.free(rs, ps, conn);
	}

    }    
}
