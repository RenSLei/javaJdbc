package priv.rsl.jdbc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BlobTest {

    public static void main(String[] args) throws SQLException, IOException {
	//create();
	read();
	
    }
    
    /** 
    * @Title: create 
    *
    * @Description: TODO  
    * �����ֽ�������һ��ͼƬ���뵽���ݿ���
    * ע�⣺mysql���ݿ��е�BLOB���͵�����Ĭ�ϵĴ�С�ģ������ڽ��ж����Ƶ����ݵĴ洢��ʱ��
    * Ҫע���趨�����͵Ĵ�СҪ���ڴ���Ķ���������
    * �磺TinyBlob ��� 255B, Blob ��� 64K, MediumBlob ���16M, LongBlob ��� 4G
    * @throws SQLException
    * @throws IOException
    *
    *
    */
    static void create() throws SQLException, IOException {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    conn = JdbcUtils.getConnection();
	    
	    //ʵ�ʴ����ͼƬ����Ϊ374KB �� 374�ֽ�
	    File file = new File("src/priv/rsl/jdbc1/��������.jpg");
	    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

	    String sql = "insert into blob_test(big_bit) values(?)";
	    ps = conn.prepareStatement(sql);
	    
	    
	    ps.setBinaryStream(1, bis, file.length());
	    int i = ps.executeUpdate();
	    bis.close();
	    System.out.println("i=" + i);
	} finally {
	    JdbcUtils.free(rs, ps, conn);
	}
    }
    
    /** 
    * @Title: read 
    * @Description: TODO  
    * �����ݿ��и��еĶ��������ݶ��������Ҹ��Ƴ���һ��ͼƬ
    * @throws SQLException
    * @throws IOException
    */
    static void read() throws SQLException, IOException {

	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;

	try {
	    // ��������
	    conn = JdbcUtils.getConnection();

	    // �������
	    String sql = "select * from blob_test";
	    st = conn.createStatement();

	    // ִ�����

	    rs = st.executeQuery(sql);
	    // ������
	    while (rs.next()) {
		
		//����Ŀ���ļ�����
		BufferedOutputStream bufo = new BufferedOutputStream(new FileOutputStream("��ȡ����������.jpg"));
		
		//2�����ѯ�������еĵ�2�У���Ϊ��ȫ����ѯ������ͼƬ��һ���������ǵ�ǰ��һ�еĵ�2��
		BufferedInputStream bufI = new BufferedInputStream(rs.getBinaryStream(2));

		// ��ȡ�ļ���д���ļ���һ�ζ�ȡ1024���ֽ�
		byte[] b = new byte[1024];
		for( int i=0;(i= bufI.read(b,0,b.length))>0;) {
		    // д��һ�У������У���ˢ�µ��ļ���ȥ
		    bufo.write(b, 0, i);
		    bufo.flush();
		}
		bufo.close();
		bufI.close();
	    }

	} finally {
	    JdbcUtils.free(rs, st, conn);
	}

    }

}
