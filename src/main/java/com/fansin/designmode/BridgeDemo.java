package com.fansin.designmode;


import com.alibaba.fastjson.JSONObject;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 17 -12-25 下午11:23
 */
public class BridgeDemo {

    /**
     * 桥接模式:
     * 是一种抽象和现实分离的思想.
     * <p>
     * 应用:
     * 经典应用为JDBC的使用
     * 各个厂商对Driver接口实现,DriverManager调用Driver中的接口,而不用关心底层是哪家厂商,来完成数据库相关操作.
     * <p>
     * 自己写驱动:
     * 1 实现java.sql.Driver接口中的方法
     * 2 将驱动注册到DriverManager中,可以使用静态块注册,一个类只会注册一个,不雅使用构造方法注册,会重复注册.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        /*
        * 按照正常的数据库操作流程
        * 加载驱动->建立连接->查询-关闭
        * */
        try {
            //加载驱动(把具体的实现绑定在接口上)
            Class.forName("com.fansin.designmode.FSDBDriver");
            //建立连接(DriverManager 是实现和抽象结合胶水,对于DriverManager操作的是抽象,而抽象的具体实现由驱动完成)
            Connection connection = DriverManager.getConnection("jdbc:fsdb://localhost:9019/test","user","passwd");
            //查询
            Statement statement = connection.createStatement();
            connection.setAutoCommit(false);
            ResultSet resultSet = statement.executeQuery("search content");
            connection.commit();
            while (resultSet.next()){
                System.out.println("查询结果:"+resultSet.getString("content"));
            }
            //关闭
            resultSet.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}

/**
 * Created by zhaofeng on 17-5-11.
 * 为自己的数据库(虚拟 FSDB )实现接口驱动
 */
@SuppressWarnings({"ALL", "AlibabaClassNamingShouldBeCamel"})
class FSDBDriver implements Driver {


    /** 这里不能少了
     *向DriverManager注册,DriverManager使用CopyOnWriteArrayList来保存注册的驱动信息
     * */
    static {
        try {
            DriverManager.registerDriver(new FSDBDriver());
        } catch (SQLException e) {
            throw new RuntimeException("Can't register driver", e);
        }
    }


