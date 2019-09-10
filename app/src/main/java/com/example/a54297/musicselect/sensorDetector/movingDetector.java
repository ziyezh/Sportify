package com.example.a54297.musicselect.sensorDetector;
import android.content.Context;
import  android.util.Log;
import android.widget.Toast;
/*
 *    author chenzhentao liuguiyi a
 *    date 8.27 8.28 8.29 9.4
 *
 */
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
    final float InitialValue = (float) 2;
    //初始阈值
    float ThreadValue = (float) 0.7;
    //波峰波谷时间差
    int TimeInterval = 100;
    //设置状态
    int result = 0;
    //new extra↓
    float[] stepTime = new float[10];
    float[] peakValues = new float[5];
    int peaks=0;
    //波峰值及波峰数目
    float time1=520,time2=2000;
    //历史次数
    int numOfHistory=0;

    private int stepCount = 0;
    private long timeOfLastStep = 0;
    private long timeOfThisStep = 0;
    private float averageTimeOfEveryStep = 0;
    private float peakValue = 0;
    private Context c;
    private int holdingStyles;
    public movingDetector(Context c){
        this.c= c;
        holdingStyles = 0;
    }

    //数据的输入口
    public int inputValue(float value, int gesture){

        if(value<10.2){numOfHistory++;}
        else {numOfHistory=0;}

        holdingStyles = gesture;
        detectorNewStep(value);
//        outData_all();
//       System.out.println("返回值返回值返回值=-=返回值返回值"+result);
        if(numOfHistory>18){
            return 0;
        }
        return result;
    }

    /*
     * 检测步子，并开始计步
     * 1.传入sersor中的数据
     * 2.如果检测到了波峰，并且符合时间差以及阈值的条件，则判定为1步
     * 3.符合时间差条件，波峰波谷差值大于initialValue，则将该差值纳入阈值的计算中
     * */

    public void detectorNewStep(float values) {

        if(gravityOld == 0.0) {
            gravityOld = values;

        }else {
            boolean check = detectorPeak(values, gravityOld);
            if (check) {
                timeOfLastPeak = timeOfThisPeak;
                timeOfNow = System.currentTimeMillis();
                if ((timeOfNow - timeOfLastPeak >= TimeInterval) && (peakOfWave - valleyOfWave >= ThreadValue)) {
                    timeOfThisPeak = timeOfNow;
                    /*
                     走路或者跑步的识别
                     * */
                    result = countStep();
                }

                if (timeOfNow - timeOfLastPeak >= TimeInterval && (peakOfWave - valleyOfWave >= InitialValue)) {
                    timeOfThisPeak = timeOfNow;
                    ThreadValue = peakValleyThread(peakOfWave - valleyOfWave);
                }
            }

        }
        gravityOld = values;
    }


    private int countStep() {
        Log.i("=====","===countStep===  "+result+"===");
        timeOfLastStep = timeOfThisStep;
        timeOfThisStep = System.currentTimeMillis();
        long diffValue = timeOfThisStep - timeOfLastStep;
        System.out.print("==result===" + result + "===averageStepTime===" + averageTimeOfEveryStep+ " stepNum="+stepCount+ " style="+holdingStyles );
        if (diffValue < time2) {//else 超时重置
            //更新每一步时间
            if (stepCount < 9){
                stepTime[stepCount]=diffValue;
                stepCount++;
                averageTimeOfEveryStep = averageStepTime();
                peakValue = averagePeak();
                //return 5;
            }else if(stepCount == 9||stepCount == 10){//else 重置
                if(stepTime[9]<5){
                    stepTime[9]=diffValue;
                }
                else{
                    for(int i=1;i<9;i++){
                        stepTime[i-1]=stepTime[i];
                    }
                    stepTime[9]=diffValue;
                }//更新每一步时间↑
                stepCount=10;
                averageTimeOfEveryStep = averageStepTime();//计算运动速度
                peakValue = averagePeak();
                Log.i("result","averageTimeOfEveryStep: "+averageTimeOfEveryStep);
                //确定运动状态
            }else{//count > 10 处理
                resSomeValue();
                return 0;
            }

            System.out.println("num of his:"+numOfHistory);
            if(peakValue<11){
                return 0;
            }
            switch (holdingStyles){
                //口袋
                case 0:{
                    if (averageTimeOfEveryStep < time1) {
                        if(peakValue<14){
                            return 1;
                        }
                        else if(peakValue < 19){
                            Log.i("result", "慢" +
                                    "快走");
                            return 2;
                        }else if(peakValue < 29) {
                            Log.i("result", "慢跑");
                            return 3;
                        }else{
                            Log.i("result", "快跑");
                            return 4;
                        }
                    } else if (averageTimeOfEveryStep >= time1 && averageTimeOfEveryStep < time2) {
                        Log.i("result", "走");
                        return 1;
                    }  else {
                        Log.i("result", "静止");
                        return 0;
                    }
                }
                //手中，身前
                case 2: {
                    if (averageTimeOfEveryStep < time1) {//阈（fa = =||）值要改啊
                        if(peakValue<12){
                            return 1;
                        }
                        else if(peakValue < 15){
                            Log.i("result", "快走");
                            return 2;
                        }else if(peakValue < 20) {
                            Log.i("result", "慢跑");
                            return 3;
                        }else{
                            Log.i("result", "快跑");
                            return 4;
                        }
                    } else if (averageTimeOfEveryStep >= time1 && averageTimeOfEveryStep < time2) {
                        Log.i("result", "走");
                        return 1;
                    }  else {
                        Log.i("result", "静止");
                        return 0;
                    }
                }
                //身侧或者手机侧放
                case 5:{
                    if (averageTimeOfEveryStep < time1) {
                        if(peakValue<13){
                            return 1;
                        }
                        else if(peakValue < 15){
                            Log.i("result", "快走");
                            return 2;
                        }else if(peakValue < 22) {
                            Log.i("result", "慢跑");
                            return 3;
                        }else{
                            Log.i("result", "快跑");
                            return 4;
                        }
                    } else if (averageTimeOfEveryStep >= time1 && averageTimeOfEveryStep < time2) {
                        Log.i("result", "走");
                        return 1;
                    }  else {
                        Log.i("result", "静止");
                        return 0;
                    }
                }
                default:{
                    if (averageTimeOfEveryStep < time1) {

                        if(peakValue<12){
                            return 1;
                        }
                        else if(peakValue < 15){
                            Log.i("result", "慢" +
                                    "快走");
                            return 2;
                        }else if(peakValue < 20) {
                            Log.i("result", "慢跑");
                            return 3;
                        }else{
                            Log.i("result", "快跑");
                            return 4;
                        }
                    } else if (averageTimeOfEveryStep >= time1 && averageTimeOfEveryStep < time2) {
                        Log.i("result", "走");
                        return 1;
                    }  else {
                        Log.i("result", "静止");
                        return 0;
                    }
                }//end default
            }//end switch
        }else{//超时  diffValue>=2000处理
            resSomeValue();
            return 0;
        }
    }

    private void resSomeValue(){
        stepCount = 0;
        averageTimeOfEveryStep = 0;
    }

    public float averageStepTime(){//每一步时间
        float t=0;
        for(int i=0;i<stepCount;i++){
            t += stepTime[i];
        }
        if(stepCount>0)
            return t/stepCount;
        else return 500;
    }
    public float averagePeak(){
        float num=0;
        for(int i=0;i<peaks;i++){
            num += peakValues[i];
        }
        if(peaks!=0) {
            return num / peaks;
        }
        else {
            return 10;
        }
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
        Log.i("=====","===detectorPeak===   newValue"+newValue+"==="+oldValue);
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

        if (!isDirectionUp && lastStatus && oldValue >= 9.5) {
            peakOfWave = oldValue;

            if(peaks<4){// 记录peak值 5个
                peakValues[peaks]=peakOfWave;
                peaks++;
            }else{
                peaks=5;
                if(peakValues[4]<5){
                    peakValues[4]=peakOfWave;
                }
                else{
                    peakValues[0]=peakValues[1];
                    peakValues[1]=peakValues[2];
                    peakValues[2]=peakValues[3];
                    peakValues[3]=peakValues[4];
                    peakValues[4]=peakOfWave;
                }
            }

            return true;
        } else if (!lastStatus && isDirectionUp && oldValue <= 10) {
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
            ave = (float) 4;
        else if (ave >= 7 && ave < 8)
            ave = (float) 3;
        else if (ave >= 4 && ave < 7)
            ave = (float) 2;
        else if (ave >= 3 && ave < 4)
            ave = (float) 1.8;
        else {
            ave = (float) 1.3;
        }
        return (float)0.7;
    }
//    public void outData_pocket(){//搜索System.out: ==r查看
//        if(stepCount!=0) {
//            System.out.print("==result===" + result + "===averageStepTime===" + averageStepTime()+ " stepNum="+stepCount+ " style="+holdingStyles );
//        }//输出啊啊
//        String tempStr="";
//        averageTimeOfEveryStep=(long)averageStepTime();
//        if (averageTimeOfEveryStep >= 850 && averageTimeOfEveryStep < 1500) {
//            tempStr="慢走";
//        } else if (averageTimeOfEveryStep >= 640 && averageTimeOfEveryStep < 850) {
//            tempStr="  走";
//        } else if (averageTimeOfEveryStep >= 300 && averageTimeOfEveryStep < 640) {
//            if(averagePeak()<24)tempStr="  快走";
//            else if(averagePeak()<35)tempStr="  跑";
//            else if(averagePeak()<95)tempStr="疾跑";
//            else tempStr="飞/晃手机";
//
//        } else if(averageTimeOfEveryStep >= 1500){
//            tempStr="  停";
//        }
//        else{
//            tempStr="飞奔";
//        }
//        System.out.print(tempStr);
//        System.out.println(" 波峰波谷："+peakOfWave + valleyOfWave+" 平均波峰："+ averagePeak());
//    }
//
//    public void outData_hand(){//搜索System.out: ==r查看
//        if(stepCount!=0) {
//            System.out.print("==result===" + result + "===averageStepTime===" + averageStepTime()+ " stepNum="+stepCount+ " style="+holdingStyles );
//        }//输出
//        String tempStr="";
//        averageTimeOfEveryStep=(long)averageStepTime();
//        if (averageTimeOfEveryStep >= 850 && averageTimeOfEveryStep < 2000) {
//            tempStr="慢走";
//        } else if (averageTimeOfEveryStep >= 650 && averageTimeOfEveryStep < 850) {
//            tempStr="  走";
//        } else if (averageTimeOfEveryStep >= 300 && averageTimeOfEveryStep < 650) {
//            if(averagePeak()<18)tempStr="快走";
//            else if(averagePeak()<30)tempStr="  跑";
//            else if(averagePeak()<90)tempStr="疾跑";
//            else tempStr="飞奔";
//
//        } else if(averageTimeOfEveryStep >= 1500){
//            tempStr="  停";
//        }
//        else{
//            tempStr="飞奔";
//        }
//        System.out.print(tempStr);
//        System.out.println(" 波峰："+peakOfWave +" 平均波峰："+ averagePeak());
//    }
//
//
//
//
    //
//    public void outData_all(){//搜索System.out: =-=r查看
//        if(stepCount!=0) {
//            System.out.println("=-=运动方式:" + result + "  步长:" + averageStepTime()+ " 步数="+stepCount+ " 握机状态="+holdingStyles );
//        }//输出
//        else {
//            System.out.println("步数=0");
//        }
//        String tempStr="";
//        averageTimeOfEveryStep=(long)averageStepTime();
//        peakValue=averagePeak();
//        if (averageTimeOfEveryStep < time1) {
//            if(peakValue < 24){
//                tempStr="快走";
//            }else if(peakValue < 35) {
//                tempStr="慢跑";
//            }else{
//                tempStr="快跑";
//            }
//        } else if (averageTimeOfEveryStep >= time1 && averageTimeOfEveryStep < time2) {
//            tempStr="慢走";
//        }  else {
//            tempStr="静止";
//        }
//        if(peakValue<11)tempStr="静1止";
//        System.out.print(tempStr);
//        System.out.println("=-=波峰："+peakOfWave + " 平均波峰："+ averagePeak());
//    }

}