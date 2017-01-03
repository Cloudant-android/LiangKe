package com.mchat.api;

import android.content.Context;

import com.mchat.api.utils.ToastUtils;

/**
 * Created by CloudAnt on 2016/12/6.
 */

public class Appconfig {

    public static void getMsgCode(Context context,int code){
        switch (code){
            case 1000:
                ToastUtils.ToastMessage(context,"请求成功");
                break;
            case 1001:
                ToastUtils.ToastMessage(context,"非法请求");
                break;
            case 1002:
                ToastUtils.ToastMessage(context,"用户错误");
                break;
            case 1003:
                ToastUtils.ToastMessage(context,"token错误");
                break;
            case 1004:
                ToastUtils.ToastMessage(context,"签名错误");
                break;
            case 1005:
                ToastUtils.ToastMessage(context,"时间错误");
                break;
            case 1006:
                ToastUtils.ToastMessage(context,"参数缺失");
                break;
            case 1007:
                ToastUtils.ToastMessage(context,"用户名已存在");
                break;
            case 1008:
                ToastUtils.ToastMessage(context,"用户名不存在");
                break;
            case 1009:
                ToastUtils.ToastMessage(context,"用户已被禁用");
                break;
            case 1010:
                ToastUtils.ToastMessage(context,"服务器错误，请重试");
                break;
            case 1011:
                ToastUtils.ToastMessage(context,"密码错误");
                break;
            case 1012:
                ToastUtils.ToastMessage(context,"图片上传错误");
                break;
        }

    }


}
