package com.example.a54297.musicselect.sensorDetector;

import android.content.Context;
import  android.util.Log;
//返回用户的运动状态
//0静止，1慢走，2快走，3慢跑，4快跑，5加载，6摇晃
public class movingDetector {
    //存放三轴数据
    float[] oriValues = new float[3];
    final int ValueNum = 4;
    //用于存放计算阈值的波峰波谷差值
    float[] tempValue = new float[ValueNum];
    int tempCount = 0;
    //是否上升的标志位
    boolean isDirectionUp = false;
    //持续上升次数
    int continueUpCount = 0;
    int continueDownCount = 0;
    //上一点的持续上升的次数，为了记录波峰的上升次数
    int continueUpFormerCount = 0;
    int continueDownFormerCount = 0;
    //上一点的状态，上升还是下降
    boolean lastStatus = false;
    //波峰值
    float peakOfWave = 0;
    //波谷值
    float valleyOfWave = 0;
    //此次波峰的时间
    long timeOfThisPeak = 0;
    //上次波峰的时间
    long timeOfLastPeak = 0;
    //当前的时间
    long timeOfNow = 0;
    //当前传感器的值
    float gravityNew = 0;
    //上次传感器的值
    float gravityOld = 0;
    //动态阈值需要动态的数据，这个值用于这些动态数据的阈值
    final float InitialValue = (float) 1.3;
    //初始阈值
    float ThreadValue = (float) 2.0;
    //波峰波谷时间差
    int TimeInterval = 250;
    //设置状态
    int result = 0;


    private int stepCount = 0;
    private long timeOfLastStep = 0;
    private long timeOfThisStep = 0;
    private long averageTimeOfEveryStep = 0;
    //输入需要该信息的界面
    private Context c;
    private int holdingStyles;
    public movingDetector(Context c){
        this.c= c;
        holdingStyles = 0;
    }



    //数据的输入口
    public int inputValue(float value, int gesture){
        detectorNewStep(value);
        holdingStyles = gesture;
        return result;
    }

    /*
     * 检测步子，并开始计步
     * 1.传入sersor中的数据
     * 2.如果检测到了波峰，并且符合时间差以及阈值的条件，则判定为1步
     * 3.符合时间差条件，波峰波谷差值大于initialValue，则将该差值纳入阈值的计算中
     * */
    public void detectorNewStep(float values) {
            if(gravityOld == 0) {
                gravityOld = values;
            }else {

                boolean check = detectorPeak(values, gravityOld);
                if (check) {
                    timeOfLastPeak = timeOfThisPeak;
                    timeOfNow = System.currentTimeMillis();
                    if (timeOfNow - timeOfLastPeak >= TimeInterval
                            && (peakOfWave - valleyOfWave >= ThreadValue)) {
                        timeOfThisPeak = timeOfNow;
                    /*
                     走路或者跑步的识别
                     * */
                        result = countStep();
                        System.out.println("===result==="+result+"===averageTimeOfEveryStep==="+averageTimeOfEveryStep);
                    }

                    if (timeOfNow - timeOfLastPeak >= TimeInterval
                            && (peakOfWave - valleyOfWave >= InitialValue)) {
                        timeOfThisPeak = timeOfNow;
                        ThreadValue = peakValleyThread(peakOfWave - valleyOfWave);
                    }
                }

            }
            gravityOld = values;
    }


