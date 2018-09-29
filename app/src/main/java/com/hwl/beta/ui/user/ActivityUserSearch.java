package com.hwl.beta.ui.user;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hwl.beta.R;
import com.hwl.beta.databinding.UserActivitySearchBinding;
import com.hwl.beta.net.user.UserSearchInfo;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.KeyBoardAction;
import com.hwl.beta.ui.dialog.AddFriendDialogFragment;
import com.hwl.beta.ui.dialog.LoadingDialog;
import com.hwl.beta.ui.user.action.IUserSearchItemListener;
import com.hwl.beta.ui.user.action.IUserSearchListener;
import com.hwl.beta.ui.user.adp.UserSearchAdapter;
import com.hwl.beta.ui.user.logic.UserSearchLogic;
import com.hwl.beta.ui.user.standard.UserSearchStandard;
import com.hwl.im.common.DefaultConsumer;

import java.util.List;

/**
 * Created by Administrator on 2018/1/27.
 */

public class ActivityUserSearch extends BaseActivity {
    Activity activity;
    UserActivitySearchBinding binding;
    UserSearchAdapter userAdapter;
    UserSearchStandard searchStandard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        searchStandard = new UserSearchLogic();
        userAdapter = new UserSearchAdapter(this, new UserSearchItemListener());

        binding = DataBindingUtil.setContentView(this, R.layout.user_activity_search);
        binding.setAction(new UserSearchListener());
        binding.setSearchAdapter(userAdapter);

        initView();
    }

    public void initView() {
        binding.pbLoading.setVisibility(View.GONE);
        binding.tvShow.setVisibility(View.GONE);
    }

    public class UserSearchListener implements IUserSearchListener {

        private boolean isRuning = false;

        @Override
        public void onBackClick() {
            onBackPressed();
            finish();
        }

        @Override
        public void onSearchClick() {
            if (isRuning) {
                return;
            }
            isRuning = true;

            KeyBoardAction.hideSoftInput(activity);
            binding.pbLoading.setVisibility(View.VISIBLE);

            searchStandard.searchUsers(binding.etUserKey.getText() + "", new DefaultConsumer<List
                    <UserSearchInfo>>() {
                @Override
                public void accept(List<UserSearchInfo> users) {
                    isRuning = false;
                    userAdapter.clearAndAddUsers(users);
                    binding.pbLoading.setVisibility(View.GONE);
                    binding.tvShow.setVisibility((users != null && users.size() > 0) ? View.GONE
                            : View.VISIBLE);
                }
            }, new DefaultConsumer<String>() {
                @Override
                public void accept(String s) {
                    isRuning = false;
                    binding.tvShow.setVisibility(View.GONE);
                    binding.pbLoading.setVisibility(View.GONE);
                }
            });
        }
    }

    public class UserSearchItemListener implements IUserSearchItemListener {
        private AddFriendDialogFragment addFriendDialogFragment;

        @Override
        public void onHeadImageClick(UserSearchInfo user) {

        }

        //用户添加好友需要发送一条请求验证的消息
        //好友验证通过需要请求net api，将添加成功的好友数据存储到数据库中
        //只有当好友验证通过了，再发送一条验证通过的消息到自己
        @Override
        public void onAddClick(final View view, final UserSearchInfo user) {
            if (addFriendDialogFragment == null) {
                addFriendDialogFragment = new AddFriendDialogFragment();
            }
            final String remark = "我是 " + UserSP.getUserShowName();
            addFriendDialogFragment.setRemark(remark);
            addFriendDialogFragment.setTitle(user.getShowName());
            addFriendDialogFragment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    LoadingDialog.show(activity, "请求发送中...");
                    KeyBoardAction.hideSoftInput(activity);

//                    UserMessageSend.sendFriendRequestMessage(user.getId(), remark).subscribe
// (new MQDefaultObserver() {
//                        @Override
//                        protected void onSuccess() {
//                            view.setVisibility(View.GONE);
//                            Toast.makeText(activity, "好友请求发送成功", Toast.LENGTH_SHORT).show();
//                            addFriendDialogFragment.dismiss();
//                            LoadingDialog.hide();
//                        }
//
//                        @Override
//                        protected void onError(String resultMessage) {
//                            super.onError(resultMessage);
//                            LoadingDialog.hide();
//                        }
//                    });
                }
            });
            addFriendDialogFragment.show(getFragmentManager(), "AddFriendDialogFragment");
        }
    }
}
