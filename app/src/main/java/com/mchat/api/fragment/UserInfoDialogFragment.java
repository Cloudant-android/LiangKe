package com.mchat.api.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.mchat.api.R;
import com.mchat.api.fragment.base.BaseFragmentDialog;

/**
 * Created by CloudAnt on 2016/12/8.
 */

public class UserInfoDialogFragment extends BaseFragmentDialog {
    private ImageButton closeView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //对话框底部对齐, 这句话不能写onCreate里(包括父类的)，在onCreate里执行getDialog()获取的值为null
        getDialog().getWindow().setGravity(Gravity.BOTTOM);

        return inflater.inflate(R.layout.dialog_setuserinfo,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        initView();
    }

    private void initView(){
        closeView  = (ImageButton) getActivity().findViewById(R.id.dialog_dismiss);
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UserInfoDialogFragment().dismiss();
            }
        });
    }




}
