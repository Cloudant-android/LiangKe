package com.mchat.api.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;

import com.mchat.api.R;

/**
 * Created by CloudAnt on 2016/12/8.
 */

public class BaseFragmentDialog extends DialogFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog);//设置无标题、透明背景、无边框等等属性
        setCancelable(true);//设置点击除了对话框以外的部分就关闭
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置宽度满屏
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        //设置显示和关闭时的动画
        getDialog().getWindow().setWindowAnimations(R.style.style_item);
    }
}
