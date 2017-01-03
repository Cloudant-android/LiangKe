package com.mchat.api.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 大灯泡 on 2016/1/15.
 * Toast工具类
 */
public class ToastUtils {

    public static void ToastMessage(Context context, String msg) {
       Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
    }
    public static void showWaitToast(Context context) {
        Toast.makeText(context,"工程师正在研发中...", Toast.LENGTH_SHORT).show();
    }

}
