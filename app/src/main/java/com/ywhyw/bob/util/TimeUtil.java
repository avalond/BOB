package com.ywhyw.bob.util;

/**
 * Created by yangfan on 2016/4/5.
 */
public class TimeUtil {

    public static String getStrFromInt(int time){
        String str = "";
        int min = time/60;
        int sen = time%60;
        if(min == 0){
            str = sen + "秒";
        }else{
            str = min + "分钟" + sen + "秒";
        }
        return str;
    }
}
