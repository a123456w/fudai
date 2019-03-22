package com.ruirong.chefang.util;

/**
 * Created by dillon on 2017/5/15.
 */

public class TextUtils {

    public static String hideText(String string){
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < string.length() - 4; i++) {
            stringBuffer.append("*");
        }
        String subString = string.substring(string.length()-4);
        stringBuffer.append(subString);
        return stringBuffer.toString();
    }
}
