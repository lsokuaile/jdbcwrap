package com.ghca.utils;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.stat.TableStat.Column;
import com.alibaba.druid.stat.TableStat.Name;
import com.alibaba.druid.util.JdbcConstants;

import java.util.*;


public class SqlUtis {

    public static void main(String[] args) {

        final String dbType1 = JdbcConstants.MYSQL; // 可以是ORACLE、POSTGRESQL、SQLSERVER、ODPS等
        extracted(dbType1);

    }

    public static Collection<Column> extracted(String dbType1) {
        String sql1 = "select * from student";
        List<SQLStatement> stmtList1 = SQLUtils.parseStatements(sql1, dbType1);


        // String sql = "Select * from Student;";  // fields : [Student.*]
        // String sql = " Select a.* from student a;"; // fields : [student.*]
        // String sql = " select StuId,StuName,Gender,Age,JoinDate,ClassId,Address from student;"; // fields : [student.StuId, student.StuName, student.Gender, student.Age, student.JoinDate, student.ClassId, student.Address]
        // String sql = "  select x.StuId,x.StuName, x.Gender,x.Age,x.JoinDate,x.ClassId,x.Address from student x;"; // fields : [student.StuId, student.StuName, student.Gender, student.Age, student.JoinDate, student.ClassId, student.Address]
        // String sql = " select student.StuId,student.StuName, student.Gender,student.Age,student.JoinDate,student.ClassId,student.Address from student;"; //fields : [student.StuId, student.StuName, student.Gender, student.Age, student.JoinDate, student.ClassId, student.Address]
        // String sql = " select student.StuId as studentID, student.StuName as studentname, student.Gender as gender, student.Age as age, student.JoinDate , student.ClassId, student.Address from student;"; //fields : [student.StuId, student.StuName, student.Gender, student.Age, student.JoinDate, student.ClassId, student.Address]
        // String sql = " select student.StuId studentID, student.StuName studentname, student.Gender gender, student.Age as age, student.JoinDate , student.ClassId, student.Address from student;"; //fields : [student.StuId, student.StuName, student.Gender, student.Age, student.JoinDate, student.ClassId, student.Address]
        String sql = " select substr(a.a1) a2,b.b1 from (select a1 from tab1) a ,(select b1 from tab2) b where a.a1=b.b1(+)"; //fields : [student.StuId, student.StuName, student.Gender, student.Age, student.JoinDate, student.ClassId, student.Address]
//        String sql = ""
//                + "insert into tar select * from boss_table bo, ("
//                + "select a.f1, ff from emp_table a "
//                + "inner join log_table b "
//                + "on a.f2 = b.f3"
//                + ") f "
//                + "where bo.f4 = f.f5 "
//                + "group by bo.f6 , f.f7 having count(bo.f8) > 0 "
//                + "order by bo.f9, f.f10;"
//                + "select substr(f,2) from test1; "
//                + "";
        // String dbType = JdbcConstants.POSTGRESQL;
        String dbType = JdbcConstants.ORACLE;

        //格式化输出
        String result = SQLUtils.format(sql, dbType);
        System.out.println(result); // 缺省大写格式
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);

        //解析出的独立语句的个数
        System.out.println("size is:" + stmtList.size());
        for (int i = 0; i < stmtList.size(); i++) {
            SQLStatement stmt = stmtList.get(i);
            // PGSchemaStatVisitor visitor = new PGSchemaStatVisitor();
            OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
            stmt.accept(visitor);
            Map<String, String> aliasmap = visitor.getAliasMap();
            for (Iterator iterator = aliasmap.keySet().iterator(); iterator.hasNext(); ) {
                String key = iterator.next().toString();
                System.out.println("[ALIAS]" + key + " - " + aliasmap.get(key));

            }
            Set<Column> groupby_col = visitor.getGroupByColumns();
            for (Iterator iterator = groupby_col.iterator(); iterator.hasNext(); ) {
                Column column = (Column) iterator.next();
                System.out.println("[GROUP]" + column.toString());
            }

            //获取表名称
            System.out.println("table names:");
            Map<Name, TableStat> tabmap = visitor.getTables();
            for (Iterator iterator = tabmap.keySet().iterator(); iterator.hasNext(); ) {
                Name name = (Name) iterator.next();
                System.out.println(name.toString() + " - " + tabmap.get(name).toString());
            }

            System.out.println("Tables : " + visitor.getCurrentTable());            //获取操作方法名称,依赖于表名称
            System.out.println("Manipulation : " + visitor.getTables());    //获取字段名称
            System.out.println("fields : " + visitor.getColumns());
            return visitor.getColumns();
        }
        return null;
    }

}
