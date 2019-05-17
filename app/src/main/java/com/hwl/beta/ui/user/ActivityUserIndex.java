package com.hwl.beta.ui.user;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.hwl.beta.R;
import com.hwl.beta.databinding.UserActivityIndexBinding;
import com.hwl.beta.emotion.widget.EmotionTextView;
import com.hwl.beta.net.user.UserDetailsInfo;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.common.KeyBoardAction;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.dialog.DialogUtils;
import com.hwl.beta.ui.dialog.LoadingDialog;
import com.hwl.beta.ui.ebus.EventBusConstant;
import com.hwl.beta.ui.ebus.EventMessageModel;
import com.hwl.beta.ui.ebus.bean.EventUpdateFriendRemark;
import com.hwl.beta.ui.immsg.IMClientEntry;
import com.hwl.beta.ui.immsg.IMDefaultSendOperateListener;
import com.hwl.beta.ui.user.action.IUserIndexListener;
import com.hwl.beta.ui.user.bean.ImageViewBean;
import com.hwl.beta.ui.user.bean.UserEditItemBean;
import com.hwl.beta.ui.user.bean.UserIndexBean;
import com.hwl.beta.ui.user.logic.UserIndexLogic;
import com.hwl.beta.ui.user.standard.UserIndexStandard;
import com.hwl.beta.utils.DisplayUtils;
import com.hwl.beta.utils.StringUtils;

/**
 * Created by Administrator on 2018/1/9.
 */
public class ActivityUserIndex extends BaseActivity {

    FragmentActivity activity;
    UserActivityIndexBinding binding;
    UserIndexStandard indexStandard;
    UserIndexBean userBean;
    UserIndexListener indexListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        indexStandard = new UserIndexLogic();
        indexListener = new UserIndexListener();

        userBean = indexStandard.getUserInfo(
                getIntent().getLongExtra("userid", 0),
                getIntent().getStringExtra("username"),
                getIntent().getStringExtra("userimage"));
        if (userBean == null) {
            Toast.makeText(activity, "用户不存在", Toast.LENGTH_SHORT).show();
            finish();
        }

        binding = DataBindingUtil.setContentView(activity, R.layout.user_activity_index);
        binding.setAction(indexListener);

