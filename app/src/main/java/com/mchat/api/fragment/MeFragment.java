package com.mchat.api.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mchat.api.Appconfig;
import com.mchat.api.LKApplication;
import com.mchat.api.R;
import com.mchat.api.activity.SettingActivity;
import com.mchat.api.activity.UserinfoActivity;
import com.mchat.api.fragment.base.BaseFragment;
import com.mchat.api.lkinterface.UserInfoService;
import com.mchat.api.model.UserInfo;

import cn.carbs.android.avatarimageview.library.AvatarImageView;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by CloudAnt on 2016/11/30.
 */

public class MeFragment extends BaseFragment {
    private RelativeLayout layout_setuserinfo;
    private AvatarImageView user_avater;
    private TextView nick_name;
    private TextView uid;
    private Retrofit retrofit;
    private RelativeLayout start_setting;


    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me,null);
    }

    @Override
    protected void onViewInitilize() {
        retrofit = LKApplication.initRetrofit();
        layout_setuserinfo = (RelativeLayout)findViewById(R.id.layout_setuserinfo);
        start_setting = (RelativeLayout) findViewById(R.id.start_setting);
        user_avater = (AvatarImageView) findViewById(R.id.user_avater);
        nick_name = (TextView) findViewById(R.id.nickname);
        uid  = (TextView)findViewById(R.id.uid);

        layout_setuserinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), UserinfoActivity.class));
            }
        });
        start_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });
        getData();
    }

    private void getData(){
        if (""==LKApplication.getInstance().getLogo()){
            //设置文字：
            user_avater.setTextAndColor(LKApplication.getInstance().getUserName(), AvatarImageView.COLORS[5]);//直接设置颜色，如果设置的文字为多个字符，则会具有clip效果，单个字符没有clip效果
        } else {
            //当用户设置头像之后 ，将在这里绑定ui，显示用户设置的头像
            String logo =   LKApplication.getInstance().getLogo();
            Log.d("logo_avater",logo);
            Glide.with(getActivity()).load(LKApplication.requrst_path+logo).into(user_avater);
        }
        if (""==LKApplication.getInstance().getNickName()){
            nick_name.setText("昵称:未设置");
        }else {
            nick_name.setText("昵称:"+LKApplication.getInstance().getNickName());

        }
        uid.setText("用户名:"+LKApplication.getInstance().getUserName());
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }





}
