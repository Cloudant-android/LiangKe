package com.mchat.api;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.mchat.api.sqlitedb.UserInfoCacheSvc;
import com.mchat.api.hx.DemoHelper;
import com.mchat.api.hx.db.UserDao;
import com.mchat.api.lkinterface.FriendsService;
import com.mchat.api.model.FriendsListInfo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by CloudAnt on 2016/11/30.
 * application
 * 初始化Retrofit
 * 初始化EaseUI
 * 单例LKApplication
 */

public class LKApplication extends Application {
    /**
     * 请求状态码
     1000	请求成功
     1001	非法请求
     1002	用户错误
     1003	token错误
     1004	签名错误
     1005	时间错误
     1006	参数缺失
     1007	用户名已存在
     1008	用户名不存在
     1009	用户已被禁用
     1010	服务器错误，请重试
     1011	密码错误
     1012	图片上传错误
     */
    private static LKApplication instance;
    private Context applicationContext;
    public static String requrst_path = "http://120.77.39.171/";
    private static Retrofit retrofit;
    private static SharedPreferences sharedPreferences;
    private int page = 1;
    private int page_count = 0;
    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;
        retrofit = initRetrofit();
        SDKInitializer.initialize(this);
        DemoHelper.getInstance().init(applicationContext);
        sharedPreferences = getSharedPreferences("loginData",MODE_PRIVATE);
        request_data();
    }

    public static Retrofit initRetrofit(){
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(requrst_path)
                .build();
        return retrofit;
    }

    public static int getTime(){
       int time = (int) (System.currentTimeMillis()/1000);
        return time;
    }
    public static String getSign(){
        String sign ="key=bdffc041a426402a636819566f04fe2a95d1a303&time="+getTime();
        String h =  getSha1(sign);
        return h;
    }


    public static LKApplication  getInstance(){
        return instance;
    }
    /**
     * 哈希加密
     * @param str
     * @return
     */
    public static String getSha1(String str) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public  String getUserName(){
        String username = String.valueOf(sharedPreferences.getAll().get("username"));
        return username;
    }

    public  String getNickName(){
        String nickname = String.valueOf(sharedPreferences.getAll().get("nickname"));
        return nickname;
    }
    public  String getUid(){
        String uid = String.valueOf(sharedPreferences.getAll().get("uid"));
        return uid;
    }
    public  String getGender(){
        String gender = String.valueOf(sharedPreferences.getAll().get("gender"));
        return gender;
    }
    public  String getProfession(){
        String profession = String.valueOf(sharedPreferences.getAll().get("profession"));
        return profession;
    }
    public  String getToken(){
        String token = String.valueOf(sharedPreferences.getAll().get("token"));
        return token;
    }
    public  String getLogo(){

        String logo = String.valueOf(sharedPreferences.getString("logo",null));
        return logo;
    }
    public  String getRegTime(){
        String reg_time = String.valueOf(sharedPreferences.getAll().get("reg_time"));
        return reg_time;
    }
    public String setlogo(){
        String setlogo = String.valueOf(sharedPreferences.getString("setlogo",null));
        return setlogo;
    }
    public void setSharePreferences(String str,String vaule){
        SharedPreferences sharedPreferences = getSharedPreferences("loginData",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if ("".equals(sharedPreferences.getString(str,null))){
            editor.putString(str,vaule);
            editor.apply();
        }else {
            editor.remove(str);
            editor.putString(str,vaule);
            editor.apply();
        }

    }

    public void request_data(){
        getData(page);
        if (page<=page_count){
            for (int i =page;i<=page_count;i++){
                getData(i);
            }
        }
    }

    public void getData(int page){
        FriendsService friendsService = retrofit.create(FriendsService.class);
        friendsService.FriendsList(LKApplication.getSign(),
                LKApplication.getTime(),
                LKApplication.getInstance().getToken(),LKApplication.getInstance().getUid(),page)
                .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(Schedulers.io())         //请求完成后在io线程中执行
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FriendsListInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("error_friends",e.getMessage());

                    }

                    @Override
                    public void onNext(FriendsListInfo friendsListInfo) {
                        if (friendsListInfo.getCode()==1000){
                            page_count = friendsListInfo.getData().getTotal_rows();
                            for (FriendsListInfo.DataBean.ListBean listBean : friendsListInfo.getData().getList()){
                                UserDao userDao = new UserDao(applicationContext);
                                EaseUser easeUser = new EaseUser(listBean.getFriend_id());
                                easeUser.setAvatar(listBean.getLogo());
                                easeUser.setNick(listBean.getNickname());
                                EaseCommonUtils.setUserInitialLetter(easeUser);
                                userDao.saveContact(easeUser);
                                UserInfoCacheSvc.createOrUpdate(listBean.getFriend_id(),listBean.getNickname(),LKApplication.requrst_path+listBean.getLogo());
                                Log.d("listfriends",listBean.getFriend_id());
                            }
                        }

                    }
                });
    }






}
