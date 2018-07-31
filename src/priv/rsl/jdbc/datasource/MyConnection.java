package priv.rsl.jdbc.datasource;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/** 
* @ClassName: MyConnection 
* @Description: TODO  
* 为了重载close方法，让连接不是真正的关闭连接，
* 但是有一个问题，就是我们要把所有的方法都要重写一遍，很麻烦
* 
* @author rsl
* @date 2018年3月29日 
*  
*/
public class MyConnection implements Connection {
    
    private Connection realConn;
    private MyDataSource2 dataSource;
    
    //限制连接使用的次数
    private int maxCount = 3;
    private int currentCount = 0;
    
    //只在本包中可以获取该对象
    MyConnection(Connection connection,MyDataSource2 dataSource) {
	this.realConn = connection;
	this.dataSource = dataSource;
    }
    

    //这个方法就是我们关心的方法：
    @Override
    public void close() throws SQLException {
	
	//关闭一次，就让当前的次数加1
	this.currentCount++;
	
	//如果没有达到上限，就将该连接添加到连接池里，否则关掉该连接
	if(this.currentCount<this.maxCount)
	    this.dataSource.connectionsPool.addLast(this);
	else {
	    this.realConn.close();
	    this.dataSource.currentCount--;
	}
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
	return this.realConn.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
	return this.realConn.isWrapperFor(iface);
    }

    @Override
    public Statement createStatement() throws SQLException {
	return this.realConn.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
	return this.realConn.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
	return this.realConn.prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
	return this.realConn.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
	this.realConn.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
	return this.realConn.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
	this.realConn.commit();
    }

    @Override
    public void rollback() throws SQLException {
	this.realConn.rollback();;
    }


    @Override
    public boolean isClosed() throws SQLException {
	return this.realConn.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public boolean isReadOnly() throws SQLException {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public String getCatalog() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void clearWarnings() throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
	    throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public int getHoldability() throws SQLException {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
	    throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
	    int resultSetHoldability) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
	    int resultSetHoldability) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Clob createClob() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Blob createBlob() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public NClob createNClob() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Properties getClientInfo() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void setSchema(String schema) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public String getSchema() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void abort(Executor executor) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
	// TODO Auto-generated method stub
	return 0;
    }

    
    
    
}

