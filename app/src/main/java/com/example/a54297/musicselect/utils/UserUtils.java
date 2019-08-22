package com.example.a54297.musicselect.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.example.a54297.musicselect.Help.LoginHelp;
import com.example.a54297.musicselect.Help.RealmHelp;
import com.example.a54297.musicselect.Help.UserHelp;
import com.example.a54297.musicselect.R;
import com.example.a54297.musicselect.activitys.LoginActivity;
import com.example.a54297.musicselect.constants.SPConstants;
import com.example.a54297.musicselect.models.UserModel;

import java.util.List;

import io.realm.RealmObject;

public class UserUtils {

//验证输入合法性
    public static boolean validateLogin(Context context,String phone,String password){
        //简单方法
//        RegexUtils.isMobileSimple(phone);
        //复杂方法
        if(!RegexUtils.isMobileExact(phone)){
            Toast.makeText(context,"无效手机号",Toast.LENGTH_SHORT).show();
                    return false;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(context,"请输入密码",Toast.LENGTH_SHORT).show();
            return false;
        }

        /**
         * 1、用户当前手机是否被注册
         * 2、用户输入的手机号和密码是否匹配
         */

        if(!UserUtils.userExistFromPhone(phone)){
            Toast.makeText(context,"当前手机号未注册",Toast.LENGTH_SHORT).show();
            return false;
        }


        RealmHelp realmHelp = new RealmHelp();
        boolean result = realmHelp.validateUser(phone,EncryptUtils.encryptMD5ToString(password));

        if(!result){
            Toast.makeText(context,"手机号或密码不正确",Toast.LENGTH_SHORT).show();
            return false;
        }
// 保存登录标记

        boolean isSave = SPUtils.saveUser(context,phone);
        if(!isSave){

            return false;
        }
        //保存用户信息
        UserHelp.getInstance().setPhone(phone);

        //保存音乐源数据
        realmHelp.setMusicSource(context);

        realmHelp.close();

        return true;
    }

    public static void logout(Context context){

        //删除sp保存的用户标记

        boolean isRemove = SPUtils.removeUser(context);
        if(!isRemove){
            Toast.makeText(context,"系统错误，请稍后重试",Toast.LENGTH_SHORT).show();
            return;
        }

        RealmHelp realmHelp = new RealmHelp();
        realmHelp.removeMusicSource();
        realmHelp.close();

        Intent intent = new Intent(context,LoginActivity.class);
        //添加标志符，清理TASK栈，新生成一个栈
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        //定义Activity跳转动画
        ((Activity)context).overridePendingTransition(R.anim.open_enter,R.anim.open_exit);
    }

    /**
     * 注册用户
     * @param context
     * @param phone
     * @param password
     * @param passwordConfirm
     */
    public static boolean registerUser (Context context,String phone,String password,String passwordConfirm){
        if(!RegexUtils.isMobileExact(phone)){
            Toast.makeText(context,"无效手机号",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(StringUtils.isEmpty(password)||!password.equals(passwordConfirm)){
            Toast.makeText(context,"请确认密码",Toast.LENGTH_SHORT).show();
            return false;
        }

        //用户当前手机号是否已经被注册
        /**
         * 1、通过realm获取到当前已经注册的所有用户
         * 2、根据用户输入的手机号匹配查询的所有用户，如果可以匹配则证明该手机号已经被注册。否则就表示还未注册
         */
        if(UserUtils.userExistFromPhone(phone)){
            Toast.makeText(context,"该手机号已存在",Toast.LENGTH_SHORT).show();
            return false;
        }



        UserModel userModel = new UserModel();
        userModel.setPhone(phone);
        userModel.setPassword(EncryptUtils.encryptMD5ToString(password));

        saveUser(userModel);

        /**
         * 测试MySql
         */
        LoginHelp loginHelp = new LoginHelp(phone,password);
        loginHelp.start();

        return true;
    }

    /**
     * 保存用户到数据库
     * @param userModel
     */
    public static  void saveUser(UserModel userModel){
        RealmHelp realmHelp = new RealmHelp();
        realmHelp.saveUser(userModel);
        realmHelp.close();
    }

    /**
     * 根据手机号判断用户是否存在
     */
    public static boolean userExistFromPhone(String phone){
        boolean result = false;

        RealmHelp realmHelp = new RealmHelp();
        List<UserModel> alluser = realmHelp.getAllUser();

        for(UserModel userModel:alluser){
            if(userModel.getPhone().equals(phone)){
                //当前手机号已经存在于数据库中
                result = true;
                break;
            }
        }

        realmHelp.close();

        return  result;
    }

    /**
     * 验证是否存在已登录用户
     */
    public static boolean validateUserLogin(Context context){
       return SPUtils.isLoginUser(context);
    }

    /**
     * 修改密码
     * 1、验证
     *      原密码是否输入
     *      新密码是否输入，新密码与确定密码是否相同
     *      原密码输入是否正确
     *          获取当前用户模型
     *          用保存的密码与输入密码匹配
     * 2、利用 Realm模型自动更新特性完成密码修改
     */

    public static boolean changePassword(Context context,String oldpassword,String password,String passwordConfirm){

        if (TextUtils.isEmpty(oldpassword)) {
            Toast.makeText(context,"请输入原密码",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password)|| !password.equals(passwordConfirm)) {
            Toast.makeText(context,"请确认密码",Toast.LENGTH_SHORT).show();
            return false;
        }
    //验证原密码是否正确
        RealmHelp realmHelp = new RealmHelp();
        UserModel userModel = realmHelp.getUser();
        if(!EncryptUtils.encryptMD5ToString(oldpassword).equals(userModel.getPassword())){
            Toast.makeText(context,"原密码不正确",Toast.LENGTH_SHORT).show();
            return false;
        }

        realmHelp.changePassword(EncryptUtils.encryptMD5ToString(password));
        realmHelp.close();

        return true;
    }
}
