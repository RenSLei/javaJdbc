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
    * 操作字节流，将一张图片插入到数据库中
    * 注意：mysql数据库中的BLOB类型的是有默认的大小的，所以在进行二进制的数据的存储的时候，
    * 要注意设定的类型的大小要大于传输的二进制数据
    * 如：TinyBlob 最大 255B, Blob 最大 64K, MediumBlob 最大16M, LongBlob 最大 4G
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
	    
	    //实际传输的图片数据为374KB 即 374字节
	    File file = new File("src/priv/rsl/jdbc1/建立连接.jpg");
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
    * 将数据库中该列的二进制数据读出来并且复制成另一个图片
    * @throws SQLException
    * @throws IOException
    */
    static void read() throws SQLException, IOException {

	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;

	try {
	    // 创建连接
	    conn = JdbcUtils.getConnection();

	    // 创建语句
	    String sql = "select * from blob_test";
	    st = conn.createStatement();

	    // 执行语句

	    rs = st.executeQuery(sql);
	    // 处理结果
	    while (rs.next()) {
		
		//建立目标文件对象。
		BufferedOutputStream bufo = new BufferedOutputStream(new FileOutputStream("读取二进制数据.jpg"));
		
		//2代表查询的数据中的第2列，因为是全部查询，所以图片那一个数据是是当前这一行的第2列
		BufferedInputStream bufI = new BufferedInputStream(rs.getBinaryStream(2));

		// 读取文件并写入文件，一次读取1024个字节
		byte[] b = new byte[1024];
		for( int i=0;(i= bufI.read(b,0,b.length))>0;) {
		    // 写入一行，并换行，且刷新到文件中去
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
