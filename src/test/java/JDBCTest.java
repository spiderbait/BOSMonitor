import cn.bos.monitor.util.ResultSet2JSON;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.Iterator;

public class JDBCTest {

    public static Connection getConnection(String driver, String username, String password, String url) {
        Connection conn = null;

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public static ResultSet getResultSet(Connection conn, String sql) {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    public static void main(String[] args) throws SQLException {
        String driver = "com.mysql.cj.jdbc.Driver";
        String username = "root";
        String password = "ltz5623459";
        String url = "jdbc:mysql://localhost:3306/MonitorControl?useOldAliasMetadataBehavior=true";
        String sql = "select name as mresult, '如下作业有问题：' as mprompt from rule where enable=0";
        String sql1 = "select * from list";

        Connection conn1 = JDBCTest.getConnection(driver, username, password, url);
//        ResultSet rs1 = JDBCTest.getResultSet(conn1, sql);
        Statement stmt = conn1.createStatement();
        ResultSet rs1 = stmt.executeQuery(sql);



        JSONArray jsonArray = ResultSet2JSON.convert(rs1);
        ResultSet rs2 = stmt.executeQuery(sql1);
        JSONArray jsonArray1 = ResultSet2JSON.convert(rs2);
//        System.out.println(jsonArray.toString());
//        stmt.close();
        System.out.println("JSONArray.length(): " + jsonArray.length());
        for (int i=0; i<jsonArray.length(); i++) {
            System.out.println(jsonArray.get(i).toString());
        }
        for (int i=0; i<jsonArray1.length(); i++) {
            System.out.println(jsonArray1.get(i).toString());
        }
    }
}
