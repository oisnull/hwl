package com.hwl.beta.ui.user;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RadioButton;

import com.hwl.beta.R;
import com.hwl.beta.databinding.UserActivityEditItemBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.SetFriendRemarkResponse;
import com.hwl.beta.net.user.body.SetUserInfoResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.rxext.RXDefaultObserver;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.KeyBoardAction;
import com.hwl.beta.ui.convert.SexAction;
import com.hwl.beta.ui.dialog.LoadingDialog;
import com.hwl.beta.ui.user.action.IUserEditItemListener;
import com.hwl.beta.ui.user.bean.UserEditItemBean;

/**
 * Created by Administrator on 2018/1/26.
 */

public class ActivityUserEditItem extends BaseActivity {

    UserActivityEditItemBinding binding;
    Activity activity;
    UserEditItemBean itemBean;
    UserEditItemListener itemListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        itemBean = new UserEditItemBean(
                getIntent().getIntExtra("actiontype", 0),
                getIntent().getStringExtra("editcontent"));
        itemBean.setFriendId(getIntent().getLongExtra("friendid", 0));
        itemListener = new UserEditItemListener();
        binding = DataBindingUtil.setContentView(this, R.layout.user_activity_edit_item);
        binding.setUser(itemBean);
        binding.setAction(itemListener);

        binding.tbTitle.setTitle(getActionName())
                .setTitleRightShow()
                .setTitleRightText("提交")
                .setImageRightHide()
                .setTitleRightBackground(R.drawable.bg_top)
                .setTitleRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        KeyBoardAction.hideSoftInput(activity);
                        LoadingDialog.show(activity);

