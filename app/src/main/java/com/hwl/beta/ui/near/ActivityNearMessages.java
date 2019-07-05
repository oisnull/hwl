package com.hwl.beta.ui.near;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.hwl.beta.R;
import com.hwl.beta.databinding.NearActivityMessagesBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.NearCircleMessage;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.beta.ui.near.adp.NearMessageAdapter;

import java.util.List;

public class ActivityNearMessages extends BaseActivity {

    Activity activity;
    NearActivityMessagesBinding binding;
    List<NearCircleMessage> messages;
    NearMessageAdapter messageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        messages = DaoUtils.getNearCircleMessageManagerInstance().getAll();
        binding = DataBindingUtil.setContentView(activity, R.layout.near_activity_messages);

        initView();
    }

    private void initView() {
        binding.tbTitle.setTitle("附近的消息")
                .setTitleRightText("清空")
                .setTitleRightBackground(R.drawable.bg_top)
                .setImageRightHide()
                .setTitleRightShow()
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                })
                .setTitleRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (messages.size() <= 0) return;
                        new AlertDialog.Builder(activity)
                                .setMessage("消息清空后不能恢复,确认清空?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        DaoUtils.getNearCircleMessageManagerInstance().deleteAll();
                                        messages.clear();
                                        messageAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                    }
                });

        messageAdapter = new NearMessageAdapter(activity, messages, new NearMessageItemListener());
        binding.rvMessageContainer.setAdapter(messageAdapter);
        binding.rvMessageContainer.setLayoutManager(new LinearLayoutManager(activity));

        MessageCountSP.setNearCircleMessageCount(0);
        EventBusUtil.sendNearMessageUpdateEvent();
    }

    private class NearMessageItemListener implements NearMessageAdapter.INearMessageItemListener {

        @Override
        public void onItemClick(View v, NearCircleMessage message, int position) {
            UITransfer.toNearDetailActivity(activity, message.getNearCircleId());
        }

        @Override
        public void onItemLongClick(View v, final NearCircleMessage message, final int position) {
            new AlertDialog.Builder(activity)
                    .setMessage("确认要删除这条消息?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (DaoUtils.getNearCircleMessageManagerInstance().deleteMessage(message)) {
                                messages.remove(position);
                                messageAdapter.notifyItemRemoved(position);
                                messageAdapter.notifyItemRangeChanged(position,
                                        messages.size() - position);
                            }
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
    }
}
