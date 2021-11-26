package com.hwl.beta.ui.user;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.view.View;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.UserActivityNewFriendBinding;
import com.hwl.beta.db.entity.FriendRequest;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.common.rxext.RXDefaultObserverEmpty;
import com.hwl.beta.ui.dialog.LoadingDialog;
import com.hwl.beta.ui.user.action.INewFriendItemListener;
import com.hwl.beta.ui.user.adp.NewFriendAdapter;
import com.hwl.beta.ui.user.logic.NewFriendLogic;
import com.hwl.beta.ui.user.standard.NewFriendStandard;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2018/1/8.
 */

public class ActivityNewFriend extends BaseActivity {

    FragmentActivity activity;
    NewFriendStandard friendStandard;
    NewFriendAdapter friendAdapter;
    UserActivityNewFriendBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        friendStandard = new NewFriendLogic();

        friendAdapter = new NewFriendAdapter(activity, friendStandard.getFriendRequestInfos(),
                new NewFriendItemListener());
        binding = UserActivityNewFriendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    private void initView() {
        binding.tbTitle
                .setTitle("好友请求列表")
                .setImageRightHide()
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
    }

    public class NewFriendItemListener implements INewFriendItemListener {

        @Override
        public void onHeadImageClick(FriendRequest friendRequest) {

        }

        @Override
        public void onRemarkClick(String remark) {
            Toast.makeText(activity, remark, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancelClick(View view, final FriendRequest friendRequest) {
            new AlertDialog.Builder(activity)
                    .setMessage("好友请求删除后,不能恢复,确认删除 ?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            friendAdapter.removeInfo(friendRequest);
                            friendStandard.deleteFriendRequest(friendRequest);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }

        @Override
        public void onAddClick(View view, final FriendRequest friendRequest) {
            LoadingDialog.show(activity);
            friendStandard.addFriend(friendRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RXDefaultObserverEmpty() {
                    @Override
                    protected void onSuccess() {
						LoadingDialog.hide();
						Toast.makeText(activity, "添加好友成功", Toast.LENGTH_SHORT).show();
						friendAdapter.removeInfo(friendRequest);
                    }

                    @Override
                    protected void onError(String message) {
                        super.onError(message);
						LoadingDialog.hide();
                    }

					@Override
                    public void onRelogin() {
						LoadingDialog.hide();
						UITransfer.toReloginDialog(activity);
                    }
                });
        }
    }
}
