package com.example.a54297.musicselect.Help;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DatabaseHelp extends Thread{
    private String userPhone;
    private String userPassword;

    private static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://47.98.168.203:3306/User";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "AAAaaa111111!";

    public DatabaseHelp(String userPhone, String userPassword) {
        this.userPhone = userPhone;
        this.userPassword = userPassword;
    }

    public static Connection getConn(){
        Connection connection = null;
        try {
            Class.forName(DATABASE_DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 关闭数据库
     * @param connection 连接
     * @param ps 数据库PreparedStatement
     */
    public static void closeAll(Connection connection, PreparedStatement ps){
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void run() {
        Connection connection=null;
        try{
            Class.forName(DATABASE_DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            String sql = "INSERT INTO Account(phone,password) VALUES (?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, userPhone);
            ps.setString(2,userPassword);
            ps.executeUpdate();
            if (ps!=null) {
                ps.close();
            }
            if (connection!=null) {
                connection.close();
            }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}