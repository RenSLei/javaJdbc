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
     * 在指定的数据库中读取指定表的全部内容，该内容的类型是TEXT类型的，并将此内容放在该工程的根目录下
     * 涉及到操作的文件的io的知识： 将读取到的结果放在ResultSet中
     * 
     * 将输出流与指定的文件关联，并装饰一下 再获取到该行该列的值的输入流，将该读取流装饰一下，一次读一行文本，
     * 说明输出流和输入流：
     * 此处只是为了演示数据库jdbc的知识，其实还有很多不完善的地方，如应该将目标文件先封装成File对象再传递、
     * 这样在每一次循环之前可以先判断或者可以更改文件名，以至于不被覆盖，假如有很多条记录，
     * 而每条记录的第二列都希望存在一个文件中，就会导致文件覆盖，最后只有一个文件
     * 读一次就将结果写入到目标问价中，直到返回的是null为止。 记得刷新和关闭资源
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
	    // 创建连接
	    conn = JdbcUtils.getConnection();

	    // 创建语句
	    String sql = "select * from clob_text";
	    st = conn.createStatement();

	    // 执行语句

	    rs = st.executeQuery(sql);
	    // 处理结果
	    while (rs.next()) {

		BufferedWriter bufw = new BufferedWriter(new FileWriter("SQLinJect_copy.java"));
		BufferedReader bufr = new BufferedReader(rs.getCharacterStream(2));

		// 读取文件并写入文件
		String line = null;

		while ((line = bufr.readLine()) != null) {
		    // 写入一行，并换行，且刷新到文件中去
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
     * 不通过io读取文件的输入流，直接获取该结果的字符串形式:getString()，将其写入进文件即可
     * 因为java的String类型是没有限制其大小的，只要不超过内存的大小就可以，不像sql的varchar是有大小限制的。
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
	    // 创建连接
	    conn = JdbcUtils.getConnection();

	    // 创建语句
	    String sql = "select * from clob_text";
	    st = conn.createStatement();

	    // 执行语句

	    rs = st.executeQuery(sql);
	    // 处理结果
	    while (rs.next()) {
		BufferedWriter bufw = new BufferedWriter(new FileWriter("SQLinJect_copy.java"));
		String s = rs.getString(2);
		// 读取文件并写入文件
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
    * 直接插入一段文本到数据库中。使用preparedStatement 
    * 为什么要使用PreparedStatement?
    * 因为要在sql中传递参数
    * 在一个表中给类型为TEXT的列插入一个记录，TEXT类型是大文本数据类型(clob类型)
    * 插入的来源是一个文本文件，所以在ps.set的时候，将关联该文本文件的读取流传递到该方法中
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
	    // 创建连接
	    conn = JdbcUtils.getConnection();
	    
	    File file = new File("src/priv/rsl/jdbc2/SQLinJect.java");
	    BufferedReader bufr = new BufferedReader(new FileReader(file));
	    // 创建语句
	    String sql = "insert into clob_text(big_text) values (?)";
	    ps = conn.prepareStatement(sql);
	    
	    ps.setCharacterStream(1, bufr, file.length());
	    
	    
	    //方法二，直接setString(1,string)即可，问题就在于如何将要写入的文本转换为一个字符串
	/*  StringBuilder sb = new StringBuilder();
	    String line = null;
	    while((line =bufr.readLine())!=null) {
		sb.append(line);
		sb.append("\r\n");
	    }
	    String s = new String(sb);
	    ps.setString(1, s);*/
	   
	    /*
	     * 方法三
	    char[] buf = new char[1024 * 1024];
	    int n = 0;
	    n = bufr.read(buf);
	    System.out.println(n);
	    String s = new String(buf, 0, n);
	    ps.setString(1, s);*/
	    
	    bufr.close();
	    // 执行语句
	    int i = ps.executeUpdate();

	    // 处理结果
	    System.out.println("i="+i);

	} finally {
	    JdbcUtils.free(rs, ps, conn);
	}

    }

}
