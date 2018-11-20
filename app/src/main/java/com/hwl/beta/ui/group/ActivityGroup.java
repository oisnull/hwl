package com.hwl.beta.ui.group;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.GroupActivityListBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.net.group.GroupService;
import com.hwl.beta.net.group.body.GetGroupsResponse;
import com.hwl.beta.net.user.NetGroupUserInfo;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.convert.DBGroupAction;
import com.hwl.beta.ui.group.adp.GroupAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActivityGroup extends BaseActivity {

    GroupActivityListBinding binding;
    Activity activity;
    List<GroupInfo> groupInfos;
    GroupAdapter groupAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        groupInfos = this.getGroupInfos();
        groupAdapter = new GroupAdapter(activity, groupInfos);
        binding = DataBindingUtil.setContentView(activity, R.layout.group_activity_list);

        initView();
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateGroupInfo(EventActionGroup actionGroup) {
//        if (actionGroup == null || actionGroup.getGroupInfo() == null) return;
//        if (actionGroup.getActionType() == EventBusConstant.EB_TYPE_ACTINO_ADD) {
//            groupInfos.add(actionGroup.getGroupInfo());
//            groupAdapter.notifyItemInserted(groupInfos.size() - 1);
//        } else if (actionGroup.getActionType() == EventBusConstant.EB_TYPE_ACTINO_REMOVE ||
// actionGroup.getActionType() == EventBusConstant.EB_TYPE_ACTINO_EXIT) {
//            groupInfos.remove(actionGroup.getGroupInfo());
//            groupAdapter.notifyDataSetChanged();
//        }
//    }

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
//                        UITransfer.toGroupAddActivity(activity);
                    }
                });
        binding.rvGroupContainer.setAdapter(groupAdapter);
        binding.rvGroupContainer.setLayoutManager(new LinearLayoutManager(activity));

        loadGroupFromServer();
    }

    private void loadGroupFromServer() {
        if (groupInfos.size() > 2) return;

        GroupService.getGroups()
                .subscribe(new NetDefaultObserver<GetGroupsResponse>(false) {
                    @Override
                    protected void onSuccess(GetGroupsResponse response) {
                        if (response.getGroupInfos() != null && response.getGroupInfos().size() >
                                0) {
                            List<GroupInfo> groups = DBGroupAction.convertToGroupInfos(response
                                    .getGroupInfos());
                            DaoUtils.getGroupInfoManagerInstance().addList(groups);
                            groupInfos.addAll(groups);
                            groupAdapter.notifyDataSetChanged();
                            List<NetGroupUserInfo> groupUserInfos = new ArrayList<>();
                            for (int i = 0; i < response.getGroupInfos().size(); i++) {
                                groupUserInfos.addAll(response.getGroupInfos().get(i)
                                        .getGroupUsers());
                            }
                            DaoUtils.getGroupUserInfoManagerInstance().addListAsync(DBGroupAction
                                    .convertToGroupUserInfos(groupUserInfos));
                        }
                    }
                });
    }

    public List<GroupInfo> getGroupInfos() {
        groupInfos = DaoUtils.getGroupInfoManagerInstance().getAll();
        if (groupInfos == null) {
            groupInfos = new ArrayList<>();
        }
        groupInfos.add(0, new GroupInfo());

        return groupInfos;
    }
}
