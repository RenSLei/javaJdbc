package priv.rsl.jdbc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClobText {

    public static void main(String[] args) throws SQLException, IOException {
	create();

	//readToFile2();
    }
    
    /**
     * @Title: read
     *
     * @Description: TODO 
     * ��ָ�������ݿ��ж�ȡָ�����ȫ�����ݣ������ݵ�������TEXT���͵ģ����������ݷ��ڸù��̵ĸ�Ŀ¼��
     * �漰���������ļ���io��֪ʶ�� ����ȡ���Ľ������ResultSet��
     * 
     * ���������ָ�����ļ���������װ��һ�� �ٻ�ȡ�����и��е�ֵ�������������ö�ȡ��װ��һ�£�һ�ζ�һ���ı���
     * ˵�����������������
     * �˴�ֻ��Ϊ����ʾ���ݿ�jdbc��֪ʶ����ʵ���кܶ಻���Ƶĵط�����Ӧ�ý�Ŀ���ļ��ȷ�װ��File�����ٴ��ݡ�
     * ������ÿһ��ѭ��֮ǰ�������жϻ��߿��Ը����ļ����������ڲ������ǣ������кܶ�����¼��
     * ��ÿ����¼�ĵڶ��ж�ϣ������һ���ļ��У��ͻᵼ���ļ����ǣ����ֻ��һ���ļ�
     * ��һ�ξͽ����д�뵽Ŀ���ʼ��У�ֱ�����ص���nullΪֹ�� �ǵ�ˢ�º͹ر���Դ
     *
     * @throws SQLException
     * @throws IOException
     *
     */
    static void readToFile() throws SQLException, IOException {
	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;

	try {
	    // ��������
	    conn = JdbcUtils.getConnection();

	    // �������
	    String sql = "select * from clob_text";
	    st = conn.createStatement();

	    // ִ�����

	    rs = st.executeQuery(sql);
	    // ������
	    while (rs.next()) {

		BufferedWriter bufw = new BufferedWriter(new FileWriter("SQLinJect_copy.java"));
		BufferedReader bufr = new BufferedReader(rs.getCharacterStream(2));

		// ��ȡ�ļ���д���ļ�
		String line = null;

		while ((line = bufr.readLine()) != null) {
		    // д��һ�У������У���ˢ�µ��ļ���ȥ
		    bufw.write(line);
		    bufw.newLine();
		    bufw.flush();
		}
		bufw.close();
		bufr.close();
	    }

	} finally {
	    JdbcUtils.free(rs, st, conn);
	}

    }

    /**
     * @Title: readToFile2
     *
     * @Description: TODO 
     * ��ͨ��io��ȡ�ļ�����������ֱ�ӻ�ȡ�ý�����ַ�����ʽ:getString()������д����ļ�����
     * ��Ϊjava��String������û���������С�ģ�ֻҪ�������ڴ�Ĵ�С�Ϳ��ԣ�����sql��varchar���д�С���Ƶġ�
     *
     * @throws SQLException
     * @throws IOException
     *
     *
     */
    static void readToFile2() throws SQLException, IOException {
	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;

	try {
	    // ��������
	    conn = JdbcUtils.getConnection();

	    // �������
	    String sql = "select * from clob_text";
	    st = conn.createStatement();

	    // ִ�����

	    rs = st.executeQuery(sql);
	    // ������
	    while (rs.next()) {
		BufferedWriter bufw = new BufferedWriter(new FileWriter("SQLinJect_copy.java"));
		String s = rs.getString(2);
		// ��ȡ�ļ���д���ļ�
		bufw.write(s);
		bufw.flush();
		bufw.close();
	    }
	} finally {
	    JdbcUtils.free(rs, st, conn);
	}

    }

    /** 
    * @Title: create 
    * @Description: TODO 
    * ֱ�Ӳ���һ���ı������ݿ��С�ʹ��preparedStatement 
    * ΪʲôҪʹ��PreparedStatement?
    * ��ΪҪ��sql�д��ݲ���
    * ��һ�����и�����ΪTEXT���в���һ����¼��TEXT�����Ǵ��ı���������(clob����)
    * �������Դ��һ���ı��ļ���������ps.set��ʱ�򣬽��������ı��ļ��Ķ�ȡ�����ݵ��÷�����
    * 
    * @throws SQLException
    * @throws IOException
    * @return void
    * @throws 
    */
    static void create() throws SQLException, IOException {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    // ��������
	    conn = JdbcUtils.getConnection();
	    
	    File file = new File("src/priv/rsl/jdbc2/SQLinJect.java");
	    BufferedReader bufr = new BufferedReader(new FileReader(file));
	    // �������
	    String sql = "insert into clob_text(big_text) values (?)";
	    ps = conn.prepareStatement(sql);
	    
	    ps.setCharacterStream(1, bufr, file.length());
	    
	    
	    //��������ֱ��setString(1,string)���ɣ������������ν�Ҫд����ı�ת��Ϊһ���ַ���
	/*  StringBuilder sb = new StringBuilder();
	    String line = null;
	    while((line =bufr.readLine())!=null) {
		sb.append(line);
		sb.append("\r\n");
	    }
	    String s = new String(sb);
	    ps.setString(1, s);*/
	   
	    /*
	     * ������
	    char[] buf = new char[1024 * 1024];
	    int n = 0;
	    n = bufr.read(buf);
	    System.out.println(n);
	    String s = new String(buf, 0, n);
	    ps.setString(1, s);*/
	    
	    bufr.close();
	    // ִ�����
	    int i = ps.executeUpdate();

	    // ������
	    System.out.println("i="+i);

	} finally {
	    JdbcUtils.free(rs, ps, conn);
	}

    }

}
