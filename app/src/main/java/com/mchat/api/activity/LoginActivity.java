package com.mchat.api.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.mchat.api.Appconfig;
import com.mchat.api.LKApplication;
import com.mchat.api.R;
import com.mchat.api.sqlitedb.UserInfoCacheSvc;
import com.mchat.api.activity.base.BaseActivity;
import com.mchat.api.lkinterface.LoginService;
import com.mchat.api.model.LoginInfo;
import com.mchat.api.hx.utils.LoadDialog;
import com.mchat.api.view.ClearWriteEditText;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mImg_Background;
    private ClearWriteEditText mPhoneEdit, mPasswordEdit;
    private TextView toregisteractivity;
    private Button btn_sign;
    private TextView sg_forget;
    private Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = LKApplication.initRetrofit();
        setContentView(R.layout.activity_login);
        initView();
    }

    @Override
    protected void initView(){
        mPhoneEdit = (ClearWriteEditText) findViewById(R.id.de_login_phone);
        mPasswordEdit = (ClearWriteEditText) findViewById(R.id.de_login_password);
        mImg_Background = (ImageView) findViewById(R.id.de_img_backgroud);
        toregisteractivity = (TextView) findViewById(R.id.toregisteractivity);
        btn_sign = (Button) findViewById(R.id.de_login_sign);
        sg_forget = (TextView) findViewById(R.id.de_login_forgot);
        setbganim(mImg_Background);
        btn_sign.setOnClickListener(this);
        toregisteractivity.setOnClickListener(this);
    }

    private void getData(){
        String username = mPhoneEdit.getText().toString();
        String password = mPasswordEdit.getText().toString();
        LoginService loginService = retrofit.create(LoginService.class);
        loginService.login(LKApplication.getSign(),LKApplication.getTime(),username,password)
                .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(Schedulers.io())         //请求完成后在io线程中执行
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LoginInfo loginInfo) {
                        LoadDialog.show(LoginActivity.this,"正在登录");
                        Appconfig.getMsgCode(getApplicationContext(),loginInfo.getCode());
                        SharedPreferences sharedPreferences = getSharedPreferences("loginData",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("nickname",loginInfo.getData().getNickname());
                        editor.putString("logo",loginInfo.getData().getLogo());
                        editor.putString("uid",loginInfo.getData().getUid());
                        editor.putString("gender",loginInfo.getData().getGender());
                        editor.putString("token",loginInfo.getData().getToken());
                        editor.putString("profession",loginInfo.getData().getProfession());
                        editor.putString("username",loginInfo.getData().getUsername());
                        editor.putString("reg_time",loginInfo.getData().getReg_time());
                        editor.apply();
                        UserInfoCacheSvc.createOrUpdate(loginInfo.getData().getUid(),loginInfo.getData().getNickname(),LKApplication.requrst_path+loginInfo.getData().getLogo());
                        String uid = loginInfo.getData().getUid();
                        getHX_Login(uid);
                    }
                });
    }

    private void getHX_Login(String uid){
        EMClient.getInstance().login(uid, uid, new EMCallBack() {

            @Override
            public void onSuccess() {
                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                if (!LoginActivity.this.isFinishing() ) {
                    LoadDialog.dismiss(LoginActivity.this);
                }
                Intent intent = new Intent(LoginActivity.this,
                        MainActivity.class);
                startActivity(intent);

                finish();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(final int code, final String message) {
                runOnUiThread(new Runnable() {
                    public void run() {
                       LoadDialog.dismiss(LoginActivity.this);
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.de_login_sign:
                getData();
                break;
            case R.id.toregisteractivity:
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
                break;
        }
    }
}
