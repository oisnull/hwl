package com.hwl.beta.ui.group;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.hwl.beta.R;
import com.hwl.beta.databinding.GroupActivityListBinding;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.ebus.EventBusConstant;
import com.hwl.beta.ui.ebus.EventMessageModel;
import com.hwl.beta.ui.group.adp.GroupAdapter;
import com.hwl.beta.ui.group.logic.GroupLogic;
import com.hwl.beta.ui.group.standard.GroupStandard;

import java.util.List;

public class ActivityGroup extends BaseActivity {

    GroupActivityListBinding binding;
    FragmentActivity activity;
    GroupStandard groupStandard;
    GroupAdapter groupAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        groupStandard = new GroupLogic();

        binding = DataBindingUtil.setContentView(activity, R.layout.group_activity_list);

        initView();
    }

    private void initView() {
        binding.tbTitle.setTitle("我的群组")
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                })
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(activity, "发起群聊天", Toast.LENGTH_SHORT).show();
                        UITransfer.toGroupAddActivity(activity);
                    }
                });

        groupAdapter = new GroupAdapter(activity, groupStandard.getLocalGroups());
        binding.rvGroupContainer.setAdapter(groupAdapter);
        binding.rvGroupContainer.setLayoutManager(new LinearLayoutManager(activity));

        if (groupAdapter.getGroupCount() < UserSP.getGroupCount()) {
            groupStandard.loadServerGroups(groupAdapter.getGroups(), new
                    DefaultCallback<List<GroupInfo>, String>() {
                        @Override
                        public void success(List<GroupInfo> groupInfos) {
                            groupAdapter.addGroups(groupInfos);
                        }

                        @Override
                        public void error(String errorMessage) {
                        }

                        @Override
                        public void relogin() {
                            UITransfer.toReloginDialog(activity);
                        }
                    });
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEventMessage(EventMessageModel messageModel) {
        switch (messageModel.getMessageType()) {
            case EventBusConstant.EB_TYPE_GROUP_ACTION_ADD:
                groupAdapter.addGroup((GroupInfo) messageModel.getMessageModel());
                break;
        }
//        if (actionGroup.getActionType() == EventBusConstant.EB_TYPE_ACTINO_ADD) {
//            groupInfos.add(actionGroup.getGroupInfo());
//            groupAdapter.notifyItemInserted(groupInfos.size() - 1);
//        } else if (actionGroup.getActionType() == EventBusConstant.EB_TYPE_ACTINO_REMOVE ||
// actionGroup.getActionType() == EventBusConstant.EB_TYPE_ACTINO_EXIT) {
//            groupInfos.remove(actionGroup.getGroupInfo());
//            groupAdapter.notifyDataSetChanged();
//        }
    }
}
