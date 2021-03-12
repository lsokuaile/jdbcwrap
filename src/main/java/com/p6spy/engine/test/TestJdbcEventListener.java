/**
 * P6Spy
 *
 * Copyright (C) 2002 - 2020 P6Spy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.p6spy.engine.test;

import com.p6spy.engine.common.ResultSetInformation;
import com.p6spy.engine.common.StatementInformation;
import com.p6spy.engine.event.JdbcEventListener;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * A No-Op implementation just used for testing purposes
 */
public class TestJdbcEventListener extends JdbcEventListener {
  private ResultSetMetaData metaData;

  @Override
  public void onBeforeAddBatch(StatementInformation statementInformation, String sql) {
    System.out.println("拦截前：+++"+sql);
    sql = "select * from t_user a";
  }
  @Override
  public void onBeforeExecute(StatementInformation statementInformation, String sql) {
    System.out.println("拦截前：+++"+sql);
    sql="select * from dual";
  }
  public void onBeforeExecuteQuery(StatementInformation statementInformation, String sql) {
    System.out.println("拦截前：+++"+sql);
    sql = "select * from t_user a";
  }
@Override
  public void onBeforeResultSetNext(ResultSetInformation resultSetInformation) {
    System.out.println("数据拦截：+++");
//    ResultSet rs = resultSetInformation.getResultSet();
////    if(rs instanceof JDBC42ResultSet){
////      Field[] fields = ((JDBC42ResultSet) rs).fields;
////
////    }
//
//    Class aClass = rs.getClass();
//    while (aClass!=null){
//      Field fields =  null;
//      try {
//        fields = aClass.getDeclaredField("fields");
//      } catch (Exception e) {
//        aClass =aClass.getSuperclass();
//        continue;
//      }
//      if(fields != null){
//        fields.setAccessible(true);
////        rs.fields;
//      }
//
//
//    }
//    declaredField.getGenericType();


//    ResultSetMetaData metaData = rs.getMetaData().fields;


//  try {
//    rs.absolute(1);
//    rs.updateInt(1,5);
//    rs.updateRow();
//    while(rs.next()){

//      rs.updateString(2,"王五");
//      rs.updateRow();
//      rs.relative(-2);
//      rs.first();
//    }


//  } catch (SQLException e) {
//    e.printStackTrace();
//  }
}
  public void onBeforeGetResltSetDate(ResultSet resultSet){
    System.out.println("数据拦截修改数据");
    try {
      ResultSetMetaData metaData = resultSet.getMetaData();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Object onAfterResultSetGet(ResultSetInformation resultSetInformation, int columnIndex, Object value, SQLException e) throws SQLException {
    System.out.println("获取数据");
    resultSetInformation.getResultSet().getMetaData().getSchemaName(columnIndex);
    String tableName = resultSetInformation.getResultSet().getMetaData().getTableName(columnIndex);
    String columnLabel = resultSetInformation.getResultSet().getMetaData().getColumnLabel(columnIndex);
    System.out.println("表名："+tableName+"字段名为："+columnLabel);
    if(value instanceof Integer){
      value = 10;
    }
    return value;
  }

}
