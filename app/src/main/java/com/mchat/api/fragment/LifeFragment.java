package com.mchat.api.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mchat.api.R;
import com.mchat.api.fragment.base.BaseFragment;

/**
 * Created by CloudAnt on 2016/11/30.
 */

public class LifeFragment extends BaseFragment {
    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_life,null);
    }

    @Override
    protected void onViewInitilize() {

    }

}
