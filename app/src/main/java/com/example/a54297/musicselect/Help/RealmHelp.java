package com.example.a54297.musicselect.Help;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.example.a54297.musicselect.activitys.AlbumListActivity;
import com.example.a54297.musicselect.migration.Migration;
import com.example.a54297.musicselect.models.AlbumModel;
import com.example.a54297.musicselect.models.MusicModel;
import com.example.a54297.musicselect.models.MusicSourceModel;
import com.example.a54297.musicselect.models.UserModel;
import com.example.a54297.musicselect.utils.DataUtils;

import java.io.FileNotFoundException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class RealmHelp {

    private Realm mRealm;

    public RealmHelp(){
        mRealm = Realm.getDefaultInstance();
    }

    public static void migration(){
        RealmConfiguration conf = getRealmConf();

        Realm.setDefaultConfiguration(conf);

        try {
            Realm.migrateRealm(conf);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static RealmConfiguration getRealmConf(){
        return new RealmConfiguration.Builder()
                .schemaVersion(1)
                .migration(new Migration())
                .build();
    }

    //关闭数据库
    public void close(){
        if(mRealm != null && !mRealm.isClosed()){
            mRealm.close();
        }
    }
    /**
     * 保存用户信息
     */
    public void saveUser (UserModel userModel){
        mRealm.beginTransaction();
        mRealm.insert(userModel);
//        mRealm.insertOrUpdate(userModel);
        mRealm.commitTransaction();
    }

    //返回所有用户
    public List<UserModel> getAllUser(){
        RealmQuery<UserModel> query = mRealm.where(UserModel.class);
        RealmResults<UserModel> results = query.findAll();
        return results;
    }

    public boolean validateUser(String phone,String password) {
        boolean result = false;
        RealmQuery<UserModel> query = mRealm.where((UserModel.class));
        query = query.equalTo("phone", phone)
                .equalTo("password", password);
        UserModel userModel = query.findFirst();
        if (userModel != null) {
            result = true;
        }
        return result;
    }

    public UserModel getUser(){
        RealmQuery<UserModel> query = mRealm .where(UserModel.class);
        UserModel userModel = query.equalTo("phone",UserHelp.getInstance().getPhone()).findFirst();
        return userModel;
    }

    /**
     * 修改密码
     */

    public void changePassword(String password){
        UserModel userModel = getUser();
        mRealm.beginTransaction();
        userModel.setPassword(password);
        mRealm.commitTransaction();
    }

    /**
     * 1、用户登录，存放数据
     * 2、用户退出，删除数据
     */


    /**
     * 保存音乐源数据
     */
    public void setMusicSource(Context context){
        //拿到资源文件中的数据
       String musicSourceJson = DataUtils.getJsonFromAssets(context,"DataSource.json");
       mRealm.beginTransaction();
       mRealm.createObjectFromJson(MusicSourceModel.class,musicSourceJson);
       mRealm.commitTransaction();
    }

    /**
     *
     * 删除音乐源数据
     */

    public void removeMusicSource(){

        mRealm.beginTransaction();
        mRealm.delete(MusicSourceModel.class);
        mRealm.delete(MusicModel.class);
        mRealm.delete(AlbumModel.class);
        mRealm.commitTransaction();
    }


    public MusicSourceModel getMusicSource(){
        return mRealm.where(MusicSourceModel.class).findFirst();
    }

    public AlbumModel getAlbum (String albumId){
        return mRealm.where(AlbumModel.class).equalTo("albumId",albumId).findFirst();
    }

    public MusicModel getMusic (String musicId){
        return mRealm.where(MusicModel.class).equalTo("musicId",musicId).findFirst();
    }

}
//验证用户信息




