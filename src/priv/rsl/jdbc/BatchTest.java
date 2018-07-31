package priv.rsl.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

/**
 * @ClassName: BatchTest
 * @Description: TODO
 * 测试jdbc的批处理功能
 * 以插入500条数据库记录做测试
 *
 * 区别：单个插入记录效率低的原因可能是每插入一条记录都要建立一次连接，然后释放
 * 而打包插入，将数据打包后一次执行，
 *
 * 优势：批处理在运行时间上，比不使用批处理功能缩小了4倍
 *
 * @author rsl
 * @date 2018年3月23日
 *
 */
public class BatchTest {

public static void main(String[] args) throws SQLException {

        /*//演示方法一：
           long st = System.currentTimeMillis();
           for (int i = 0; i < 500; i++) {
            create(i);
           }
           long en = System.currentTimeMillis();
           System.out.println("create:"+(en-st));
         */

        //演示方法二：打包
        long st = System.currentTimeMillis();
        BatchCreate();
        long en = System.currentTimeMillis();
        System.out.println("BatchCreate:"+(en-st));
}

/**
 * @Title: create
 * @Description: TODO
 * 在user 表中插入记录
 * @param i 添加到插入记录的数据上，以便于观察数据
 * @throws SQLException
 */
static void create(int i) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
                // 创建连接
                conn = JdbcUtils.getConnection();

                String sql = "insert into user (name,password,birthday,money) values (?,?,?,?)";

                //对于mysql来说可以不用加后面的参数就可以拿到主键，但是对于其他的数据库或者驱动就不一定了，所以还是加上这个参数比较有保证
                ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

                //设置值
                ps.setString(1,"create"+i);
                ps.setString(2,"***" );
                ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                ps.setFloat(4, 500.0f+i);

                ps.executeUpdate();

        } finally {
                JdbcUtils.free(rs, ps, conn);
        }

}

/**
 * @Title: BatchCreate
 * @Description: TODO
 * 批处理插入数据500条
 *
 * @throws SQLException
 */
static void BatchCreate() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
                // 创建连接
                conn = JdbcUtils.getConnection();

                String sql = "insert into user (name,password,birthday,money) values (?,?,?,?)";

                //对于mysql来说可以不用加后面的参数就可以拿到主键，但是对于其他的数据库或者驱动就不一定了，所以还是加上这个参数比较有保证
                ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

                for (int i = 1; i <= 500; i++) {
                        ps.setString(1,"create"+i);
                        ps.setString(2,"*****" );
                        ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                        ps.setFloat(4, 500.0f+i);

                        //打包
                        ps.addBatch();
                }
                ps.executeBatch();

        } finally {
                JdbcUtils.free(rs, ps, conn);
        }

}
}
