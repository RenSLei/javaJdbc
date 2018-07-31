package priv.rsl.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** 
* @ClassName: ScrollTest 
* @Description: TODO  
* 1������ɹ����Ľ������
* 	�����ResultSet�ﲻ���������¹����������������Ĺ�����ʽ
* 	�ڴ���createStatement()��ʱ�򣬿���ָ���ض��Ĺ�����ʽ���Բ�������ʽָ��
* 2����ҳ��
* 	�����з�ҳ���ܵ����ݿ���˵����ѡ�����ݿ��Դ��ķ�ҳ���ܣ�����Ч�ʺܸߡ�
* 
* 	������û�иù��ܵ���˵��ֻ��ʹ�ýӿ��еķ��������ֶ���ҳ���������£���ҳ
* 	�����Ǵ�306����¼������ȡ10����¼����������mysql���ԣ�ֻ��Ҫ��sql�������������limit 306,10�Ϳ�����
* 	
* @author rsl
* @date 2018��3��24�� 
*  
*/
public class ScrollTest {

    public static void main(String[] args) throws SQLException {
	scroll();
    }

    static void scroll() throws SQLException {
	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;

	try {
	    // ��������
	    conn = JdbcUtils.getConnection();
	    
	    //ָ�����Թ���
	    st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
	    
	    rs = st.executeQuery("select id,name,password,birthday from user");

	    // ������
	    while (rs.next()) {
		    System.out.println(rs.getInt("id")+ " \t" +
			    rs.getString("name")+ " \t" +rs.getString("password")+ " \t" +
			    rs.getDate("birthday")+ " \t");
	    }
	    System.out.println("------------------------");
	    
	    //��λ���ڼ��У�������ע���Ƕ�λ��rs��ѯ����еĵ�4��
	    rs.absolute(4);
	    //���Խ�����ع����ǿ������ع���
	    if(rs.previous())
		   System.out.println(rs.getInt("id")+ " \t" +
			    rs.getString("name")+ " \t" +rs.getString("password")+ " \t" +
			    rs.getDate("birthday")+ " \t");
	    
	    System.out.println("------------------------");
	    
	    //��ҳ��
	    //��λ����306����¼��
	    rs.absolute(306);
	    int i=0;
	    while(rs.next()&&i<10) {
		i++;
		System.out.println(rs.getInt("id") + " \t" + rs.getString("name") + " \t" + rs.getString("password")
			+ " \t" + rs.getDate("birthday") + " \t");

	    }

	} finally {
	    JdbcUtils.free(rs, st, conn);
	}

    }
}
