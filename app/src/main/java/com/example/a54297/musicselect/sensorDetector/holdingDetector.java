package com.example.a54297.musicselect.sensorDetector;
import  android.util.Log;
public class holdingDetector {
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

    }

    public int holdingStyle() {
        timeofoldValue = timeofnewValue;
        timeofnewValue = System.currentTimeMillis();
        angleOld = angleNew;
        angleNew = Orien[1];
        float rate;
        if((timeofnewValue - timeofoldValue)!=0)
        {
            rate = (angleNew - angleOld)/(timeofnewValue - timeofoldValue);
        }else{
            rate = 0;
        }

        System.out.println("====proximity"+Proximity);
        System.out.println("====light"+Light);
        System.out.println("====rate"+rate+(float)(timeofnewValue - timeofoldValue));
        Log.i("=====","===rate===   newValue"+rate+"  period:");
        if (Proximity < 1 && Light < 5) {
            style = 0;//手机放在口袋
        } else if (Orien[1] > -70.0 && Orien[1] < -10.0 && (Orien[2] > 150 || Orien[2] < -150)) {
            style = 1;//躺在床上，手机在身前
        } else if((( Orien[1] < 2.0 && Orien[1] > -2.0) || (Orien[1] >178.0 && Orien[1] > -178))
                &&(( Orien[2] < 2.0 && Orien[2] > -2.0) || (Orien[2] >178.0 && Orien[2] > -178))){
            style = 3;//放在桌子或者平面上
        }else if(Orien[1] < 10.0 && Orien[1] > -60.0 && Orien[2] < 25.0 && Orien[2] > -25.0){
            style = 2;//手机握在手上,身前把玩
        }else if(Orien[1] > -60 &&Orien[1] < 60 && (Orien[2] < -40 || Orien[2] > 40)){
            style =  5;//手机放在身侧，或者侧放
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
