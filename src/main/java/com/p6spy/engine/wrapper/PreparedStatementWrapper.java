/**
 * P6Spy
 * <p>
 * Copyright (C) 2002 - 2020 P6Spy
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.p6spy.engine.wrapper;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.fastjson.JSONObject;
import com.ghca.masking.entity.orm.table.core.strategy.audit.app.AuditAppDataRule;
import com.ghca.masking.entity.orm.table.core.strategy.audit.conditions.AuditApplicationConditions;
import com.ghca.masking.entity.orm.table.core.strategy.audit.conditions.AuditDateConditions;
import com.ghca.masking.entity.orm.table.core.strategy.audit.conditions.AuditDbuserConditions;
import com.ghca.masking.entity.orm.table.core.strategy.audit.conditions.AuditIpConditions;
import com.ghca.masking.entity.orm.table.core.strategy.audit.vo.ConnectionInfo;
import com.ghca.masking.tools.object.ObjectTool;
import com.ghca.masking.tools.permission.AuthUtil;
import com.ghca.masking.tools.sql.SqlParseTool;
import com.ghca.utils.RequestUtil;
import com.p6spy.engine.common.PreparedStatementInformation;
import com.p6spy.engine.common.ResultSetInformation;
import com.p6spy.engine.event.JdbcEventListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This implementation wraps a {@link PreparedStatement}  and notifies a {@link JdbcEventListener}
 * about certain method invocations.
 * <p>
 * This class implements the Wrapper or Decorator pattern. Methods default
 * to calling through to the wrapped request object.
 *
 * @see PreparedStatement
 */
public class PreparedStatementWrapper extends StatementWrapper implements PreparedStatement {

    private final PreparedStatement delegate;
    private final PreparedStatementInformation statementInformation;