        initView();
    }

    private void initView() {
        final View.OnClickListener deleteUserClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userBean.getIdcard() == UserIndexBean.IDCARD_OTHER) {
                    Toast.makeText(activity, "对方还不是你的好友", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (userBean.getUserId() == UserSP.getUserId()) {
                    Toast.makeText(activity, "不能删除自己", Toast.LENGTH_SHORT).show();
                    return;
                }
                DialogUtils.closeUserActionDialog();
                indexListener.onDeleteClick();
            }
        };

        final View.OnClickListener addBlackClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userBean.getUserId() == UserSP.getUserId()) {
                    Toast.makeText(activity, "不能加自己到黑名单", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(activity, "添加黑名单功能稍后开放...", Toast.LENGTH_SHORT).show();
                DialogUtils.closeUserActionDialog();
            }
        };

        binding.tbTitle.setTitle("用户详情")
                .setImageRightResource(R.drawable.v_more)
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                })
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtils.showUserActionDialog(activity, deleteUserClickListener,
                                addBlackClickListener);
                    }
                });

        initData();
        loadServerUserInfo();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEventMessage(EventMessageModel messageModel) {
        if (messageModel.getMessageType() == EventBusConstant.EB_TYPE_FRIEND_UPDATE_REMARK) {
            EventUpdateFriendRemark friendRemark = (EventUpdateFriendRemark) messageModel
                    .getMessageModel();
            if (friendRemark.getFriendId() == userBean.getUserId()) {
                userBean.setRemark(friendRemark.getFriendRemark());
                if (StringUtils.isNotBlank(userBean.getRemark())) {
                    binding.tvShowName.setText(userBean.getRemark());
                    binding.tvUsername.setText(userBean.getUserName());
                    binding.llUsername.setVisibility(View.VISIBLE);
                } else {
                    binding.tvShowName.setText(userBean.getUserName());
                    binding.llUsername.setVisibility(View.GONE);
                }
            }
        }
    }

    private void initData() {
        ImageViewBean.loadImage(binding.ivHeader, userBean.getUserImage());
        if (userBean.getIdcard() == UserIndexBean.IDCARD_FRIEND) {
            if (StringUtils.isNotBlank(userBean.getRemark())) {
                binding.tvShowName.setText(userBean.getRemark());
                binding.tvUsername.setText(userBean.getUserName());
                binding.llUsername.setVisibility(View.VISIBLE);
            } else {
                binding.tvShowName.setText(userBean.getUserName());
                binding.llUsername.setVisibility(View.GONE);
            }
            if (StringUtils.isNotBlank(userBean.getSymbol())) {
                binding.tvSymbol.setText(userBean.getSymbol());
                binding.llSymbol.setVisibility(View.VISIBLE);
            } else {
                binding.llSymbol.setVisibility(View.GONE);
            }
            binding.tvArea.setText(userBean.getRegisterAddress());
            binding.tvNotes.setText(userBean.getUserLifeNotes());

            this.setCircleImages();
            this.setCircleTexts();

            binding.llRemarkSet.setVisibility(View.VISIBLE);
            binding.llArea.setVisibility(View.VISIBLE);
            binding.llNotes.setVisibility(View.VISIBLE);
            binding.llCircles.setVisibility(View.VISIBLE);
            binding.btnSendMessage.setVisibility(View.VISIBLE);
            binding.btnAddFriend.setVisibility(View.GONE);
        } else if (userBean.getIdcard() == UserIndexBean.IDCARD_MINE) {
            binding.tvShowName.setText(userBean.getUserName());
            if (StringUtils.isNotBlank(userBean.getSymbol())) {
                binding.tvSymbol.setText(userBean.getSymbol());
                binding.llSymbol.setVisibility(View.VISIBLE);
            } else {
                binding.llSymbol.setVisibility(View.GONE);
            }
            binding.tvArea.setText(userBean.getRegisterAddress());
            binding.tvNotes.setText(userBean.getUserLifeNotes());

            this.setCircleImages();
            this.setCircleTexts();

            binding.llSymbol.setVisibility(View.VISIBLE);
            binding.llUsername.setVisibility(View.GONE);
            binding.llRemarkSet.setVisibility(View.GONE);
            binding.llArea.setVisibility(View.VISIBLE);
            binding.llNotes.setVisibility(View.VISIBLE);
            binding.llCircles.setVisibility(View.VISIBLE);
            binding.btnSendMessage.setVisibility(View.GONE);
            binding.btnAddFriend.setVisibility(View.GONE);
        } else {
            binding.tvShowName.setText(userBean.getUserName());
            binding.llRemarkSet.setVisibility(View.GONE);
            binding.llUsername.setVisibility(View.GONE);
            binding.llSymbol.setVisibility(View.GONE);
            binding.llArea.setVisibility(View.GONE);
            binding.llNotes.setVisibility(View.GONE);
            binding.llCircles.setVisibility(View.GONE);
            binding.btnSendMessage.setVisibility(View.VISIBLE);
            binding.btnAddFriend.setVisibility(View.VISIBLE);
        }
    }

    private void setCircleImages() {
        if (userBean.getCircleImages() == null || userBean.getCircleImages().size() <= 0) return;

        binding.fblCircleContainer.removeAllViews();
        int size = DisplayUtils.dp2px(activity, 80);
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(size, size);
        params.leftMargin = 10;
        for (int i = 0; i < userBean.getCircleImages().size(); i++) {
            ImageView iv = new ImageView(activity);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageViewBean.loadImage(iv, userBean.getCircleImages().get(i));
            iv.setLayoutParams(params);
            binding.fblCircleContainer.addView(iv);
        }
    }

    private void setCircleTexts() {
        if (userBean.getCircleTexts() == null || userBean.getCircleTexts().size() <= 0) return;

        binding.fblCircleContainer.removeAllViews();
        binding.fblCircleContainer.setFlexWrap(FlexWrap.WRAP);
        binding.fblCircleContainer.setBackgroundColor(getResources().getColor(R.color
                .color_f3f3f3));
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(FlexboxLayout
                .LayoutParams.MATCH_PARENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 5;
        params.rightMargin = 5;
        params.topMargin = 5;
        params.bottomMargin = 5;
        for (int i = 0; i < userBean.getCircleTexts().size(); i++) {
            EmotionTextView tv = new EmotionTextView(activity);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(userBean.getCircleTexts().get(i));
            tv.setLayoutParams(params);
            binding.fblCircleContainer.addView(tv);
        }
    }

    private void loadServerUserInfo() {
        indexStandard.loadServerUserInfo(userBean.getUserId(), userBean.getUpdateTime(), new
                DefaultCallback<UserDetailsInfo, String>() {
                    @Override
                    public void success(UserDetailsInfo info) {
                        if (!userBean.getUserImage().equals(info.getHeadImage())) {
                            ImageViewBean.loadImage(binding.ivHeader, info.getHeadImage());
                        }
                        if (!userBean.getSymbol().equals(info.getSymbol())) {
                            binding.tvSymbol.setText(userBean.getSymbol());
                        }
                        if (!userBean.getUserName().equals(info.getName())) {
                            binding.tvUsername.setText(info.getName());
                        }
                        if (!userBean.getRegisterAddress().equals(info.getCountry())) {
                            binding.tvArea.setText(info.getCountry());
                        }
                    }

                    @Override
                    public void error(String errorMessage) {

                    }
                });
    }

    public class UserIndexListener implements IUserIndexListener {

        @Override
        public void onUserHeadClick() {
            UITransfer.toImageBrowseActivity(activity, userBean.getUserImage());
        }

        @Override
        public void onRemarkClick() {
            UITransfer.toUserEditItemActivity(activity, UserEditItemBean.ACTIONTYPE_REMARK,
                    userBean.getRemark(), userBean.getUserId());
        }

        @Override
        public void onCircleClick() {
            UITransfer.toCircleUserIndexActivity(activity, userBean.getUserId(), userBean.getShowName(), userBean.getUserImage());
        }

        @Override
        public void onAddUserClick(final View view) {
            String title = "TO: " + userBean.getShowName();
            final String content = "我是 " + UserSP.getUserShowName();
            DialogUtils.showAddFriendDialog(activity, title, content, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadingDialog.show(activity, "好友请求发送中...");
                    KeyBoardAction.hideSoftInput(activity);

                    IMClientEntry.sendAddFriendMessage(userBean.getUserId(), content, new
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

        @Override
        public void onSendMessageClick() {
            UITransfer.toChatUserActivity(activity, userBean.getUserId(), userBean.getShowName(),
                    userBean.getUserImage());
        }

        @Override
        public void onDeleteClick() {
            LoadingDialog.show(activity, "好友删除中,请稍后...");
            indexStandard.deleteFriend(userBean.getUserId(), new DefaultCallback() {
                @Override
                public void success(Object successMessage) {
                    LoadingDialog.hide();
                    activity.finish();
                }

                @Override
                public void error(Object errorMessage) {
                    LoadingDialog.hide();
                }
            });
        }

        @Override
        public void onBlackClick() {

        }
    }
}
