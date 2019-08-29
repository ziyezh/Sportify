package com.example.a54297.musicselect.sensorDetector;

import  android.util.Log;
//import android.widget.Toast;



public class holdingDetector {
    //输入需要该信息的界面

    long timeofoldValue = 0;
    long timeofnewValue = 0;
    float angleOld = 0;
    float angleNew = 0;
    private float[] Orien = new float[3];
    private float Proximity;
    private float Light;
    private int style;

    public holdingDetector(float[] o,float p,float l){
        Orien = o;
        Proximity = p;
        Light = l;
        Log.i("====","===Proximity==="+Proximity+"===Light"+Light);

    }

    public int holdingStyle() {
        timeofoldValue = timeofnewValue;
        timeofnewValue = System.currentTimeMillis();
        angleOld = angleNew;
        angleNew = Orien[0];
        float rate;
        if(timeofoldValue != 0 && angleNew != 0)
        {
            rate = Math.abs((angleNew - angleOld)/(timeofnewValue - timeofoldValue));
        }else{
            rate = 0;
        }

        if (Proximity < 0.5 && Light < 5.0) {
            style = 0;//手机放在口袋
        } else if (Orien[1] > -70.0 && Orien[1] < -10.0 && (Orien[2] > 150 || Orien[2] < -150)) {
            style = 1;//躺在床上，手机在身前
        } else if((( Orien[1] < 2.0 && Orien[1] > -2.0) || (Orien[1] >178.0 && Orien[2] < -178))
                &&(( Orien[2] < 2.0 && Orien[2] > -2.0) || (Orien[2] >178.0 && Orien[2] < -178))){
            style = 3;//放在桌子或者平面上
        }else if(Orien[1] < 10.0 && Orien[1] > -60.0 && Orien[2] < 25.0 && Orien[2] > -25.0){
            style = 2;//手机握在手上,身前把玩
        }else if(Orien[1] > -60 && Orien[1] < 60 && (Orien[2] > 40 || Orien[2] < -40)){
            style = 5;//手机放在身侧或者侧放
        }
        return style;
    }

    public void setOrien(float[] values)
    {
        Orien = values;
    }
    public void setProximity(float value)
    {
        Proximity = value;
    }
    public void setLight(float value){
        Light = value;
    }

}