    @Override
    public Connection connect(String url, Properties info) throws SQLException {

        System.out.println("连接数据库:url="+url+" properties="+ JSONObject.toJSONString(info));

        return new Connection() {
            @Override
            public Statement createStatement() throws SQLException {
                return new Statement() {
                    @Override
                    public ResultSet executeQuery(String sql) throws SQLException {
                        System.out.println("开始向数据库查询结果.....请等待");
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //noinspection AliDeprecation
                        return new ResultSet() {

                            private boolean isNext = true;
                            @Override
                            public boolean next() throws SQLException {
                                if (isNext){
                                    isNext = false;
                                    return true;
                                }else {
                                    return false;
                                }
                            }

                            @Override
                            public void close() throws SQLException {
                                System.out.println("关闭结果集!");
                            }

                            @Override
                            public boolean wasNull() throws SQLException {
                                return false;
                            }

                            @Override
                            public String getString(int columnIndex) throws SQLException {
                                return null;
                            }

                            @Override
                            public boolean getBoolean(int columnIndex) throws SQLException {
                                return false;
                            }

                            @Override
                            public byte getByte(int columnIndex) throws SQLException {
                                return 0;
                            }

                            @Override
                            public short getShort(int columnIndex) throws SQLException {
                                return 0;
                            }

                            @Override
                            public int getInt(int columnIndex) throws SQLException {
                                return 0;
                            }

                            @Override
                            public long getLong(int columnIndex) throws SQLException {
                                return 0;
                            }

                            @Override
                            public float getFloat(int columnIndex) throws SQLException {
                                return 0;
                            }

                            @Override
                            public double getDouble(int columnIndex) throws SQLException {
                                return 0;
                            }

                            /**
                             * Retrieves the value of the designated column in the current row
                             * of this <code>ResultSet</code> object as
                             * a <code>java.sql.BigDecimal</code> in the Java programming language.
                             *
                             * @param columnIndex the first column is 1, the second is 2, ...
                             * @param scale       the number of digits to the right of the decimal point
                             * @return the column value; if the value is SQL <code>NULL</code>, the
                             * value returned is <code>null</code>
                             * @throws SQLException                    if the columnIndex is not valid;
                             *                                         if a database access error occurs or this method is
                             *                                         called on a closed result set
                             * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
                             *                                         this method
                             * @deprecated Use {@code getBigDecimal(int columnIndex)}
                             * or {@code getBigDecimal(String columnLabel)}
                             */
                            @Deprecated
                            @Override
                            public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
                                return null;
                            }

                            @Override
                            public byte[] getBytes(int columnIndex) throws SQLException {
                                return new byte[0];
                            }

                            @Override
                            public Date getDate(int columnIndex) throws SQLException {
                                return null;
                            }

                            @Override
                            public Time getTime(int columnIndex) throws SQLException {
                                return null;
                            }

                            @Override
                            public Timestamp getTimestamp(int columnIndex) throws SQLException {
                                return null;
                            }

                            @Override
                            public InputStream getAsciiStream(int columnIndex) throws SQLException {
                                return null;
                            }

                            /**
                             * Retrieves the value of the designated column in the current row
                             * of this <code>ResultSet</code> object as
                             * as a stream of two-byte 3 characters. The first byte is
                             * the high byte; the second byte is the low byte.
                             * <p>
                             * The value can then be read in chunks from the
                             * stream. This method is particularly
                             * suitable for retrieving large <code>LONGVARCHAR</code>values.  The
                             * JDBC driver will do any necessary conversion from the database
                             * format into Unicode.
                             * <p>
                             * <P><B>Note:</B> All the data in the returned stream must be
                             * read prior to getting the value of any other column. The next
                             * call to a getter method implicitly closes the stream.
                             * Also, a stream may return <code>0</code> when the method
                             * <code>InputStream.available</code>
                             * is called, whether there is data available or not.
                             *
                             * @param columnIndex the first column is 1, the second is 2, ...
                             * @return a Java input stream that delivers the database column value
                             * as a stream of two-byte Unicode characters;
                             * if the value is SQL <code>NULL</code>, the value returned is
                             * <code>null</code>
                             * @throws SQLException                    if the columnIndex is not valid;
                             *                                         if a database access error occurs or this method is
                             *                                         called on a closed result set
                             * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
                             *                                         this method
                             * @deprecated use <code>getCharacterStream</code> in place of
                             * <code>getUnicodeStream</code>
                             */
                            @Deprecated
                            @Override
                            public InputStream getUnicodeStream(int columnIndex) throws SQLException {
                                return null;
                            }


                            @Override
                            public InputStream getBinaryStream(int columnIndex) throws SQLException {
                                return null;
                            }

                            @Override
                            public String getString(String columnLabel) throws SQLException {
                                return columnLabel+">>>"+sql;
                            }

                            @Override
                            public boolean getBoolean(String columnLabel) throws SQLException {
                                return false;
                            }

                            @Override
                            public byte getByte(String columnLabel) throws SQLException {
                                return 0;
                            }

                            @Override
                            public short getShort(String columnLabel) throws SQLException {
                                return 0;
                            }

                            @Override
                            public int getInt(String columnLabel) throws SQLException {
                                return 0;
                            }

                            @Override
                            public long getLong(String columnLabel) throws SQLException {
                                return 0;
                            }

                            @Override
                            public float getFloat(String columnLabel) throws SQLException {
                                return 0;
                            }

                            @Override
                            public double getDouble(String columnLabel) throws SQLException {
                                return 0;
                            }

                            /**
                             * Retrieves the value of the designated column in the current row
                             * of this <code>ResultSet</code> object as
                             * a <code>java.math.BigDecimal</code> in the Java programming language.
                             *
                             * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
                             * @param scale       the number of digits to the right of the decimal point
                             * @return the column value; if the value is SQL <code>NULL</code>, the
                             * value returned is <code>null</code>
                             * @throws SQLException                    if the columnLabel is not valid;
                             *                                         if a database access error occurs or this method is
                             *                                         called on a closed result set
                             * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
                             *                                         this method
                             * @deprecated Use {@code getBigDecimal(int columnIndex)}
                             * or {@code getBigDecimal(String columnLabel)}
                             */
                            @Deprecated
                            @Override
                            public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
                                return null;
                            }

                            @Override
                            public byte[] getBytes(String columnLabel) throws SQLException {
                                return new byte[0];
                            }

                            @Override
                            public Date getDate(String columnLabel) throws SQLException {
                                return null;
                            }

                            @Override
                            public Time getTime(String columnLabel) throws SQLException {
                                return null;
                            }

                            @Override
                            public Timestamp getTimestamp(String columnLabel) throws SQLException {
                                return null;
                            }

                            @Override
                            public InputStream getAsciiStream(String columnLabel) throws SQLException {
                                return null;
                            }

                            /**
                             * Retrieves the value of the designated column in the current row
                             * of this <code>ResultSet</code> object as a stream of two-byte
                             * Unicode characters. The first byte is the high byte; the second
                             * byte is the low byte.
                             * <p>
                             * The value can then be read in chunks from the
                             * stream. This method is particularly
                             * suitable for retrieving large <code>LONGVARCHAR</code> values.
                             * The JDBC technology-enabled driver will
                             * do any necessary conversion from the database format into Unicode.
                             * <p>
                             * <P><B>Note:</B> All the data in the returned stream must be
                             * read prior to getting the value of any other column. The next
                             * call to a getter method implicitly closes the stream.
                             * Also, a stream may return <code>0</code> when the method
                             * <code>InputStream.available</code> is called, whether there
                             * is data available or not.
                             *
                             * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
                             * @return a Java input stream that delivers the database column value
                             * as a stream of two-byte Unicode characters.
                             * If the value is SQL <code>NULL</code>, the value returned
                             * is <code>null</code>.
                             * @throws SQLException                    if the columnLabel is not valid;
                             *                                         if a database access error occurs or this method is
                             *                                         called on a closed result set
                             * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
                             *                                         this method
                             * @deprecated use <code>getCharacterStream</code> instead
                             */
                            @Deprecated
                            @Override
                            public InputStream getUnicodeStream(String columnLabel) throws SQLException {
                                return null;
                            }

                            @Override
                            public InputStream getBinaryStream(String columnLabel) throws SQLException {
                                return null;
                            }

                            @Override
                            public SQLWarning getWarnings() throws SQLException {
                                return null;
                            }

                            @Override
                            public void clearWarnings() throws SQLException {

                            }

                            @Override
                            public String getCursorName() throws SQLException {
                                return null;
                            }

                            @Override
                            public ResultSetMetaData getMetaData() throws SQLException {
                                return null;
                            }

                            @Override
                            public Object getObject(int columnIndex) throws SQLException {
                                return null;
                            }

                            @Override
                            public Object getObject(String columnLabel) throws SQLException {
                                return null;
                            }

                            @Override
                            public int findColumn(String columnLabel) throws SQLException {
                                return 0;
                            }

                            @Override
                            public Reader getCharacterStream(int columnIndex) throws SQLException {
                                return null;
                            }

                            @Override
                            public Reader getCharacterStream(String columnLabel) throws SQLException {
                                return null;
                            }

                            @Override
                            public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
                                return null;
                            }

                            @Override
                            public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
                                return null;
                            }

                            @Override
                            public boolean isBeforeFirst() throws SQLException {
                                return false;
                            }

                            @Override
                            public boolean isAfterLast() throws SQLException {
                                return false;
                            }

                            @Override
                            public boolean isFirst() throws SQLException {
                                return false;
                            }

                            @Override
                            public boolean isLast() throws SQLException {
                                return false;
                            }

                            @Override
                            public void beforeFirst() throws SQLException {

                            }

                            @Override
                            public void afterLast() throws SQLException {

                            }

                            @Override
                            public boolean first() throws SQLException {
                                return false;
                            }

                            @Override
                            public boolean last() throws SQLException {
                                return false;
                            }

                            @Override
                            public int getRow() throws SQLException {
                                return 0;
                            }

                            @Override
                            public boolean absolute(int row) throws SQLException {
                                return false;
                            }

                            @Override
                            public boolean relative(int rows) throws SQLException {
                                return false;
                            }

                            @Override
                            public boolean previous() throws SQLException {
                                return false;
                            }

                            @Override
                            public void setFetchDirection(int direction) throws SQLException {

                            }

                            @Override
                            public int getFetchDirection() throws SQLException {
                                return 0;
                            }

                            @Override
                            public void setFetchSize(int rows) throws SQLException {

                            }

                            @Override
                            public int getFetchSize() throws SQLException {
                                return 0;
                            }

                            @Override
                            public int getType() throws SQLException {
                                return 0;
                            }

                            @Override
                            public int getConcurrency() throws SQLException {
                                return 0;
                            }

                            @Override
                            public boolean rowUpdated() throws SQLException {
                                return false;
                            }

                            @Override
                            public boolean rowInserted() throws SQLException {
                                return false;
                            }

                            @Override
                            public boolean rowDeleted() throws SQLException {
                                return false;
                            }

                            @Override
                            public void updateNull(int columnIndex) throws SQLException {

                            }

                            @Override
                            public void updateBoolean(int columnIndex, boolean x) throws SQLException {

                            }

                            @Override
                            public void updateByte(int columnIndex, byte x) throws SQLException {

                            }

                            @Override
                            public void updateShort(int columnIndex, short x) throws SQLException {

                            }

                            @Override
                            public void updateInt(int columnIndex, int x) throws SQLException {

                            }

                            @Override
                            public void updateLong(int columnIndex, long x) throws SQLException {

                            }

                            @Override
                            public void updateFloat(int columnIndex, float x) throws SQLException {

                            }

                            @Override
                            public void updateDouble(int columnIndex, double x) throws SQLException {

                            }

                            @Override
                            public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {

                            }

                            @Override
                            public void updateString(int columnIndex, String x) throws SQLException {

                            }

                            @Override
                            public void updateBytes(int columnIndex, byte[] x) throws SQLException {

                            }

                            @Override
                            public void updateDate(int columnIndex, Date x) throws SQLException {

                            }

                            @Override
                            public void updateTime(int columnIndex, Time x) throws SQLException {

                            }

                            @Override
                            public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {

                            }

                            @Override
                            public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {

                            }

                            @Override
                            public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {

                            }

                            @Override
                            public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {

                            }

                            @Override
                            public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {

                            }

                            @Override
                            public void updateObject(int columnIndex, Object x) throws SQLException {

                            }

                            @Override
                            public void updateNull(String columnLabel) throws SQLException {

                            }

                            @Override
                            public void updateBoolean(String columnLabel, boolean x) throws SQLException {

                            }

                            @Override
                            public void updateByte(String columnLabel, byte x) throws SQLException {

                            }

                            @Override
                            public void updateShort(String columnLabel, short x) throws SQLException {

                            }

                            @Override
                            public void updateInt(String columnLabel, int x) throws SQLException {

                            }

                            @Override
                            public void updateLong(String columnLabel, long x) throws SQLException {

                            }

                            @Override
                            public void updateFloat(String columnLabel, float x) throws SQLException {

                            }

                            @Override
                            public void updateDouble(String columnLabel, double x) throws SQLException {

                            }

                            @Override
                            public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {

                            }

                            @Override
                            public void updateString(String columnLabel, String x) throws SQLException {

                            }

                            @Override
                            public void updateBytes(String columnLabel, byte[] x) throws SQLException {

                            }

                            @Override
                            public void updateDate(String columnLabel, Date x) throws SQLException {

                            }

                            @Override
                            public void updateTime(String columnLabel, Time x) throws SQLException {

                            }

                            @Override
                            public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {

                            }

                            @Override
                            public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {

                            }

                            @Override
                            public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {

                            }

                            @Override
                            public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {

                            }

                            @Override
                            public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {

                            }

                            @Override
                            public void updateObject(String columnLabel, Object x) throws SQLException {

                            }

                            @Override
                            public void insertRow() throws SQLException {

                            }

                            @Override
                            public void updateRow() throws SQLException {

                            }

                            @Override
                            public void deleteRow() throws SQLException {

                            }

                            @Override
                            public void refreshRow() throws SQLException {

                            }

                            @Override
                            public void cancelRowUpdates() throws SQLException {

                            }

                            @Override
                            public void moveToInsertRow() throws SQLException {

                            }

                            @Override
                            public void moveToCurrentRow() throws SQLException {

                            }

                            @Override
                            public Statement getStatement() throws SQLException {
                                return null;
                            }

                            @Override
                            public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
                                return null;
                            }

                            @Override
                            public Ref getRef(int columnIndex) throws SQLException {
                                return null;
                            }

                            @Override
                            public Blob getBlob(int columnIndex) throws SQLException {
                                return null;
                            }

                            @Override
                            public Clob getClob(int columnIndex) throws SQLException {
                                return null;
                            }

                            @Override
                            public Array getArray(int columnIndex) throws SQLException {
                                return null;
                            }

                            @Override
                            public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
                                return null;
                            }

