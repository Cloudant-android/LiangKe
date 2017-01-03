package com.mchat.api.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.mchat.api.sqlitedb.SharePrefConstant;
import com.mchat.api.sqlitedb.UserInfoCacheSvc;
import com.mchat.api.fragment.base.BaseFragment;
import com.mchat.api.hx.Constant;
import com.mchat.api.R;
import com.mchat.api.hx.DemoHelper;
import com.mchat.api.hx.activity.GroupsActivity;
import com.mchat.api.hx.db.InviteMessgeDao;
import com.mchat.api.view.DragPointView;

import java.util.List;

/**
 * Created by CloudAnt on 2016/11/30.
 */

public class ConversationFragment extends BaseFragment implements View.OnClickListener {
    private Fragment[] fragments;
    private Button[] Tabs;
    private ConversationMsgFragment msgFragment;
    private GroupListFragment groupListFragment;
    private int currentTabIndex;
    private int index;
    private InviteMessgeDao inviteMessgeDao;
    private android.app.AlertDialog.Builder exceptionBuilder;
    private boolean isExceptionDialogShow =  false;
    private BroadcastReceiver internalDebugReceiver;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;
    private DragPointView mUnreadNumView;
    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conversation,container,false);
    }

    @Override
    protected void onViewInitilize() {
        mUnreadNumView = (DragPointView) getActivity().findViewById(R.id.seal_num);
        msgFragment = new ConversationMsgFragment();
        groupListFragment = new GroupListFragment();
        inviteMessgeDao = new InviteMessgeDao(getContext());
        fragments = new Fragment[]{msgFragment,groupListFragment};
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_content,msgFragment).show(msgFragment)
                .commit();
        Tabs = new Button[2];
        Tabs[0] = (Button) findViewById(R.id.message_conversation);
        Tabs[1] = (Button) findViewById(R.id.group_list);
        Tabs[0].setSelected(true);
        Tabs[0].setOnClickListener(this);
        Tabs[1].setOnClickListener(this);
        registerBroadcastReceiver();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.message_conversation:
                index=0;
                break;
            case R.id.group_list:
                index=1;
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                fragmentTransaction.add(R.id.fragment_content, fragments[index]);
            }
            fragmentTransaction.show(fragments[index]).commit();
        }
        Tabs[currentTabIndex].setSelected(false);
        Tabs[index].setSelected(true);
        currentTabIndex = index;
    }
    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);

                try {
                    String uid = message.getStringAttribute(SharePrefConstant.ChatUserId);
                    String avater = message.getStringAttribute(SharePrefConstant.ChatUserPic);
                    String nickname = message.getStringAttribute(SharePrefConstant.ChatUserNick);
                    UserInfoCacheSvc.createOrUpdate(uid,avater,nickname);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }


            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {}
    };

    private void refreshUIWithMessage() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                updateUnreadLabel();
                if (currentTabIndex == 0) {
                    // refresh conversation list
                    if (msgFragment != null) {
                        msgFragment.refresh();
                    }
                }
            }
        });
    }
    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                if (currentTabIndex == 0) {
                    // refresh conversation list
                    if (msgFragment != null) {
                        msgFragment.refresh();
                    }
                }
                String action = intent.getAction();
                if(action.equals(Constant.ACTION_GROUP_CHANAGED)){
                    if (EaseCommonUtils.getTopActivity(getContext()).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private void unregisterBroadcastReceiver(){
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (exceptionBuilder != null) {
            exceptionBuilder.create().dismiss();
            exceptionBuilder = null;
            isExceptionDialogShow = false;
        }
        unregisterBroadcastReceiver();


    }

    /**
     * update unread message count
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            mUnreadNumView.setText(String.valueOf(count));
            mUnreadNumView.setVisibility(View.VISIBLE);
        } else {
            mUnreadNumView.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * get unread message count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        for(EMConversation conversation:EMClient.getInstance().chatManager().getAllConversations().values()){
            if(conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount=chatroomUnreadMsgCount+conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal-chatroomUnreadMsgCount;
    }
    @Override
    public void onResume() {
        super.onResume();
        updateUnreadLabel();
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(getActivity());

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    public void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(getActivity());

        super.onStop();
    }
}
