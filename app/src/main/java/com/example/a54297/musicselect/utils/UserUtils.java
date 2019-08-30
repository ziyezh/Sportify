package com.example.a54297.musicselect.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.example.a54297.musicselect.Help.RealmHelp;
import com.example.a54297.musicselect.R;
import com.example.a54297.musicselect.activities.LoginActivity;
import com.example.a54297.musicselect.models.UserModel;

import java.util.List;

public class UserUtils {

    public static boolean validateLogin(Context context,String phone,String password){
        if(!RegexUtils.isMobileExact(phone)){
            Toast.makeText(context,"无效手机号",Toast.LENGTH_SHORT).show();
                    return false;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(context,"请输入密码",Toast.LENGTH_SHORT).show();
            return false;
        }
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
     * @param context 提示
     * @param phone 手机号
     * @param password 密码
     * @param passwordConfirm 确认密码
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

        return true;
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
//    //验证原密码是否正确
//        RealmHelp realmHelp = new RealmHelp();
//        UserModel userModel = realmHelp.getUser();
//        if(!EncryptUtils.encryptMD5ToString(oldpassword).equals(userModel.getPassword())){
//            Toast.makeText(context,"原密码不正确",Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        realmHelp.changePassword(EncryptUtils.encryptMD5ToString(password));
//        realmHelp.close();

        return true;
    }
}
