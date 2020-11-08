package com.hwl.beta.ui.group;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hwl.beta.R;
import com.hwl.beta.databinding.GroupActivityAllUsersBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.group.adp.GroupUserAdapter;

import java.util.List;

public class ActivityGroupAllUsers extends BaseActivity {

    GroupActivityAllUsersBinding binding;
    FragmentActivity activity;
    String groupGuid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        groupGuid = getIntent().getStringExtra("groupguid");
        binding = DataBindingUtil.setContentView(activity, R.layout.group_activity_all_users);

        initView();
    }

    private void initView() {
        GroupInfo group = DaoUtils.getGroupInfoManagerInstance().get(groupGuid);
        binding.tbTitle.setTitle(group.getGroupName())
                .setImageLeftClick(v -> onBackPressed())
                .setImageRightClick(v -> UITransfer.toGroupAddActivity(activity));

        List<GroupUserInfo> users = DaoUtils.getGroupUserInfoManagerInstance().getUsers(groupGuid);
        binding.rvUsersContainer.setAdapter(new GroupUserAdapter(activity, users));
        binding.rvUsersContainer.setLayoutManager(new LinearLayoutManager(activity));
    }
}
