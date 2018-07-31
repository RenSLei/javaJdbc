package priv.rsl.jdbc;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;//导入的是驱动程序（我们import的在classpath下的jar包）中的包
import com.mysql.jdbc.Statement;//导入的是驱动程序（我们import的在classpath下的jar包）中的包
/** 
* @ClassName: JdbcBaseInstance 
* @Description: TODO  
* 连接数据的一般步骤：
* 一、注册驱动（只做一次）
* 驱动就是实现了JDBC接口的类。
* 注册驱动就是告诉DriverManager要与哪一个驱动连接,方法有三种：
* 
*1、DriverManager.registerDriver(new com.mysql.jdbc.Driver());
* 		解释：类com.mysql.jdbc.Driver是驱动程序里的一个类，源代码如下：
* 		
* 		public class Driver extends NonRegisteringDriver implements java.sql.Driver {
*    		//Register ourselves with the DriverManager
*    		static {
*        		try {
*            			java.sql.DriverManager.registerDriver(new Driver());
*        		} catch (SQLException E) {
*            	throw new RuntimeException("Can't register driver!");
*        	}
*    			}
* 		* Construct a new driver and register it with DriverManager
* 		* 
*		* @throws SQLException
*		*             if a database error occurs.
*	阅读驱动程序Driver 类的源代码可以知道：
*	1)、此类实现了java.sql.Driver接口(这正是驱动的定义所在)；
*	2)、该类有一个静态代码块，即类一被加载就会执行DriverManager.registerDriver(new Driver())即构建一个本类对象；
*
*结论：这种方法会造成DriverManager中产生两个一样的驱动，并会对具体的驱动类产生依赖。 
具体来说就是： 
a.加载的时候注册一次驱动，实例化的时候又注册一次。所以两次。 
b.由于实例化了com.mysql.jdbc.Driver.class，导致必须导入该类(就是要把这个类import进去)，从而具体驱动产生了依赖。不方便扩展代码。 

*2、System.setProperty("jdbc.drivers","com.mysql.jdbc.Driver"); 
*通过系统的属性设置注册驱动
*如果要注册多个驱动，则System.setProperty("jdbc.drivers","com.mysql.jdbc.Driver:com.oracle.jdbc.Driver"); 
*虽然不会对具体的驱动类产生依赖；但注册不太方便，所以很少使用。 
*
*3、Class.forName("com.mysql.jdbc.Driver");
*推荐这种方式，不会对具体的驱动类产生依赖（就是不用import package了）。 
*其实这个只是把com.mysql.jdbc.Driver.class这个类装载进去，但是关键就在于静态块,就是因为这个代码块，让类在加载的时候就把驱动注册进去了！
*
* 二、建立连接（Connection）
* 相当于是搭桥，建立连接
* 
* 三、创建执行SQL的语句（Statement）
* 相当于汽车
* 
* 四、执行语句
* 将要执行的语句放在Statement中进行执行
* 
* 五、处理执行的结果（ResultSet）
* 按照行来遍历，
* 
* 六、释放资源
* 按照先创建后关闭的原则进行资源的关闭,数据库连接数越多，数据库系统越卡
*
* @author rsl
* @date 2018年3月8日 下午5:05:54 
*  
*/
public class ConnectorTemplate {

    public static void main(String[] args) throws Exception {
	//test();
	template();
    }
    
    
    
    /** 
    * @Title: template 
    * @Description: TODO  
    * 改进之处是：
    * 先声明、处理异常try-finally、关闭资源try-finally
    * 
    * 但是，注册驱动只需要执行一次就行了，而且不用每次都去写大量复杂的关闭资源的代码
    * 所以注册驱动和关闭资源就可以单独封装一个类,当做工具类来使用:详见JdbcUtils类
    * @param @throws Exception
    * @return void
    * @throws 
    */
    static void template() throws Exception {
	//将声明放在工具类中，要修改只需要修改那一个类的信息就可以了
	java.sql.Connection conn = null;
	java.sql.Statement st = null;
	ResultSet rs = null;
	
	//处理异常
	try {
	    
	    /*注册驱动已经在工具类中实现了
	    Class.forName("com.mysql.jdbc.Driver");
	    */
	    
	    //建立连接
	    conn = JdbcUtils.getConnection();
	    //conn = JdbcUtilsSing.getInstance().getConnection();

	    //创建语句
	    st = conn.createStatement();

	    //执行语句
	    rs = st.executeQuery("select * from user");

	    //处理结果
	    while (rs.next()) {
		for (int i = 1; i <= 8; i++) {
		    System.out.print(rs.getObject(i) + " \t");
		}
		System.out.println("\n");
	    }

	} finally {
	    JdbcUtils.free(rs, st, conn);
	}
    }
    
    /** 
    * @Title: test 
    * @Description: TODO 
    * 一种简单的、基本的连接数据的过程，不严谨，也不是特别优化的代码 
    * @throws SQLException
    * @throws ClassNotFoundException
    * @return void
    *
    */
    static void test() throws SQLException, ClassNotFoundException {
	//1.注册驱动
	//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
	//System.setProperty("jdbc.drivers", "com.mysql.jdbc.Driver");
	Class.forName("com.mysql.jdbc.Driver");//推荐方式
	
	
	//2.建立连接
	//不同的数据库有不同形式的url形式，且不需要去背它，只需要查看文档就可以了 
	String url = "jdbc:mysql://localhost:3306/mydb1";
	String user = "root";
	String password = "root";
	Connection conn = (Connection) DriverManager.getConnection(url,user,password);
	
	//3.创建语句
	java.sql.Statement st = (Statement) conn.createStatement();
	
	//4.执行语句
	ResultSet rs =st.executeQuery("select * from user");
	
	//5.处理结果
	while(rs.next()) {
	    for (int i = 1; i <9; i++) {
		System.out.print(rs.getObject(i)+"   \t  ");
	    }
	    System.out.println("\n");
	}
	rs.close();
	st.close();
	conn.close();
    }

}



















