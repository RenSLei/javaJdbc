package priv.rsl.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

/**
 * @ClassName: BatchTest
 * @Description: TODO
 * ����jdbc����������
 * �Բ���500�����ݿ��¼������
 *
 * ���𣺵��������¼Ч�ʵ͵�ԭ�������ÿ����һ����¼��Ҫ����һ�����ӣ�Ȼ���ͷ�
 * ��������룬�����ݴ����һ��ִ�У�
 *
 * ���ƣ�������������ʱ���ϣ��Ȳ�ʹ������������С��4��
 *
 * @author rsl
 * @date 2018��3��23��
 *
 */
public class BatchTest {

public static void main(String[] args) throws SQLException {

        /*//��ʾ����һ��
           long st = System.currentTimeMillis();
           for (int i = 0; i < 500; i++) {
            create(i);
           }
           long en = System.currentTimeMillis();
           System.out.println("create:"+(en-st));
         */

        //��ʾ�����������
        long st = System.currentTimeMillis();
        BatchCreate();
        long en = System.currentTimeMillis();
        System.out.println("BatchCreate:"+(en-st));
}

/**
 * @Title: create
 * @Description: TODO
 * ��user ���в����¼
 * @param i ��ӵ������¼�������ϣ��Ա��ڹ۲�����
 * @throws SQLException
 */
static void create(int i) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
                // ��������
                conn = JdbcUtils.getConnection();

                String sql = "insert into user (name,password,birthday,money) values (?,?,?,?)";

                //����mysql��˵���Բ��üӺ���Ĳ����Ϳ����õ����������Ƕ������������ݿ���������Ͳ�һ���ˣ����Ի��Ǽ�����������Ƚ��б�֤
                ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

                //����ֵ
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
 * �������������500��
 *
 * @throws SQLException
 */
static void BatchCreate() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
                // ��������
                conn = JdbcUtils.getConnection();

                String sql = "insert into user (name,password,birthday,money) values (?,?,?,?)";

                //����mysql��˵���Բ��üӺ���Ĳ����Ϳ����õ����������Ƕ������������ݿ���������Ͳ�һ���ˣ����Ի��Ǽ�����������Ƚ��б�֤
                ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

                for (int i = 1; i <= 500; i++) {
                        ps.setString(1,"create"+i);
                        ps.setString(2,"*****" );
                        ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                        ps.setFloat(4, 500.0f+i);

                        //���
                        ps.addBatch();
                }
                ps.executeBatch();

        } finally {
                JdbcUtils.free(rs, ps, conn);
        }

}
}
