package idusw.javaweb.blogapi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static ConnectionManager instance = new ConnectionManager();
    private ConnectionManager() {}
    public static ConnectionManager getInstance() {return instance;}

    public Connection getConnection() { //db 연결
        Connection conn = null;

        String dbName = "db_b202012055";
        String jdbcUrl = "jdbc:mysql://localhost:3306/"+dbName+"?characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false";
        //사용할 db
        String dbUser = "u_b202012055";
        String dbPass = "cometrue";
        try {
            //mysql-connector-j-8.0.33.jar안에 Driver가 존재
            //Driver, Connector (산출물 - DBMS 중재)를 메모리에 적재
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try { //db연결후 제대로 쿼리가 작동하여 가져오는지
            conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass);
        } catch (SQLException e) {
            //아래 오류 문구 출력 시 JDBCurl, ID, PW 확인
            System.out.println("Connection Fail - ");
        } finally {
            return conn;
        }
    }
}
