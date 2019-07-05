package com.hwl.beta.ui.circle;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.hwl.beta.R;
import com.hwl.beta.databinding.CircleActivityMessagesBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.CircleMessage;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.ui.circle.adp.CircleMessageAdapter;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.ebus.EventBusUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ActivityCircleMessages extends BaseActivity {

    FragmentActivity activity;
    CircleActivityMessagesBinding binding;
    List<CircleMessage> messages;
    CircleMessageAdapter messageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        messages = DaoUtils.getCircleMessageManagerInstance().getAll();
        binding = DataBindingUtil.setContentView(activity, R.layout.circle_activity_messages);

        initView();
    }

    private void initView() {
        binding.tbTitle.setTitle("我的朋友圈消息")
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
                        //Toast.makeText(activity, "清空消息功能稍后开放...", Toast.LENGTH_SHORT).show();
                        if (messages.size() <= 0) return;
                        new AlertDialog.Builder(activity)
                                .setMessage("消息清空后不能恢复,确认清空?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        DaoUtils.getCircleMessageManagerInstance().deleteAll();
                                        messages.clear();
                                        messageAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                    }
                });
        messageAdapter = new CircleMessageAdapter(activity, messages, new MessageItemListener());
        binding.rvMessageContainer.setAdapter(messageAdapter);
        binding.rvMessageContainer.setLayoutManager(new LinearLayoutManager(activity));

        MessageCountSP.setCircleMessageCount(0);
        EventBusUtil.sendCircleMessageUpdateEvent();
    }

    private class MessageItemListener implements CircleMessageAdapter.IMessageItemListener {

        @Override
        public void onItemClick(View v, CircleMessage message, int position) {
//            UITransfer.toCircleDetailActivity(activity, message.getCircleId(), null);
        }

        @Override
        public void onItemLongClick(View v, final CircleMessage message, final int position) {
            new AlertDialog.Builder(activity)
                    .setMessage("确认要删除这条消息?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (DaoUtils.getCircleMessageManagerInstance().deleteMessage(message)) {
                                messages.remove(position);
                                messageAdapter.notifyItemRemoved(position);
                                messageAdapter.notifyItemRangeChanged(position, messages.size() - position);
                            }
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
    }
}
