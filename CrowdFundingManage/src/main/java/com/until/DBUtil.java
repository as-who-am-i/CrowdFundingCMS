package com.until;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class DBUtil {
    private static ComboPooledDataSource cpds = null;

    static {
        cpds = new ComboPooledDataSource("mysql");
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            //获取连接
            connection = cpds.getConnection();
        } catch (SQLException e) {
            log.error("error:" + e);
        }
        return connection;
    }

    //关闭连接
    public static void close(Connection connection, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps!=null){
                ps.close();
            }
            if (connection!=null){
                connection.close();
            }
        } catch (SQLException e) {
            log.error("error:" + e);
        }
    }
}
