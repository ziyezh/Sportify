package com.example.a54297.musicselect.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataUtils {

    public static String getJsonFromAssets(Context context, String fileName){

        /**
         * 1、StringBuilder 存放读取出的数据
         * 2、AssetManager资源管理器 Open方法打开指定的资源文件 返回InputStream
         * 3、InputStringReader（）字节到字符的调节器，BufferReader（存放读取字符的缓冲区）
         * 4、循环利用BufferReader的readline 方法 读取每一行的数据，并且把读取的数据，放到StringBuilder里面
         * 5、返回读取出来的所有数据
         */

        StringBuilder stringBuilder = new StringBuilder();
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine())!= null){
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();

    }

}