                            @Override
                            public Ref getRef(String columnLabel) throws SQLException {
                                return null;
                            }

                            @Override
                            public Blob getBlob(String columnLabel) throws SQLException {
                                return null;
                            }

                            @Override
                            public Clob getClob(String columnLabel) throws SQLException {
                                return null;
                            }

                            @Override
                            public Array getArray(String columnLabel) throws SQLException {
                                return null;
                            }

                            @Override
                            public Date getDate(int columnIndex, Calendar cal) throws SQLException {
                                return null;
                            }

                            @Override
                            public Date getDate(String columnLabel, Calendar cal) throws SQLException {
                                return null;
                            }

                            @Override
                            public Time getTime(int columnIndex, Calendar cal) throws SQLException {
                                return null;
                            }

                            @Override
                            public Time getTime(String columnLabel, Calendar cal) throws SQLException {
                                return null;
                            }

                            @Override
                            public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
                                return null;
                            }

                            @Override
                            public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
                                return null;
                            }

                            @Override
                            public URL getURL(int columnIndex) throws SQLException {
                                return null;
                            }

                            @Override
                            public URL getURL(String columnLabel) throws SQLException {
                                return null;
                            }

                            @Override
                            public void updateRef(int columnIndex, Ref x) throws SQLException {

                            }

                            @Override
                            public void updateRef(String columnLabel, Ref x) throws SQLException {

                            }

                            @Override
                            public void updateBlob(int columnIndex, Blob x) throws SQLException {

                            }

                            @Override
                            public void updateBlob(String columnLabel, Blob x) throws SQLException {

                            }

                            @Override
                            public void updateClob(int columnIndex, Clob x) throws SQLException {

                            }

                            @Override
                            public void updateClob(String columnLabel, Clob x) throws SQLException {

                            }

                            @Override
                            public void updateArray(int columnIndex, Array x) throws SQLException {

                            }

                            @Override
                            public void updateArray(String columnLabel, Array x) throws SQLException {

                            }

                            @Override
                            public RowId getRowId(int columnIndex) throws SQLException {
                                return null;
                            }

                            @Override
                            public RowId getRowId(String columnLabel) throws SQLException {
                                return null;
                            }

                            @Override
                            public void updateRowId(int columnIndex, RowId x) throws SQLException {

                            }

                            @Override
                            public void updateRowId(String columnLabel, RowId x) throws SQLException {

                            }

                            @Override
                            public int getHoldability() throws SQLException {
                                return 0;
                            }

                            @Override
                            public boolean isClosed() throws SQLException {
                                return false;
                            }

                            @Override
                            public void updateNString(int columnIndex, String nString) throws SQLException {

                            }

                            @Override
                            public void updateNString(String columnLabel, String nString) throws SQLException {

                            }

                            @Override
                            public void updateNClob(int columnIndex, NClob nClob) throws SQLException {

                            }

                            @Override
                            public void updateNClob(String columnLabel, NClob nClob) throws SQLException {

                            }

                            @Override
                            public NClob getNClob(int columnIndex) throws SQLException {
                                return null;
                            }

                            @Override
                            public NClob getNClob(String columnLabel) throws SQLException {
                                return null;
                            }

                            @Override
                            public SQLXML getSQLXML(int columnIndex) throws SQLException {
                                return null;
                            }

                            @Override
                            public SQLXML getSQLXML(String columnLabel) throws SQLException {
                                return null;
                            }

                            @Override
                            public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {

                            }

                            @Override
                            public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {

                            }

                            @Override
                            public String getNString(int columnIndex) throws SQLException {
                                return null;
                            }

                            @Override
                            public String getNString(String columnLabel) throws SQLException {
                                return null;
                            }

                            @Override
                            public Reader getNCharacterStream(int columnIndex) throws SQLException {
                                return null;
                            }

                            @Override
                            public Reader getNCharacterStream(String columnLabel) throws SQLException {
                                return null;
                            }

                            @Override
                            public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {

                            }

                            @Override
                            public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {

                            }

                            @Override
                            public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {

                            }

                            @Override
                            public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {

                            }

                            @Override
                            public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {

                            }

                            @Override
                            public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {

                            }

                            @Override
                            public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {

                            }

                            @Override
                            public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {

                            }

                            @Override
                            public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {

                            }

                            @Override
                            public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {

                            }

                            @Override
                            public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {

                            }

                            @Override
                            public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {

                            }

                            @Override
                            public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {

                            }

                            @Override
                            public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {

                            }

                            @Override
                            public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {

                            }

                            @Override
                            public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {

                            }

                            @Override
                            public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {

                            }

                            @Override
                            public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {

                            }

                            @Override
                            public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {

                            }

                            @Override
                            public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {

                            }

                            @Override
                            public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {

                            }

                            @Override
                            public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {

                            }

                            @Override
                            public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {

                            }

                            @Override
                            public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {

                            }

                            @Override
                            public void updateClob(int columnIndex, Reader reader) throws SQLException {

                            }

                            @Override
                            public void updateClob(String columnLabel, Reader reader) throws SQLException {

                            }

                            @Override
                            public void updateNClob(int columnIndex, Reader reader) throws SQLException {

                            }

                            @Override
                            public void updateNClob(String columnLabel, Reader reader) throws SQLException {

                            }

                            @Override
                            public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
                                return null;
                            }

                            @Override
                            public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
                                return null;
                            }

                            @Override
                            public <T> T unwrap(Class<T> iface) throws SQLException {
                                return null;
                            }

                            @Override
                            public boolean isWrapperFor(Class<?> iface) throws SQLException {
                                return false;
                            }
                        };
                    }

                    @Override
                    public int executeUpdate(String sql) throws SQLException {
                        System.out.println("更新结果......这里也ok!");
                        return 0;
                    }

                    @Override
                    public void close() throws SQLException {
                        System.out.println("关闭了结果集....");
                    }

                    @Override
                    public int getMaxFieldSize() throws SQLException {
                        return 0;
                    }

                    @Override
                    public void setMaxFieldSize(int max) throws SQLException {

                    }

                    @Override
                    public int getMaxRows() throws SQLException {
                        return 0;
                    }

                    @Override
                    public void setMaxRows(int max) throws SQLException {

                    }

                    @Override
                    public void setEscapeProcessing(boolean enable) throws SQLException {

                    }

                    @Override
                    public int getQueryTimeout() throws SQLException {
                        return 0;
                    }

                    @Override
                    public void setQueryTimeout(int seconds) throws SQLException {

                    }

                    @Override
                    public void cancel() throws SQLException {

                    }

                    @Override
                    public SQLWarning getWarnings() throws SQLException {
                        return null;
                    }

                    @Override
                    public void clearWarnings() throws SQLException {

                    }

                    @Override
                    public void setCursorName(String name) throws SQLException {

                    }

                    @Override
                    public boolean execute(String sql) throws SQLException {
                        return false;
                    }

                    @Override
                    public ResultSet getResultSet() throws SQLException {
                        return null;
                    }

                    @Override
                    public int getUpdateCount() throws SQLException {
                        return 0;
                    }

                    @Override
                    public boolean getMoreResults() throws SQLException {
                        return false;
                    }

                    @Override
                    public void setFetchDirection(int direction) throws SQLException {

                    }

                    @Override
                    public int getFetchDirection() throws SQLException {
                        return 0;
                    }

                    @Override
                    public void setFetchSize(int rows) throws SQLException {

                    }

                    @Override
                    public int getFetchSize() throws SQLException {
                        return 0;
                    }

                    @Override
                    public int getResultSetConcurrency() throws SQLException {
                        return 0;
                    }

                    @Override
                    public int getResultSetType() throws SQLException {
                        return 0;
                    }

                    @Override
                    public void addBatch(String sql) throws SQLException {

                    }

                    @Override
                    public void clearBatch() throws SQLException {

                    }

                    @Override
                    public int[] executeBatch() throws SQLException {
                        return new int[0];
                    }

                    @Override
                    public Connection getConnection() throws SQLException {
                        return null;
                    }

                    @Override
                    public boolean getMoreResults(int current) throws SQLException {
                        return false;
                    }

                    @Override
                    public ResultSet getGeneratedKeys() throws SQLException {
                        return null;
                    }

                    @Override
                    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
                        return 0;
                    }

                    @Override
                    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
                        return 0;
                    }

                    @Override
                    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
                        return 0;
                    }

                    @Override
                    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
                        return false;
                    }

                    @Override
                    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
                        return false;
                    }

                    @Override
                    public boolean execute(String sql, String[] columnNames) throws SQLException {
                        return false;
                    }

                    @Override
                    public int getResultSetHoldability() throws SQLException {
                        return 0;
                    }

                    @Override
                    public boolean isClosed() throws SQLException {
                        return false;
                    }

                    @Override
                    public void setPoolable(boolean poolable) throws SQLException {

                    }

                    @Override
                    public boolean isPoolable() throws SQLException {
                        return false;
                    }

                    @Override
                    public void closeOnCompletion() throws SQLException {

                    }

                    @Override
                    public boolean isCloseOnCompletion() throws SQLException {
                        return false;
                    }

                    @Override
                    public <T> T unwrap(Class<T> iface) throws SQLException {
                        return null;
                    }

                    @Override
                    public boolean isWrapperFor(Class<?> iface) throws SQLException {
                        return false;
                    }
                };
            }

            @Override
            public PreparedStatement prepareStatement(String sql) throws SQLException {
                return null;
            }

            @Override
            public CallableStatement prepareCall(String sql) throws SQLException {
                return null;
            }

            @Override
            public String nativeSQL(String sql) throws SQLException {
                return null;
            }

            @Override
            public void setAutoCommit(boolean autoCommit) throws SQLException {
                System.out.println("设置为手动提交事务!");
            }

            @Override
            public boolean getAutoCommit() throws SQLException {
                return false;
            }

            @Override
            public void commit() throws SQLException {
                System.out.println("提交事务....");
            }

            @Override
            public void rollback() throws SQLException {

            }

            @Override
            public void close() throws SQLException {
                System.out.println("关闭连接....");
            }

            @Override
            public boolean isClosed() throws SQLException {
                return false;
            }

            @Override
            public DatabaseMetaData getMetaData() throws SQLException {
                return null;
            }

            @Override
            public void setReadOnly(boolean readOnly) throws SQLException {

            }

            @Override
            public boolean isReadOnly() throws SQLException {
                return false;
            }

            @Override
            public void setCatalog(String catalog) throws SQLException {

            }

            @Override
            public String getCatalog() throws SQLException {
                return null;
            }

            @Override
            public void setTransactionIsolation(int level) throws SQLException {

            }

            @Override
            public int getTransactionIsolation() throws SQLException {
                return 0;
            }

            @Override
            public SQLWarning getWarnings() throws SQLException {
                return null;
            }

            @Override
            public void clearWarnings() throws SQLException {

            }

            @Override
            public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
                return null;
            }

            @Override
            public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
                return null;
            }

            @Override
            public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
                return null;
            }

            @Override
            public Map<String, Class<?>> getTypeMap() throws SQLException {
                return null;
            }

            @Override
            public void setTypeMap(Map<String, Class<?>> map) throws SQLException {

            }

            @Override
            public void setHoldability(int holdability) throws SQLException {

            }

            @Override
            public int getHoldability() throws SQLException {
                return 0;
            }

            @Override
            public Savepoint setSavepoint() throws SQLException {
                return null;
            }

            @Override
            public Savepoint setSavepoint(String name) throws SQLException {
                return null;
            }

            @Override
            public void rollback(Savepoint savepoint) throws SQLException {

            }

            @Override
            public void releaseSavepoint(Savepoint savepoint) throws SQLException {

            }

            @Override
            public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
                return null;
            }

            @Override
            public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
                return null;
            }

            @Override
            public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
                return null;
            }

            @Override
            public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
                return null;
            }

            @Override
            public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
                return null;
            }

            @Override
            public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
                return null;
            }

            @Override
            public Clob createClob() throws SQLException {
                return null;
            }

            @Override
            public Blob createBlob() throws SQLException {
                return null;
            }

            @Override
            public NClob createNClob() throws SQLException {
                return null;
            }

            @Override
            public SQLXML createSQLXML() throws SQLException {
                return null;
            }

            @Override
            public boolean isValid(int timeout) throws SQLException {
                return false;
            }

            @Override
            public void setClientInfo(String name, String value) throws SQLClientInfoException {

            }

            @Override
            public void setClientInfo(Properties properties) throws SQLClientInfoException {

            }

            @Override
            public String getClientInfo(String name) throws SQLException {
                return null;
            }

            @Override
            public Properties getClientInfo() throws SQLException {
                return null;
            }

            @Override
            public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
                return null;
            }

            @Override
            public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
                return null;
            }

            @Override
            public void setSchema(String schema) throws SQLException {

            }

            @Override
            public String getSchema() throws SQLException {
                return null;
            }

            @Override
            public void abort(Executor executor) throws SQLException {

            }

            @Override
            public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {

            }

            @Override
            public int getNetworkTimeout() throws SQLException {
                return 0;
            }

            @Override
            public <T> T unwrap(Class<T> iface) throws SQLException {
                return null;
            }

            @Override
            public boolean isWrapperFor(Class<?> iface) throws SQLException {
                return false;
            }
        };
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return true;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}

