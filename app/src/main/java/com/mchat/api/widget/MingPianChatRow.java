package com.mchat.api.widget;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.mchat.api.R;

/**
 * Created by CloudAnt on 2016/12/28.
 */

public class MingPianChatRow extends EaseChatRow {
    private ImageView mingpian_avater;
    private TextView mingpian_gender,mingpian_profression,mingpian_username;

    public MingPianChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ? R.layout.chat_row_received : R.layout.chat_row_sent, this);
    }

    @Override
    protected void onFindViewById() {
        mingpian_avater = (ImageView) findViewById(R.id.mingpian_avater);
        mingpian_gender = (TextView) findViewById(R.id.mingpian_gender);
        mingpian_profression = (TextView) findViewById(R.id.mingpian_profression);
        mingpian_username = (TextView) findViewById(R.id.mingpian_username);
    }

    @Override
    protected void onUpdateView() {

    }

    @Override
    protected void onSetUpView() {

    }

    @Override
    protected void onBubbleClick() {

    }
}
