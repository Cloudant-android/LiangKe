package com.mchat.api.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.mchat.api.Appconfig;
import com.mchat.api.hx.DemoHelper;
import com.mchat.api.LKApplication;
import com.mchat.api.R;
import com.mchat.api.activity.base.BaseActivity;
import com.mchat.api.lkinterface.RegisterService;
import com.mchat.api.model.RegisterInfo;
import com.mchat.api.hx.utils.LoadDialog;
import com.mchat.api.utils.ToastUtils;
import com.mchat.api.view.ClearWriteEditText;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private ImageView rg_img_backgroud;
    private ClearWriteEditText reg_username;
    private ClearWriteEditText reg_password;
    private ClearWriteEditText reg_repassword;
    private Button btn_register;
    private TextView tv_reg_sign;
    private TextView tv_reg_forget;
    private Retrofit retrofit;
    private String username;
    private String password;
    private String repassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = LKApplication.initRetrofit();
        setContentView(R.layout.activity_register);
        initView();
    }
    @Override
    protected void initView() {
        rg_img_backgroud = (ImageView) findViewById(R.id.rg_img_backgroud);
        reg_password = (ClearWriteEditText) findViewById(R.id.reg_password);
        reg_username = (ClearWriteEditText) findViewById(R.id.reg_username);
        reg_repassword = (ClearWriteEditText) findViewById(R.id.reg_repassword);
        btn_register = (Button) findViewById(R.id.reg_btn);
        tv_reg_sign = (TextView) findViewById(R.id.reg_login);
        tv_reg_forget = (TextView) findViewById(R.id.reg_forget);
        btn_register.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        tv_reg_forget.setOnClickListener(this);
        setbganim(rg_img_backgroud);
    }

    private void getData(){
        username = reg_username.getText().toString().trim();
        password = reg_repassword.getText().toString().trim();
        repassword = reg_repassword.getText().toString().trim();
        if ("".equals(username)){
            ToastUtils.ToastMessage(getApplicationContext(),"电话号码不能为空");
        }else if(!password.equals(repassword)){
            ToastUtils.ToastMessage(getApplicationContext(),"两次输入的密码不一致");
        }else {
            LoadDialog.show(RegisterActivity.this,"正在注册");
            RegisterService registerService = retrofit.create(RegisterService.class);
            registerService.register(LKApplication.getSign(),LKApplication.getTime(),username,password)
                    .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                    .observeOn(Schedulers.io())         //请求完成后在io线程中执行
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<RegisterInfo>() {
                        @Override
                        public void onCompleted() {
                            //请求完成
                            SharedPreferences sharedPreferences = getSharedPreferences("userinfo",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username",username);
                            editor.putString("password",password);
                            editor.apply();
                        }

                        @Override
                        public void onError(Throwable e) {
                            //请求失败
                            Log.e("RegisterActivity",e.getMessage());
                        }

                        @Override
                        public void onNext(RegisterInfo registerInfo) {
                            //请求成功
                            Appconfig.getMsgCode(getApplicationContext(),registerInfo.getCode());
                            String uid = String.valueOf(registerInfo.getData().getUid());
                            getrequestHX(uid);
                        }
                    });
        }
    }

    private void getrequestHX(final String uid){
        new Thread(new Runnable() {
            public void run() {
                try {
                    // call method in SDK
                    EMClient.getInstance().createAccount(uid, uid);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!RegisterActivity.this.isFinishing())
                                LoadDialog.dismiss(RegisterActivity.this);
                            // save current user
                            DemoHelper.getInstance().setCurrentUserName(username);
                            finish();
                        }
                    });
                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!RegisterActivity.this.isFinishing())
                                LoadDialog.dismiss(RegisterActivity.this);
                            int errorCode=e.getErrorCode();
                            if(errorCode== EMError.NETWORK_ERROR){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                            }else if(errorCode == EMError.USER_ALREADY_EXIST){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                            }else if(errorCode == EMError.USER_AUTHENTICATION_FAILED){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                            }else if(errorCode == EMError.USER_ILLEGAL_ARGUMENT){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name),Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.reg_btn:
                getData();
                break;
            case R.id.reg_login:
                SharedPreferences sharedPreferences = getSharedPreferences("userinfo",MODE_PRIVATE);
                String username = String.valueOf(sharedPreferences.getAll().get("usermame"));
                String password = (String) sharedPreferences.getAll().get("password");
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class).putExtra("username",username).putExtra("password",password));
                sharedPreferences.getAll().clear();
                finish();
                break;
            case R.id.reg_forget:
                ToastUtils.ToastMessage(RegisterActivity.this,"当前功能应资金不足，所以不做（任性一次=_=）");
                break;
        }

    }
}