                        switch (itemBean.getActionType()) {
                            case UserEditItemBean.ACTIONTYPE_SYMBOL:
                                UserService.setUserSymbol(itemBean.getEditContent()).subscribe
                                        (defaultObserver);
                                break;
                            case UserEditItemBean.ACTIONTYPE_NAME:
                                UserService.setUserName(itemBean.getEditContent()).subscribe
                                        (defaultObserver);
                                break;
                            case UserEditItemBean.ACTIONTYPE_LIFENOTES:
                                UserService.setUserLifeNotes(itemBean.getEditContent()).subscribe
                                        (defaultObserver);
                                break;
                            case UserEditItemBean.ACTIONTYPE_SEX:
                                UserService.setUserSex(SexAction.getSex(itemBean.getEditContent()
                                )).subscribe(defaultObserver);
                                break;
                            case UserEditItemBean.ACTIONTYPE_REMARK:
                                UserService.setFriendRemark(itemBean.getFriendId(), itemBean
                                        .getEditContent()).subscribe(friendRemarkObserver);
                                break;
                        }
                    }
                })
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        itemListener.init();
    }

    private String getActionName() {
        switch (itemBean.getActionType()) {
            case UserEditItemBean.ACTIONTYPE_SYMBOL:
                return "标识号设置";
            case UserEditItemBean.ACTIONTYPE_NAME:
                return "昵称设置";
            case UserEditItemBean.ACTIONTYPE_LIFENOTES:
                return "个性签名设置";
            case UserEditItemBean.ACTIONTYPE_SEX:
                return "性别设置";
            case UserEditItemBean.ACTIONTYPE_REMARK:
                return "好友备注设置";
        }
        return "个人信息设置";
    }

    private RXDefaultObserver<SetUserInfoResponse> defaultObserver = new
            RXDefaultObserver<SetUserInfoResponse>() {
                @Override
                protected void onSuccess(SetUserInfoResponse response) {
                    if (response != null && response.getStatus() == NetConstant.RESULT_SUCCESS) {
                        switch (itemBean.getActionType()) {
                            case UserEditItemBean.ACTIONTYPE_SYMBOL:
                                UserSP.setUserSymbol(itemBean.getEditContent());
                                EventBusUtil.sendUserSymbolEditEvent(itemBean.getEditContent());
                                break;
                            case UserEditItemBean.ACTIONTYPE_NAME:
                                UserSP.setUserName(itemBean.getEditContent());
                                EventBusUtil.sendUserNameEditEvent(itemBean.getEditContent());
                                break;
                            case UserEditItemBean.ACTIONTYPE_LIFENOTES:
                                UserSP.setUserLifeNotes(itemBean.getEditContent());
                                EventBusUtil.sendUserLifeNotesEditEvent(itemBean.getEditContent());
                                break;
                            case UserEditItemBean.ACTIONTYPE_SEX:
                                UserSP.setUserSex(SexAction.getSex(itemBean.getEditContent()));
                                EventBusUtil.sendUserSexEditEvent(SexAction.getSex(itemBean
                                        .getEditContent()));
                                break;

                        }
                        finish();
                        LoadingDialog.hide();
                    }
                }

                @Override
                protected void onError(String resultMessage) {
                    super.onError(resultMessage);
                    LoadingDialog.hide();
                }
            };

    private RXDefaultObserver<SetFriendRemarkResponse> friendRemarkObserver = new
            RXDefaultObserver<SetFriendRemarkResponse>() {
                @Override
                protected void onSuccess(SetFriendRemarkResponse response) {
                    Friend friend = DaoUtils.getFriendManagerInstance().updateRemark(itemBean.getFriendId(),
                            itemBean.getEditContent());
                     DaoUtils.getChatRecordMessageManagerInstance()
                            .updateUserRecordTitle(UserSP
                            .getUserId(), friend.getId(), friend
                            .getShowName());
                    EventBusUtil.sendFriendUpdateRemarkEvent(itemBean.getFriendId(), itemBean
                            .getEditContent());
//                    Friend friend = DaoUtils.getFriendManagerInstance().get(itemBean
// .getFriendId());
//                    if (friend != null && friend.getId() > 0) {
//                        friend.setRemark(itemBean.getEditContent());
//                        DaoUtils.getFriendManagerInstance().save(friend);
//                        EventBus.getDefault().post(new EventUpdateFriendRemark(friend.getId(),
//                                friend
//                                        .getFirstLetter(), friend.getRemark()));
//                        ChatRecordMessage record = DaoUtils.getChatRecordMessageManagerInstance()
//                                .updateUserRecrdTitle(UserSP.getUserId(), friend.getId(), friend
//                                        .getRemark(), friend.getHeadImage());
//                        if (record != null) EventBus.getDefault().post(record);
//                    }
                    finish();
                    LoadingDialog.hide();
                }

                @Override
                protected void onError(String resultMessage) {
                    super.onError(resultMessage);
                    LoadingDialog.hide();
                }
            };

    public class UserEditItemListener implements IUserEditItemListener {

        private void setUnSelect(RadioButton rb) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_unselected);
            drawable.setBounds(0, 0, 40, 40);
            rb.setCompoundDrawables(null, null, drawable, null);
        }

        private void setSelect(RadioButton rb) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_selected);
            drawable.setBounds(0, 0, 40, 40);
            rb.setCompoundDrawables(null, null, drawable, null);
        }

        @Override
        public void init() {
            if (SexAction.MAN.equals(itemBean.getEditContent())) {
                setSelect(binding.rbSex1);
                setUnSelect(binding.rbSex0);
            } else if (SexAction.WOMAN.equals(itemBean.getEditContent())) {
                setSelect(binding.rbSex0);
                setUnSelect(binding.rbSex1);
            } else {
                setUnSelect(binding.rbSex0);
                setUnSelect(binding.rbSex1);
            }
        }

        @Override
        public void onManClick(String sexName) {
            setSelect(binding.rbSex1);
            setUnSelect(binding.rbSex0);
            itemBean.setEditContent(sexName);
        }

        @Override
        public void onWomanClick(String sexName) {
            setSelect(binding.rbSex0);
            setUnSelect(binding.rbSex1);
            itemBean.setEditContent(sexName);
        }
    }
}
