package com.example.a54297.musicselect.Help;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class LoginHelp extends Thread{

    private String userPhone;
    private String userPassword;
    private static final String DBDRIVER = "com.mysql.jdbc.Driver";
    private static final String DBURL = "jdbc:mysql://47.98.168.203:3306/User";
    private static final String DBUSER = "root";
    private static final String DBPASSWORD = "AAAaaa111111!";

    public LoginHelp(String userPhone, String userPassword) {
        this.userPhone = userPhone;
        this.userPassword = userPassword;
    }

    @Override
    public void run() {
        Connection connection=null;
        try{
            Class.forName(DBDRIVER);
            connection = DriverManager.getConnection(DBURL,DBUSER,DBPASSWORD);
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