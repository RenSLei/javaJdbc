package priv.rsl.jdbc.datasource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/** 
* @ClassName: MyConnectionHandler 
* @Description: TODO  
* 一个可以包装Connection的类，即通过接口InvocationHandler来包装
* 该包装的目的是将Connection里的close方法拦截下来，变成我们自己的处理方式，而其他的方法交给实际的Connection去实现
* close方法需要用到数据源，所以可以将数据源作为构造函数的参数
* 
* 过程：
* 1、创建一个私有化的被包装真实Connection对象
* 2、创建一个私有化的包装Connection对象
* 3、创建数据源对象
* 4、构造函数，将数据源对象作为参数
* 5、创建一个bind方法用于包装真实Connection
* 6、重载invoke方法
* 
* 注：重在理解该接口的一些方法以及原理
* 
* @author rsl
* @date 2018年3月31日 
*  
*/

class MyConnectionHandler implements InvocationHandler {
    private Connection realConnection;
    private Connection warpedConnection;
    private MyDataSource2 dataSource;
    
    //限制连接使用的次数
    private int maxCount = 3;
    private int currentCount = 0;
    
    MyConnectionHandler(MyDataSource2 dataSource){
	this.dataSource = dataSource;
    }
    Connection bind(Connection realConn) {
	this.realConnection = realConn;
	this.warpedConnection = (Connection)Proxy.newProxyInstance(this.getClass().getClassLoader(),
		new Class[]{ Connection.class}, this);
	
	 return warpedConnection;
    }
    
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	if("close".equals(method.getName())) {
	    
	    // 关闭一次，就让当前的次数加1
	    this.currentCount++;
	    
	    // 如果没有达到上限，就将该连接添加到连接池里，否则关掉该连接
	    if (this.currentCount < this.maxCount)
		this.dataSource.connectionsPool.addLast(this.warpedConnection);
	    else {
		this.realConnection.close();
		this.dataSource.currentCount--;
	    }
	}
	
	return method.invoke(this.realConnection, args);
    }

}
