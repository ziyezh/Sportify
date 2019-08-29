package com.example.a54297.musicselect.services;

import com.example.a54297.musicselect.Help.DatabaseHelp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseService extends Thread{
    /**
     * behavior为1，表示注册
     * behavior为2，表示登陆
     * behavior为3，表示修改密码
     */
    private int behavior;
    /**
     * 数据库操作的结果；
     * 1 为成功
     * 0 为失败
     */
    private int result = 0;
    private String userPhone;
    private String userPassword=null;
    private Connection conn=null;
    private PreparedStatement ps=null;
    public static DatabaseService dbService=null;

    public String getUserPassword() {
        return userPassword;
    }

    public int getResult() {
        return result;
    }

    public DatabaseService(int behavior, String userPhone, String userPassword) {
        this.behavior = behavior;
        this.userPhone = userPhone;
        this.userPassword = userPassword;
    }
    public DatabaseService(int behavior, String userPhone) {
        this.behavior = behavior;
        this.userPhone = userPhone;
    }

    @Override
    public void run() {
        switch(behavior){
            case 1:
                register();
                break;
            case 2:
                login();
                break;
            case 3:
                changePassword();
                break;
            default:
                break;
        }
    }

    private void register(){
        try{
            conn= DatabaseHelp.getConn();
            String sql = "INSERT INTO Account(phone,password) VALUES (?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, userPhone);
            ps.setString(2,userPassword);
            result = ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseHelp.closeAll(conn,ps);
    }

    private void login(){
        try{
            conn= DatabaseHelp.getConn();
            String sql = "select * from Account where phone = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, userPhone);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                userPassword = resultSet.getString("password");
                result = 1;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseHelp.closeAll(conn,ps);
    }

    private void changePassword(){

    }
}
