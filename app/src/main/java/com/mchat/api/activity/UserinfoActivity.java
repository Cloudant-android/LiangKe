package com.mchat.api.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.mchat.api.Appconfig;
import com.mchat.api.LKApplication;
import com.mchat.api.R;
import com.mchat.api.sqlitedb.UserInfoCacheSvc;
import com.mchat.api.activity.base.BaseActivity;
import com.mchat.api.lkinterface.SetUserInfoService;
import com.mchat.api.model.SetUserInfo;
import com.mchat.api.utils.ImageUtils;
import com.mchat.api.utils.OpenLocaImage;
import com.mchat.api.utils.SaveBitmap;
import com.mchat.api.utils.UriToPath;

import java.io.File;
import java.util.List;

import cn.carbs.android.avatarimageview.library.AvatarImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserinfoActivity extends BaseActivity{
    private AvatarImageView set_avater;
    private EditText setnickname,setgender,setprofession;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        retrofit = LKApplication.initRetrofit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        setTranslucentStatus();
        initView();
    }

    @Override
    protected void initView() {
        set_avater = (AvatarImageView) findViewById(R.id.set_avater);
        setnickname = (EditText) findViewById(R.id.setnickname);
        setgender = (EditText) findViewById(R.id.setgender);
        setprofession = (EditText) findViewById(R.id.setprofession);
        imageClick();
    }
    public void saveUserInfo(View view) {
        String gender = setgender.getText().toString();
        String profession = setprofession.getText().toString();
        String nickname = setnickname.getText().toString();
        String logo = LKApplication.getInstance().setlogo();
        File file = new File(logo);
        MediaType textType = MediaType.parse("text/plain");
        RequestBody setGender = RequestBody.create(textType, gender);
        RequestBody setProfession = RequestBody.create(textType, profession);
        RequestBody setNickname = RequestBody.create(textType, nickname);
        MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody requestBody =RequestBody.create(MediaType.parse("iamge/png"), file);
        multipartBody.addFormDataPart("logo",file.getName(),requestBody);
        List<MultipartBody.Part> part = multipartBody.build().parts();
        SetUserInfoService setUserInfoService = retrofit.create(SetUserInfoService.class);
        setUserInfoService.setUserinfo(LKApplication.getSign(),
                LKApplication.getTime(),
                LKApplication.getInstance().getToken(),
                LKApplication.getInstance().getUid(),
                setGender,setProfession,setNickname,part)
                .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(Schedulers.io())         //请求完成后在io线程中执行
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SetUserInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("error_set",e.getMessage());

                    }

                    @Override
                    public void onNext(SetUserInfo setUserInfo) {
                        Appconfig.getMsgCode(UserinfoActivity.this,setUserInfo.getCode());
                        LKApplication.getInstance().setSharePreferences("logo",setUserInfo.getData().getLogo());
                        LKApplication.getInstance().setSharePreferences("nickname",setUserInfo.getData().getNickname());
                        UserInfoCacheSvc.createOrUpdate(setUserInfo.getData().getUid(),setUserInfo.getData().getNickname(),LKApplication.requrst_path+setUserInfo.getData().getLogo());
                    }
                });

    }

    private void imageClick(){
        set_avater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivityForResult(OpenLocaImage.openImage(),0);
            }
        });
    }
    private String image_path;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if(data!=null){
                    new Thread(){
                        @Override
                        public void run() {
//                            Bitmap bitmap = UriToBitmap.getBitmapFromUri(UserinfoActivity.this,data.getData());
                            Bitmap  bitmap = ImageUtils.getBitmap(UriToPath.getRealFilePath(UserinfoActivity.this,data.getData()));
                            SaveBitmap.saveMyBitmap(UserinfoActivity.this, bitmap, "avater");
                            image_path = SaveBitmap.PATH + "/" + "avater" + ".png";
                            Log.d("iamge_path",image_path);
                            final Bitmap finalBitmap = bitmap;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    set_avater.setBitmap(finalBitmap);
                                }
                            });
                            LKApplication.getInstance().setSharePreferences("setlogo",image_path);
                        }
                    }.start();
                }
                break;
        }
    }
}
