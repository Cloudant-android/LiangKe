package com.mchat.api.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.mchat.api.R;
import com.mchat.api.fragment.base.BaseFragment;
import com.mchat.api.hx.Constant;
import com.mchat.api.hx.activity.ChatActivity;
import com.mchat.api.hx.activity.NewGroupActivity;
import com.mchat.api.hx.activity.PublicGroupsActivity;
import com.mchat.api.hx.adapter.GroupAdapter;

import java.util.List;

/**
 * Created by CloudAnt on 2016/12/13.
 * 群组
 */

public class GroupListFragment extends BaseFragment {
    public static final String TAG = "GroupListFragment";
    private ListView groupListView;
    protected List<EMGroup> grouplist;
    private GroupAdapter groupAdapter;
    private InputMethodManager inputMethodManager;
    public static GroupListFragment instance;
    private View progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            swipeRefreshLayout.setRefreshing(false);
            switch (msg.what) {
                case 0:
                    refresh();
                    break;
                case 1:
                    Toast.makeText(getActivity(), R.string.Failed_to_get_group_chat_information, Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }
    };
    /**
     * 初始化view
     * @param  inflater view
     * @param  container
     * @return layout
     */
    @Override
    public View initRootView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_list,container,false);
    }

    /**
     * 初始化控件id
     */
    @Override
    public void onViewInitilize() {
        groupListView = (ListView) findViewById(R.id.group_list);
        instance = this;
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        grouplist = EMClient.getInstance().groupManager().getAllGroups();
        //show group list
        groupAdapter = new GroupAdapter(getActivity(), 1, grouplist);
        groupListView.setAdapter(groupAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        //pull down to refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Thread(){
                    @Override
                    public void run(){
                        try {
                            EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                            handler.sendEmptyMessage(0);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(1);
                        }
                    }
                }.start();
            }
        });

        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    // create a new group
                    startActivityForResult(new Intent(getActivity(), NewGroupActivity.class), 0);
                } else if (position == 2) {
                    // join a public group
                    startActivityForResult(new Intent(getActivity(), PublicGroupsActivity.class), 0);
                } else {
                    // enter group chat
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    // it is group chat
                    intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                    intent.putExtra("userId", groupAdapter.getItem(position - 3).getGroupId());
                    startActivityForResult(intent, 0);
                }
            }

        });
        groupListView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                    if (getActivity().getCurrentFocus() != null)
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
    }
    private void refresh(){
        grouplist = EMClient.getInstance().groupManager().getAllGroups();
        groupAdapter = new GroupAdapter(getContext(), 1, grouplist);
        groupListView.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();
    }
    @Override
    public void onResume() {
        refresh();
        super.onResume();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
    }
}
