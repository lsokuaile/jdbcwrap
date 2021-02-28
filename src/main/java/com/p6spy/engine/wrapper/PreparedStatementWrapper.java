//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.p6spy.engine.wrapper;

import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import com.ghca.utils.ResultSetPrinter;
import com.ghca.utils.SqlUtis;
import com.p6spy.engine.common.PreparedStatementInformation;
import com.p6spy.engine.common.ResultSetInformation;
import com.p6spy.engine.event.JdbcEventListener;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Collection;

public class PreparedStatementWrapper extends StatementWrapper implements PreparedStatement {
    private final PreparedStatement delegate;
    private final PreparedStatementInformation statementInformation;

    public static PreparedStatement wrap(PreparedStatement delegate, PreparedStatementInformation preparedStatementInformation, JdbcEventListener eventListener) {
        //return delegate == null ? null : new PreparedStatementWrapper(delegate, preparedStatementInformation, eventListener);
        // 拦截sql，脱敏处理
        String statementQuery =  preparedStatementInformation.getStatementQuery();
        Collection<TableStat.Column> columnList =  SqlUtis.extracted(JdbcConstants.MYSQL);
        System.out.println(columnList);
        // 规则引擎
        System.out.println("拦截sql：");
        System.out.println(statementQuery);
        System.out.println();
        // 替换sql
        preparedStatementInformation.setStatementQuery(statementQuery);
        if (delegate == null) {
            return null;
        }
        return new PreparedStatementWrapper(delegate, preparedStatementInformation, eventListener);
    }

    protected PreparedStatementWrapper(PreparedStatement delegate, PreparedStatementInformation preparedStatementInformation, JdbcEventListener eventListener) {
        super(delegate, preparedStatementInformation, eventListener);
        this.delegate = delegate;
        this.statementInformation = preparedStatementInformation;
    }

    public ResultSet executeQuery() throws SQLException {
        SQLException e = null;
        long start = System.nanoTime();

        ResultSet var4;
        try {
            this.eventListener.onBeforeExecuteQuery(this.statementInformation);
            var4 = ResultSetWrapper.wrap(this.delegate.executeQuery(), new ResultSetInformation(this.statementInformation), this.eventListener);
            System.out.println("拦截数据：");
            ResultSetPrinter.printResultSet(var4);
            System.out.println();
            // 业务
            //ResultSetPrinter.printResultSet(var4);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterExecuteQuery(this.statementInformation, System.nanoTime() - start, e);
        }

        return var4;
    }

