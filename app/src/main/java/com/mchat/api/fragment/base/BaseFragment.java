package com.mchat.api.fragment.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mchat.api.R;

/**
 * Created by CloudAnt on 2016/12/12.
 * basefragment
 */

public abstract class BaseFragment extends Fragment {
    private Context mContext;
    private View rootView;
    private long lastVisibletime;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mContext ==null)
            mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mContext ==null) mContext = getActivity();
        if (rootView ==null){
            rootView = initRootView(inflater,container,savedInstanceState);
        }
        onViewInitilize();

        return rootView;
    }

    public void setUserVisibleHint(boolean visibleHint){
        onVisibleChange(visibleHint);
    }
    protected void onVisibleChange(boolean idVisibleToUser){
        if (idVisibleToUser){
            lastVisibletime = System.currentTimeMillis();
        }
    }
    protected abstract  View initRootView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState);
    protected abstract void onViewInitilize();
    public final  View findViewById(@IdRes int id){
        if (id<0||rootView == null){
            return null;
        }
        return rootView.findViewById(id);
    }
    public Context getContext(){
        return mContext;
    }
    public View getRootView(){
        return rootView;
    }

    public long getLastVisibletime() {
        return lastVisibletime;
    }
}