    private int countStep() {
        timeOfLastStep = timeOfThisStep;
        timeOfThisStep = System.currentTimeMillis();
        long diffValue = timeOfThisStep - timeOfLastStep;
        if (diffValue <= 3000) {
            averageTimeOfEveryStep += diffValue;
            if (stepCount < 9){
                stepCount++;
                return 5;
            }else if(stepCount == 9) {
                averageTimeOfEveryStep = averageTimeOfEveryStep / 10;//计算运动速度
                Log.i("result", "averageTimeOfEveryStep: " + averageTimeOfEveryStep);
                //确定运动状态
                //确定手机握持状态
                switch (holdingStyles){
                    //手机在裤子口袋中
                    case 0:{
                        if (averageTimeOfEveryStep < 10) {
                            Log.i("result", "快跑");
                            return 4;
                        } else if (averageTimeOfEveryStep >= 10 && averageTimeOfEveryStep < 50) {
                            Log.i("result", "慢跑");
                            return 3;
                        } else if (averageTimeOfEveryStep >= 50 && averageTimeOfEveryStep < 100) {
                            Log.i("result", "快走");
                            return 2;
                        } else if (averageTimeOfEveryStep >= 100 && averageTimeOfEveryStep < 500) {
                            Log.i("result", "慢走");
                            return 1;
                        } else {
                            Log.i("result", "静止");
                            return 0;
                        }
                    }
                    //手机在身体正面握持状态下
                    case 2: {
                        if (averageTimeOfEveryStep < 50) {
                            Log.i("result", "快跑");
                            return 4;
                        } else if (averageTimeOfEveryStep >= 50 && averageTimeOfEveryStep < 200) {
                            Log.i("result", "慢跑");
                            return 3;
                        } else if (averageTimeOfEveryStep >= 200 && averageTimeOfEveryStep < 500) {
                            Log.i("result", "快走");
                            return 2;
                        } else if (averageTimeOfEveryStep >= 500 && averageTimeOfEveryStep < 20000) {
                            Log.i("result", "慢走");
                            return 1;
                        } else {
                            Log.i("result", "静止");
                            return 0;
                        }
                    }
                    //手机拿手上在身侧，或者身前侧拿
                    case 5:{
                        if (averageTimeOfEveryStep < 20) {
                            Log.i("result", "快跑");
                            return 4;
                        } else if (averageTimeOfEveryStep >= 20 && averageTimeOfEveryStep < 50) {
                            Log.i("result", "慢跑");
                            return 3;
                        } else if (averageTimeOfEveryStep >= 50 && averageTimeOfEveryStep < 200) {
                            Log.i("result", "快走");
                            return 2;
                        } else if (averageTimeOfEveryStep >= 200 && averageTimeOfEveryStep < 10000) {
                            Log.i("result", "慢走");
                            return 1;
                        } else {
                            Log.i("result", "静止");
                            return 0;
                        }
                    }
                    default:{
                        if(averageTimeOfEveryStep > 12000) {
                            return 6;
                        }
                        else{
                            return 0;
                        }
                    }
                }

            }else{
                resSomeValue();
                return 0;
            }
        }else{//超时
            resSomeValue();
            return 0;
        }
    }

    private void resSomeValue(){
        stepCount = 0;
        averageTimeOfEveryStep = 0;
    }



    /*
     * 比较新旧值
     * 检测波峰
     * 以下四个条件判断为波峰：
     * 1.目前点为下降的趋势：isDirectionUp为false
     * 2.之前的点为上升的趋势：lastStatus为true
     * 3.到波峰为止，持续上升大于等于2次
     * 4.波峰值大于20
     * 记录波谷值
     * 1.观察波形图，可以发现在出现步子的地方，波谷的下一个就是波峰，有比较明显的特征以及差值
     * 2.所以要记录每次的波谷值，为了和下次的波峰做对比
     * */
    public boolean detectorPeak(float newValue, float oldValue) {
        lastStatus = isDirectionUp;
        //总加速度变大记录,当减小时把计数器清零,
        if (newValue >= oldValue) {
            isDirectionUp = true;
            continueUpCount++;
            continueDownFormerCount = continueDownCount;
            continueDownCount = 0;
        } else {
            continueUpFormerCount = continueUpCount;
            continueUpCount = 0;
            continueDownCount++;
            isDirectionUp = false;
        }

        if (!isDirectionUp && lastStatus
                && oldValue >= 12) {
            peakOfWave = oldValue;
            return true;

        } else if (!lastStatus && isDirectionUp && oldValue <= 6) {
            valleyOfWave = oldValue;
            return false;
        } else {
            return false;
        }
    }

    /*
     * 阈值的计算
     * 1.通过波峰波谷的差值计算阈值
     * 2.记录4个值，存入tempValue[]数组中
     * 3.在将数组传入函数averageValue中计算阈值
     * */
    public float peakValleyThread(float value) {
        float tempThread = ThreadValue;
        if (tempCount < ValueNum) {
            tempValue[tempCount] = value;
            tempCount++;
        } else {
            tempThread = averageValue(tempValue, ValueNum);
            for (int i = 1; i < ValueNum; i++) {
                tempValue[i - 1] = tempValue[i];
            }
            tempValue[ValueNum - 1] = value;
        }
        return tempThread;
    }

    /*
     * 梯度化阈值
     * 1.计算数组的均值
     * 2.通过均值将阈值梯度化在一个范围里
     * */
    public float averageValue(float value[], int n) {
        float ave = 0;
        for (int i = 0; i < n; i++) {
            ave += value[i];
        }
        ave = ave / ValueNum;
        if (ave >= 8)
            ave = (float) 4.3;
        else if (ave >= 7 && ave < 8)
            ave = (float) 3.3;
        else if (ave >= 4 && ave < 7)
            ave = (float) 2.3;
        else if (ave >= 3 && ave < 4)
            ave = (float) 2.0;
        else {
            ave = (float) 1.3;
        }
        return ave;
    }


    //版权声明：本文为CSDN博主「finnfu」的原创文章，遵循CC 4.0 by-sa版权协议，转载请附上原文出处链接及本声明。
    //原文链接：https://blog.csdn.net/finnfu/article/details/78543622
}
