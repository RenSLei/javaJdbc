package priv.rsl.jdbc.datasource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/** 
* @ClassName: MyConnectionHandler 
* @Description: TODO  
* һ�����԰�װConnection���࣬��ͨ���ӿ�InvocationHandler����װ
* �ð�װ��Ŀ���ǽ�Connection���close����������������������Լ��Ĵ���ʽ���������ķ�������ʵ�ʵ�Connectionȥʵ��
* close������Ҫ�õ�����Դ�����Կ��Խ�����Դ��Ϊ���캯���Ĳ���
* 
* ���̣�
* 1������һ��˽�л��ı���װ��ʵConnection����
* 2������һ��˽�л��İ�װConnection����
* 3����������Դ����
* 4�����캯����������Դ������Ϊ����
* 5������һ��bind�������ڰ�װ��ʵConnection
* 6������invoke����
* 
* ע���������ýӿڵ�һЩ�����Լ�ԭ��
* 
* @author rsl
* @date 2018��3��31�� 
*  
*/

class MyConnectionHandler implements InvocationHandler {
    private Connection realConnection;
    private Connection warpedConnection;
    private MyDataSource2 dataSource;
    
    //��������ʹ�õĴ���
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
	    
	    // �ر�һ�Σ����õ�ǰ�Ĵ�����1
	    this.currentCount++;
	    
	    // ���û�дﵽ���ޣ��ͽ���������ӵ����ӳ������ص�������
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
