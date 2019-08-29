package com.example.a54297.musicselect.Help;

/**
 * 1、用户登录
 *      1、当用户登录时，利用sharedpreferences 保存登录用户的用户标记
 *      2、利用全局单例类UserHelp保存用户登陆信息
 *          1、用户登录后保存信息
 *          2、用户重新打开应用程序时，检测sharepreferences有无保存的标记，如果存在，则为Userhelper进行赋值，并且进入主页，如果不存在，则进入登录页面
 * 2、用户退出
 *      1、删除标记，退出到登录页面
 */
public class UserHelp {

  private static UserHelp instance;

  private UserHelp(){};

  public  static UserHelp getInstance(){
    if(instance == null){
      synchronized (UserHelp.class){
        if(instance == null){
          instance = new UserHelp();
        }
      }
    }
    return instance;
  }

  private  String phone;

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }
}