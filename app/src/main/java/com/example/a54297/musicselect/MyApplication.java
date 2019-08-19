package com.example.a54297.musicselect;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.example.a54297.musicselect.Help.RealmHelp;

import io.realm.Realm;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        Realm.init(this);

        RealmHelp.migration();
    }
}
