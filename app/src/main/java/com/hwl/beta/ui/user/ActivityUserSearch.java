package com.hwl.beta.ui.user;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.view.View;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.UserActivitySearchBinding;
import com.hwl.beta.net.user.UserSearchInfo;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.KeyBoardAction;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.common.rxext.RXDefaultObserver;
import com.hwl.beta.ui.dialog.DialogUtils;
import com.hwl.beta.ui.dialog.LoadingDialog;
import com.hwl.beta.ui.immsg.IMClientEntry;
import com.hwl.beta.ui.immsg.IMDefaultSendOperateListener;
import com.hwl.beta.ui.user.action.IUserSearchItemListener;
import com.hwl.beta.ui.user.action.IUserSearchListener;
import com.hwl.beta.ui.user.adp.UserSearchAdapter;
import com.hwl.beta.ui.user.logic.UserSearchLogic;
import com.hwl.beta.ui.user.standard.UserSearchStandard;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2018/1/27.
 */

public class ActivityUserSearch extends BaseActivity {
    FragmentActivity activity;
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

			searchStandard.searchUsers(binding.etUserKey.getText() + "")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RXDefaultObserver<List<UserSearchInfo>>() {
                        @Override
                        protected void onSuccess(List<UserSearchInfo> users) {
                            isRuning = false;
                            userAdapter.clearAndAddUsers(users);
                            binding.pbLoading.setVisibility(View.GONE);
                            binding.tvShow.setVisibility((users != null && users.size() > 0) ? View.GONE: View.VISIBLE);
                        }

                        @Override
                        protected void onError(String message) {
                            super.onError(message);
                            isRuning = false;
                            binding.tvShow.setVisibility(View.GONE);
                            binding.pbLoading.setVisibility(View.GONE);
                        }

						@Override
                        public void onRelogin() {
                            UITransfer.toReloginDialog(activity);
                        }
                    });
        }
    }

    public class UserSearchItemListener implements IUserSearchItemListener {
        @Override
        public void onHeadImageClick(UserSearchInfo user) {

        }

        //用户添加好友需要发送一条请求验证的消息
        //好友验证通过需要请求net api，将添加成功的好友数据存储到数据库中
        //只有当好友验证通过了，再发送一条验证通过的消息到自己
        @Override
        public void onAddClick(final View view, final UserSearchInfo user) {
            String title = "TO: " + user.getShowName();
            final String content = "我是 " + UserSP.getUserShowName();
            DialogUtils.showAddFriendDialog(activity, title, content, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadingDialog.show(activity, "好友请求发送中...");
                    KeyBoardAction.hideSoftInput(activity);

                    IMClientEntry.sendAddFriendMessage(user.getId(), content, new
                            IMDefaultSendOperateListener("AddFriend", true) {
                                @Override
                                public void success1() {
                                    view.setVisibility(View.GONE);
                                    Toast.makeText(activity, "好友请求发送成功", Toast.LENGTH_SHORT).show();
                                    DialogUtils.closeAddFriendDialog();
                                    LoadingDialog.hide();
                                }

                                @Override
                                public void failed1() {
                                    LoadingDialog.hide();
                                    Toast.makeText(activity, "好友请求发送失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        }
    }
}