    public int executeUpdate() throws SQLException {
        SQLException e = null;
        long start = System.nanoTime();
        int rowCount = 0;

        int var5;
        try {
            this.eventListener.onBeforeExecuteUpdate(this.statementInformation);
            rowCount = this.delegate.executeUpdate();
            var5 = rowCount;
        } catch (SQLException var9) {
            e = var9;
            throw var9;
        } finally {
            this.eventListener.onAfterExecuteUpdate(this.statementInformation, System.nanoTime() - start, rowCount, e);
        }

        return var5;
    }

    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setNull(parameterIndex, sqlType);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, (Object)null, e);
        }

    }

    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setBoolean(parameterIndex, x);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setByte(int parameterIndex, byte x) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setByte(parameterIndex, x);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setShort(int parameterIndex, short x) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setShort(parameterIndex, x);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setInt(int parameterIndex, int x) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setInt(parameterIndex, x);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setLong(int parameterIndex, long x) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setLong(parameterIndex, x);
        } catch (SQLException var9) {
            e = var9;
            throw var9;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setFloat(int parameterIndex, float x) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setFloat(parameterIndex, x);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setDouble(int parameterIndex, double x) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setDouble(parameterIndex, x);
        } catch (SQLException var9) {
            e = var9;
            throw var9;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setBigDecimal(parameterIndex, x);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setString(int parameterIndex, String x) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setString(parameterIndex, x);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setBytes(parameterIndex, x);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setDate(int parameterIndex, Date x) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setDate(parameterIndex, x);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setTime(int parameterIndex, Time x) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setTime(parameterIndex, x);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setTimestamp(parameterIndex, x);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setAsciiStream(parameterIndex, x, length);
        } catch (SQLException var9) {
            e = var9;
            throw var9;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setUnicodeStream(parameterIndex, x, length);
        } catch (SQLException var9) {
            e = var9;
            throw var9;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setBinaryStream(parameterIndex, x, length);
        } catch (SQLException var9) {
            e = var9;
            throw var9;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void clearParameters() throws SQLException {
        this.delegate.clearParameters();
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setObject(parameterIndex, x, targetSqlType);
        } catch (SQLException var9) {
            e = var9;
            throw var9;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setObject(int parameterIndex, Object x) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setObject(parameterIndex, x);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public boolean execute() throws SQLException {
        SQLException e = null;
        long start = System.nanoTime();

        boolean var4;
        try {
            this.eventListener.onBeforeExecute(this.statementInformation);
            var4 = this.delegate.execute();
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterExecute(this.statementInformation, System.nanoTime() - start, e);
        }

        return var4;
    }

    public void addBatch() throws SQLException {
        SQLException e = null;
        long start = System.nanoTime();

        try {
            this.eventListener.onBeforeAddBatch(this.statementInformation);
            this.delegate.addBatch();
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterAddBatch(this.statementInformation, System.nanoTime() - start, e);
        }

    }

    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setCharacterStream(parameterIndex, reader, length);
        } catch (SQLException var9) {
            e = var9;
            throw var9;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, reader, e);
        }

    }

    public void setRef(int parameterIndex, Ref x) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setRef(parameterIndex, x);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setBlob(parameterIndex, x);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setClob(int parameterIndex, Clob x) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setClob(parameterIndex, x);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setArray(int parameterIndex, Array x) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setArray(parameterIndex, x);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setDate(parameterIndex, x, cal);
        } catch (SQLException var9) {
            e = var9;
            throw var9;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setTime(parameterIndex, x, cal);
        } catch (SQLException var9) {
            e = var9;
            throw var9;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setTimestamp(parameterIndex, x, cal);
        } catch (SQLException var9) {
            e = var9;
            throw var9;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setNull(parameterIndex, sqlType, typeName);
        } catch (SQLException var9) {
            e = var9;
            throw var9;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, (Object)null, e);
        }

    }

    public void setURL(int parameterIndex, URL x) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setURL(parameterIndex, x);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setRowId(parameterIndex, x);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setNString(int parameterIndex, String value) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setNString(parameterIndex, value);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, value, e);
        }

    }

    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setNCharacterStream(parameterIndex, value, length);
        } catch (SQLException var10) {
            e = var10;
            throw var10;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, value, e);
        }

    }

    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setNClob(parameterIndex, value);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, value, e);
        }

    }

    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setClob(parameterIndex, reader, length);
        } catch (SQLException var10) {
            e = var10;
            throw var10;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, reader, e);
        }

    }

    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setBlob(parameterIndex, inputStream, length);
        } catch (SQLException var10) {
            e = var10;
            throw var10;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, inputStream, e);
        }

    }

    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setNClob(parameterIndex, reader, length);
        } catch (SQLException var10) {
            e = var10;
            throw var10;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, reader, e);
        }

    }

    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setSQLXML(parameterIndex, xmlObject);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, xmlObject, e);
        }

    }

    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
        } catch (SQLException var10) {
            e = var10;
            throw var10;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setAsciiStream(parameterIndex, x, length);
        } catch (SQLException var10) {
            e = var10;
            throw var10;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setBinaryStream(parameterIndex, x, length);
        } catch (SQLException var10) {
            e = var10;
            throw var10;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setCharacterStream(parameterIndex, reader, length);
        } catch (SQLException var10) {
            e = var10;
            throw var10;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, reader, e);
        }

    }

    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setAsciiStream(parameterIndex, x);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setBinaryStream(parameterIndex, x);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, x, e);
        }

    }

    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setCharacterStream(parameterIndex, reader);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, reader, e);
        }

    }

    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setNCharacterStream(parameterIndex, value);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, value, e);
        }

    }

    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setClob(parameterIndex, reader);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, reader, e);
        }

    }

    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setBlob(parameterIndex, inputStream);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, inputStream, e);
        }

    }

    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        SQLException e = null;

        try {
            this.delegate.setNClob(parameterIndex, reader);
        } catch (SQLException var8) {
            e = var8;
            throw var8;
        } finally {
            this.eventListener.onAfterPreparedStatementSet(this.statementInformation, parameterIndex, reader, e);
        }

    }

    public ParameterMetaData getParameterMetaData() throws SQLException {
        return this.delegate.getParameterMetaData();
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        return this.delegate.getMetaData();
    }
}
