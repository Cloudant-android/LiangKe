package com.mchat.api.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hyphenate.EMCallBack;
import com.mchat.api.R;
import com.mchat.api.activity.base.BaseActivity;
import com.mchat.api.hx.DemoHelper;
import com.mchat.api.hx.utils.LoadDialog;
import com.mchat.api.utils.ToastUtils;

public class SettingActivity extends BaseActivity {
    private Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTranslucentStatus();
        initView();
    }

    @Override
    protected void initView() {
        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadDialog.show(SettingActivity.this,"正在退出");
                DemoHelper.getInstance().logout(true, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                LoadDialog.dismiss(SettingActivity.this);
                                new MainActivity().finish();
                                // 重新显示登陆页面
                                startActivity(new Intent(SettingActivity.this,LoginActivity.class));
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(int code, String message) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                LoadDialog.dismiss(SettingActivity.this);
                                ToastUtils.ToastMessage(SettingActivity.this,"unbind devicetokens failed");
                            }
                        });
                    }
                });

            }
        });
    }
}