    public static PreparedStatement wrap(PreparedStatement delegate, PreparedStatementInformation preparedStatementInformation, JdbcEventListener eventListener) {
        // 模拟客户端用户，默认admin
        String clientUsername = "admin";
        System.out.println("------------------------------------------------");
        System.out.println("客户端用户名：" + clientUsername);
        String sql = preparedStatementInformation.getStatementQuery(); //原始sql
        System.out.println("------------------------------------------------");
        System.out.println("拦截到sql：\n" + sql);
        // 根据用户名查询规则
        Map params = userAuth(clientUsername);
        // sql替换引擎
        try {
            if (!ObjectUtils.isEmpty(params)) {
                sql = engine(params, sql);
                preparedStatementInformation.setStatementQuery(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (delegate == null) {
            return null;
        }
        return new PreparedStatementWrapper(delegate, preparedStatementInformation, eventListener);
    }

    private static Map userAuth(String username) {
        String jsonStr = AuthUtil.queryByUsername(username);
        if (StringUtils.isEmpty(jsonStr)) {
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        Map params = (Map) jsonObject.get("data");
        System.out.println("------------------------------------------------");
        System.out.printf("用户=%s,权限=%s", username, JSONObject.toJSON(params));
        return params;
    }

    /**
     * 审计规则,记录日志到文件; 脱敏规则处理sql做替换
     */
    private static String engine(Map params, String sql) throws Exception {
        // 数据库连接信息
        ConnectionInfo conInfo = getConnectionInfo();
        // 验证是否符合安全管控的条件
        List<Map<String, Object>> list = getAuthAppDataRule(params, conInfo);
        Boolean flag = false;
        for (Map<String, Object> ruleinfo : list) {
            if (ruleinfo.get("type").equals("2")) {  // 审计
                System.out.println("------------------------------------------------");
                System.out.println("审计");
                // TODO 保存到文件
            } else if (ruleinfo.get("type").equals("1")) {  // 脱敏
                sql = doMasking(sql, ruleinfo);
                System.out.println("------------------------------------------------");
                System.out.println("替换后sql：" + sql);
            }
        }
        return sql;
    }

    /**
     * 脱敏业务
     *
     * @param sql
     * @param ruleinfo
     * @return
     */
    private static String doMasking(String sql, Map<String, Object> ruleinfo) {
        StringBuffer sb = new StringBuffer("");
        // sql解析
        Collection<TableStat.Column> tablefieldList = getColumns(sql);
        if (tablefieldList.isEmpty()) return sql;
        // 表达式
        final String desensitizationexpression = (String) ruleinfo.get("desensitizationexpression");
        // 表名
        final String tablename = (String) ruleinfo.get("tablename");
        // 列名
        final String columnname = (String) ruleinfo.get("columnname");
        System.out.println("------------------------------------------------");
        System.out.printf("表达式=%s", desensitizationexpression);
        System.out.println("");
        tablefieldList.stream().forEach(tablefield -> {
            // 表名、列名完全匹配
            if (tablefield.getTable().equals(tablename) && tablefield.getName().equals(columnname)) {
                sb.setLength(0);
                System.out.printf("替换sql中表名%s.字段名%s", tablefield.getTable(), tablefield.getName());
                System.out.println("");
                sb.append(sql.replaceFirst(tablefield.getName(), desensitizationexpression));
            }
        });
        return sb.toString();
    }

    /**
     * 获取数据库连接信息
     *
     * @throws SQLException
     */
    private static ConnectionInfo getConnectionInfo() throws SQLException {
        ConnectionInfo conInfo = new ConnectionInfo();
        // 数据库用户名，conn中获取
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        DatabaseMetaData metaData = (DatabaseMetaData) request.getSession().getAttribute("metaData");
        String userName = metaData.getUserName().substring(0, metaData.getUserName().indexOf('@'));
        conInfo.setUserName(userName);
        String url = metaData.getURL();
        conInfo.setUrl(url);
        String schema = url.substring(url.indexOf("/", 15) + 1, url.indexOf('?'));
        String dbip = url.substring(url.indexOf("//") + 2, url.indexOf(':', 15));
        conInfo.setSchema(schema);
        // 客户端ip
        String clientIp = RequestUtil.getRequestIp(request);
        conInfo.setClientIp(clientIp);
        System.out.println("------------------------------------------------");
        System.out.printf("客户端ip=%s", clientIp);
        System.out.println("");
        System.out.println("------------------------------------------------");
        System.out.printf("数据库url=%s", url);
        System.out.println("");
        System.out.println("------------------------------------------------");
        System.out.printf("数据库用户名=%s", userName);
        System.out.println("");
        System.out.println("------------------------------------------------");
        System.out.printf("数据库ip=%s", dbip);
        System.out.println("");
        System.out.println("------------------------------------------------");
        System.out.printf("数据库schemas=%s", schema);
        System.out.println("");
        return conInfo;
    }

    /**
     * 校验符合条件的（返回满足条件的）
     *
     * @param params 权限
     * @param ruleinfo 规则信息
     * @param conInfo db连接信息
     * @return
     */

    /**
     * []
     * admin = {
     * "code": "POP_00014",
     * "msg": "成功22。",
     * "flag": true,
     * "data": {
     * "tableinfoList": [{
     * "schema": "ghca2",
     * "tablename": "information_schema.tables",
     * "columnname": "table_name",
     * "type": "1",
     * "ruleid": {
     * "ruleinfo": {
     * "id": "",
     * "name": "策略",
     * "rulename": "规则名称",
     * "type": "1",
     * "belongtogroup": ",1",
     * "dataareaGroup": "",
     * "dbGroup": "1",
     * "remark": "规则描述",
     * "desensitizationexpression": "substr(table_name,6)",
     * "auditswitch": "1"
     * },
     * "ipconditionslist": [],
     * "applicationconditionslist": [{
     * "matchsymbol": "=",
     * "matchval": "admin"
     * }],
     * "dbuserconditionslist": [{
     * "matchsymbol": "=",
     * "matchval": "root"
     * }],
     * "dateconditionslist": [{
     * "daterange": ["2021-03-10T16:00:00.000Z", "2021-04-09T16:00:00.000Z"],
     * "timerange": ["00:00", "03:00"],
     * "starttime": "2021/03/18 00:00",
     * "endtime": "2021/04/10 03:00"
     * }]
     * }
     * }]
     * },
     * "total": 5,
     * "pages": 0,
     * "pageSize": 0,
     * "pageNum": 0
     * }
     **/

    private static List<Map<String, Object>> getAuthAppDataRule(Map params, ConnectionInfo conInfo) {
        // 模拟权限列表
        List<Map<String, Object>> authlist = new ArrayList<Map<String, Object>>();
        Map<String, Object> authMap = new HashMap<String, Object>();
        Boolean flag = false;
        //获取表
        List<Map<String, Object>> tableinfoList = (List<Map<String, Object>>) params.get("tableinfoList");
        tableinfoList.forEach(
                tableinfo -> {
                    // schema名称完全匹配
                    if (!tableinfo.get("schema").equals(conInfo.getSchema())) {
                        return;
                    }
                    authMap.put("schema", tableinfo.get("schema"));
                    authMap.put("tablename", tableinfo.get("tablename"));
                    authMap.put("columnname", tableinfo.get("columnname"));
                    authMap.put("type", tableinfo.get("type"));
                    Map<String, Object> tableInfoMap = (Map<String, Object>) (tableinfo.get("ruleid"));
                    AuditAppDataRule ruleinfo = (AuditAppDataRule) ObjectTool.getObjectByMap(AuditAppDataRule.class, (Map<String, Object>) tableInfoMap.get("ruleinfo"));
                    System.out.println("------------------------------------------------");
                    System.out.printf("获取表达式=%s", ruleinfo.getDesensitizationexpression());
                    System.out.println("");
                    authMap.put("name", tableinfo.get("name"));//策略
                    authMap.put("rulename", tableinfo.get("rulename"));//规则名称
                    authMap.put("remark", tableinfo.get("remark")); //规则描述
                    if (StringUtils.isBlank(ruleinfo.getDesensitizationexpression())) return;
                    authMap.put("desensitizationexpression", ruleinfo.getDesensitizationexpression());
                    //获取IP条件信息
                    List<Map<String, Object>> ipconditionslist = (List<Map<String, Object>>) tableInfoMap.get("ipconditionslist");
                    final String clientIp = conInfo.getClientIp();
                    // 模拟登录用户，默认admin登录
                    String clientUsername = "admin";
                    for (Map<String, Object> ipconditions : ipconditionslist) {
                        AuditIpConditions ipcs = (AuditIpConditions) ObjectTool.getObjectByMap(AuditIpConditions.class, (Map<String, Object>) ipconditions);
                        System.out.println("------------------------------------------------");
                        System.out.printf("获取IP条件=%s%s", ipcs.getMatchsymbol(), ipcs.getMatchval());
                        System.out.println("");
                        // 来源ip 等于 不等于 包含 不包含  (= <> =% <>%)
                        if (ipcs.getMatchsymbol().equals("=")) {
                            if (clientIp.equals(ipcs.getMatchval())) {
                                System.out.println("------------------------------------------------");
                                System.out.printf("满足IP条件%s%s%s", clientIp, ipcs.getMatchsymbol(), ipcs.getMatchval());
                                break;
                            } else {
                                authMap.clear();
                            }
                        } else if (ipcs.getMatchsymbol().equals("<>")) {
                            if (!clientIp.equals(ipcs.getMatchval())) {
                                System.out.println("------------------------------------------------");
                                System.out.printf("满足IP条件%s%s%s", clientIp, ipcs.getMatchsymbol(), ipcs.getMatchval());
                                break;
                            } else {
                                authMap.clear();
                            }
                        } else if (ipcs.getMatchsymbol().equals("=%")) {
                            if (RequestUtil.ipIsValid(ipcs.getStartval(), ipcs.getEndval(), clientIp)) {
                                System.out.println("------------------------------------------------");
                                System.out.printf("满足IP条件%s%s%s", clientIp, ipcs.getMatchsymbol(), ipcs.getMatchval());
                                break;
                            } else {
                                authMap.clear();
                            }
                        } else if (ipcs.getMatchsymbol().equals("<>%")) {
                            if (!RequestUtil.ipIsValid(ipcs.getStartval(), ipcs.getEndval(), clientIp)) {
                                System.out.println("------------------------------------------------");
                                System.out.printf("满足IP条件%s%s%s", clientIp, ipcs.getMatchsymbol(), ipcs.getMatchval());
                                break;
                            } else {
                                authMap.clear();
                            }
                        }
                    }
                    //获取应用条件信息
                    List<Map<String, Object>> applicationconditionslist = (List<Map<String, Object>>) tableInfoMap.get("applicationconditionslist");
                    for (Map<String, Object> applicationconditions : applicationconditionslist) {
                        AuditApplicationConditions appcs = (AuditApplicationConditions) ObjectTool.getObjectByMap(AuditApplicationConditions.class, (Map<String, Object>) applicationconditions);
                        System.out.println("------------------------------------------------");
                        System.out.printf("获取应用条件%s%s", appcs.getMatchsymbol(), appcs.getMatchval());
                        System.out.println("");
                        // 应用条件 等于 不等于  (= <>)
                        if (appcs.getMatchsymbol().equals("=")) {
                            if (clientUsername.equals(appcs.getMatchval())) {
                                System.out.println("------------------------------------------------");
                                System.out.printf("满足应用条件%s%s%s", clientUsername, appcs.getMatchsymbol(), appcs.getMatchval());
                                System.out.println("");
                                break;
                            } else {
                                authMap.clear();
                            }
                        } else if (appcs.getMatchsymbol().equals("<>")) {
                            if (!clientUsername.equals(appcs.getMatchval())) {
                                System.out.println("------------------------------------------------");
                                System.out.printf("满足应用条件%s%s%s", clientUsername, appcs.getMatchsymbol(), appcs.getMatchval());
                                System.out.println("");
                                break;
                            } else {
                                authMap.clear();
                            }
                        }
                    }
                    //获取数据库用户条件信息
                    List<Map<String, Object>> dbuserconditionslist = (List<Map<String, Object>>) tableInfoMap.get("dbuserconditionslist");
                    for (Map<String, Object> dbuserconditions : dbuserconditionslist) {
                        AuditDbuserConditions dbusercs = (AuditDbuserConditions) ObjectTool.getObjectByMap(AuditDbuserConditions.class, (Map<String, Object>) dbuserconditions);
                        System.out.println("------------------------------------------------");
                        System.out.printf("获取数据库用户=%s", dbusercs.getMatchval());
                        System.out.println("");
                        // 数据库用户条件 等于 不等于  (= <>)
                        if (dbusercs.getMatchsymbol().equals("=")) {
                            if (conInfo.getUserName().equals(dbusercs.getMatchval())) {
                                System.out.println("------------------------------------------------");
                                System.out.printf("满足数据库用户条件%s%s%s", conInfo.getUserName(), dbusercs.getMatchsymbol(), dbusercs.getMatchval());
                                System.out.println("");
                                break;
                            } else {
                                authMap.clear();
                            }
                        } else if (dbusercs.getMatchsymbol().equals("<>")) {
                            if (!conInfo.getUserName().equals(dbusercs.getMatchval())) {
                                System.out.println("------------------------------------------------");
                                System.out.printf("满足数据库用户条件%s%s%s", conInfo.getUserName(), dbusercs.getMatchsymbol(), dbusercs.getMatchval());
                                break;
                            } else {
                                authMap.clear();
                            }
                        }
                    }
                    //获取时间条件信息
                    List dateconditionslist = (List) tableInfoMap.get("dateconditionslist");
                    dateconditionslist.forEach(
                            dateconditions -> {
                                AuditDateConditions datecs = (AuditDateConditions) ObjectTool.getObjectByMap(AuditDateConditions.class, (Map<String, Object>) dateconditions);
                                System.out.println("------------------------------------------------");
                                System.out.printf("获取时间条件%s , %s", datecs.getStarttime(), datecs.getEndtime());
                                System.out.println("");
                                try {
                                    String format = "yyyy/MM/dd HH:mm";
                                    java.util.Date now = new DateTime();
                                    java.util.Date startTime = new SimpleDateFormat(format).parse(datecs.getStarttime());
                                    java.util.Date endTime = new SimpleDateFormat(format).parse(datecs.getEndtime());
                                    if (DateUtil.isIn(now, startTime, endTime)) {
                                        authMap.clear();
                                    }
                                } catch (java.text.ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                    );
                    if (!authMap.isEmpty()) {
                        authlist.add(authMap);
                    }
                }
        );
        return authlist;
    }

    private static Collection<TableStat.Column> getColumns(String sql) {
        // sql解析，返回Map<table.field>
        Collection<TableStat.Column> tablefieldList = SqlParseTool.execute(JdbcConstants.POSTGRESQL, sql);
        System.out.println("------------------------------------------------");
        System.out.println("sql解析出来的表名.字段名：");
        tablefieldList.stream().forEach(tablefield -> {
            System.out.printf("%s.%s", tablefield.getTable(), tablefield.getName());
            System.out.println("");
        });
        return tablefieldList;
    }

    protected PreparedStatementWrapper(PreparedStatement delegate, PreparedStatementInformation preparedStatementInformation, JdbcEventListener eventListener) {
        super(delegate, preparedStatementInformation, eventListener);
        this.delegate = delegate;
        statementInformation = preparedStatementInformation;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        SQLException e = null;
        long start = System.nanoTime();
        ResultSet var4;
        try {
            eventListener.onBeforeExecuteQuery(statementInformation);
            // return ResultSetWrapper.wrap(delegate.executeQuery(), new ResultSetInformation(statementInformation), eventListener);
            var4 = ResultSetWrapper.wrap(this.delegate.executeQuery(), new ResultSetInformation(this.statementInformation), this.eventListener);
            System.out.println("-----------------------------------------");
            System.out.println("拦截到数据：");
            // 判断数据脱敏，需要拿sql，解析出来table.field, 数据替换
            while (var4.next()) {
//                String value = var4.getString("table_name");
//                // String new_value = PubFunction.dePass(value);
//                String new_value = "****";
//                var4.updateString("table_name", new_value);
            }
            System.out.println("-----------------------------------------");
            System.out.println("脱敏后数据：");
            // ResultSetPrinter.printResultSet(var4);
            return var4;
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterExecuteQuery(statementInformation, System.nanoTime() - start, e);
        }
    }

    @Override
    public int executeUpdate() throws SQLException {
        SQLException e = null;
        long start = System.nanoTime();
        int rowCount = 0;
        try {
            eventListener.onBeforeExecuteUpdate(statementInformation);
            rowCount = delegate.executeUpdate();
            return rowCount;
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterExecuteUpdate(statementInformation, System.nanoTime() - start, rowCount, e);
        }
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        SQLException e = null;
        try {
            delegate.setNull(parameterIndex, sqlType);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, null, e);
        }
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setBoolean(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setByte(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setShort(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setInt(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setLong(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setFloat(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setDouble(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setBigDecimal(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setString(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setBytes(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setDate(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setTime(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setTimestamp(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        SQLException e = null;
        try {
            delegate.setAsciiStream(parameterIndex, x, length);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        SQLException e = null;
        try {
            delegate.setUnicodeStream(parameterIndex, x, length);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        SQLException e = null;
        try {
            delegate.setBinaryStream(parameterIndex, x, length);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void clearParameters() throws SQLException {
        delegate.clearParameters();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        SQLException e = null;
        try {
            delegate.setObject(parameterIndex, x, targetSqlType);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setObject(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public boolean execute() throws SQLException {
        SQLException e = null;
        long start = System.nanoTime();
        try {
            eventListener.onBeforeExecute(statementInformation);
            return delegate.execute();
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterExecute(statementInformation, System.nanoTime() - start, e);
        }
    }

    @Override
    public void addBatch() throws SQLException {
        SQLException e = null;
        long start = System.nanoTime();
        try {
            eventListener.onBeforeAddBatch(statementInformation);
            delegate.addBatch();
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterAddBatch(statementInformation, System.nanoTime() - start, e);
        }
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        SQLException e = null;
        try {
            delegate.setCharacterStream(parameterIndex, reader, length);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, reader, e);
        }
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setRef(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setBlob(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setClob(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setArray(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        SQLException e = null;
        try {
            delegate.setDate(parameterIndex, x, cal);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        SQLException e = null;
        try {
            delegate.setTime(parameterIndex, x, cal);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        SQLException e = null;
        try {
            delegate.setTimestamp(parameterIndex, x, cal);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        SQLException e = null;
        try {
            delegate.setNull(parameterIndex, sqlType, typeName);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, null, e);
        }
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setURL(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setRowId(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        SQLException e = null;
        try {
            delegate.setNString(parameterIndex, value);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, value, e);
        }
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        SQLException e = null;
        try {
            delegate.setNCharacterStream(parameterIndex, value, length);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, value, e);
        }
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        SQLException e = null;
        try {
            delegate.setNClob(parameterIndex, value);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, value, e);
        }
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        SQLException e = null;
        try {
            delegate.setClob(parameterIndex, reader, length);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, reader, e);
        }
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        SQLException e = null;
        try {
            delegate.setBlob(parameterIndex, inputStream, length);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, inputStream, e);
        }
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        SQLException e = null;
        try {
            delegate.setNClob(parameterIndex, reader, length);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, reader, e);
        }
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        SQLException e = null;
        try {
            delegate.setSQLXML(parameterIndex, xmlObject);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, xmlObject, e);
        }
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        SQLException e = null;
        try {
            delegate.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        SQLException e = null;
        try {
            delegate.setAsciiStream(parameterIndex, x, length);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        SQLException e = null;
        try {
            delegate.setBinaryStream(parameterIndex, x, length);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        SQLException e = null;
        try {
            delegate.setCharacterStream(parameterIndex, reader, length);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, reader, e);
        }
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setAsciiStream(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setBinaryStream(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        SQLException e = null;
        try {
            delegate.setCharacterStream(parameterIndex, reader);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, reader, e);
        }
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        SQLException e = null;
        try {
            delegate.setNCharacterStream(parameterIndex, value);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, value, e);
        }
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        SQLException e = null;
        try {
            delegate.setClob(parameterIndex, reader);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, reader, e);
        }
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        SQLException e = null;
        try {
            delegate.setBlob(parameterIndex, inputStream);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, inputStream, e);
        }
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        SQLException e = null;
        try {
            delegate.setNClob(parameterIndex, reader);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, reader, e);
        }
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return delegate.getParameterMetaData();
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return delegate.getMetaData();
    }

}
