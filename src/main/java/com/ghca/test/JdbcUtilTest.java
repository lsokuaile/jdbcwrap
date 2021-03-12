package com.ghca.test;


import com.ghca.utils.JdbcTool;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class JdbcUtilTest {
    @Test
    public void test1(){
        try {
            Connection connection = JdbcTool.getConnection();
            List<String> resList = JdbcTool.testSQL(connection, "ghca2");
            resList.stream().forEach(item -> {
                System.out.println(item);
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
