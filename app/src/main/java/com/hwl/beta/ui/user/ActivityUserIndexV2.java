package com.hwl.beta.ui.user;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.UserActivityIndexV2Binding;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.circle.action.ICircleUserItemListener;
import com.hwl.beta.ui.circle.adp.CircleUserIndexAdapter;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.KeyBoardAction;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.dialog.DialogUtils;
import com.hwl.beta.ui.dialog.LoadingDialog;
import com.hwl.beta.ui.ebus.EventBusConstant;
import com.hwl.beta.ui.ebus.EventMessageModel;
import com.hwl.beta.ui.ebus.bean.EventUpdateFriendRemark;
import com.hwl.beta.ui.immsg.IMClientEntry;
import com.hwl.beta.ui.immsg.IMDefaultSendOperateListener;
import com.hwl.beta.ui.user.bean.UserEditItemBean;
import com.hwl.beta.ui.user.bean.UserIndexBean;
import com.hwl.beta.ui.user.logic.UserIndexLogic;
import com.hwl.beta.ui.user.standard.UserIndexStandard;
import com.hwl.beta.utils.NetworkUtils;
import com.hwl.beta.utils.StringUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class ActivityUserIndexV2 extends BaseActivity {

    FragmentActivity activity;
    UserActivityIndexV2Binding binding;
    UserIndexStandard indexStandard;
    UserIndexBean currentUser;
    CircleUserIndexAdapter circleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        indexStandard = new UserIndexLogic();
        currentUser = indexStandard.getUserInfo(
                getIntent().getLongExtra("userid", 0),
                getIntent().getStringExtra("username"),
                getIntent().getStringExtra("userimage"));
        if (currentUser == null) {
            Toast.makeText(activity, "The user is not exists.", Toast.LENGTH_SHORT).show();
            finish();
        }

        binding = UserActivityIndexV2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }


    private void initView() {
        final View.OnClickListener deleteUserClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentUser.isFriend()) {
                    Toast.makeText(activity, "对方还不是你的好友", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (currentUser.isMe()) {
                    Toast.makeText(activity, "不能删除自己", Toast.LENGTH_SHORT).show();
                    return;
                }
                DialogUtils.closeUserActionDialog();

                LoadingDialog.show(activity, "好友删除中,请稍后...");
                indexStandard.deleteFriend(currentUser.getUserId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer() {
                            @Override
                            public void accept(Object o) {
                                LoadingDialog.hide();
                                activity.finish();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                LoadingDialog.hide();
                                //Toast.makeText(activity, throwable.getMessage(), Toast
                                // .LENGTH_SHORT).show();
                            }
                        });
            }
        };

        final View.OnClickListener addBlackClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser.isMe()) {
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

        binding.ivHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UITransfer.toImageBrowseActivity(activity, currentUser.getUserImage());
            }
        });

        binding.ivRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UITransfer.toUserEditItemActivity(activity, UserEditItemBean.ACTIONTYPE_REMARK,
                        currentUser.getRemark(), currentUser.getUserId());
            }
        });

        binding.tvCircleMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UITransfer.toCircleUserIndexActivity(activity, currentUser.getUserId(),
                        currentUser.getUserName(), currentUser.getUserImage());
            }
        });

        binding.ivRemark2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UITransfer.toUserEditItemActivity(activity, UserEditItemBean.ACTIONTYPE_REMARK,
                        currentUser.getRemark(), currentUser.getUserId());
            }
        });

        binding.ivUserAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String title = "TO: " + currentUser.getShowName();
                final String content = "我是 " + UserSP.getUserShowName();
                DialogUtils.showAddFriendDialog(activity, title, content,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LoadingDialog.show(activity, "好友请求发送中...");
                                KeyBoardAction.hideSoftInput(activity);

                                IMClientEntry.sendAddFriendMessage(currentUser.getUserId(),
                                        content, new
                                                IMDefaultSendOperateListener("AddFriend", true) {
                                                    @Override
                                                    public void success1() {
                                                        view.setVisibility(View.GONE);
                                                        Toast.makeText(activity, "好友请求发送成功",
                                                                Toast.LENGTH_SHORT).show();
                                                        DialogUtils.closeAddFriendDialog();
                                                        LoadingDialog.hide();
                                                    }

                                                    @Override
                                                    public void failed1() {
                                                        LoadingDialog.hide();
                                                        Toast.makeText(activity, "好友请求发送失败",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                            }
                        });
            }
        });

        binding.ivUserMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UITransfer.toChatUserActivity(activity, currentUser.getUserId(),
                        currentUser.getShowName(),
                        currentUser.getUserImage());
            }
        });

        this.bindInfo(true);
        this.loadServerUserInfo();

        if (currentUser.isMe() || currentUser.isFriend())
            this.loadCircleInfos();
    }

    private void bindInfo(boolean isReloadHeader) {
//        if (isReloadHeader)
//            ImageViewBean.loadImage(binding.ivHeader, currentUser.getUserImage());

        if (StringUtils.isNotBlank(currentUser.getRemark())) {
            binding.tvRemark.setText(currentUser.getRemark());
            binding.llRemark.setVisibility(View.VISIBLE);
            binding.ivRemark2.setVisibility(View.GONE);
        } else {
            binding.llRemark.setVisibility(View.GONE);
            binding.ivRemark2.setVisibility(View.VISIBLE);
        }

        if (StringUtils.isNotBlank(currentUser.getUserName())) {
            binding.tvUsername.setText(currentUser.getUserName());
            binding.llUsername.setVisibility(View.VISIBLE);
        } else {
            binding.llUsername.setVisibility(View.GONE);
        }

        if (StringUtils.isNotBlank(currentUser.getSymbol())) {
            binding.tvSymbol.setText(currentUser.getSymbol());
            binding.llSymbol.setVisibility(View.VISIBLE);
        } else {
            binding.llSymbol.setVisibility(View.GONE);
        }

        binding.tvNotes.setText(currentUser.getUserLifeNotes());
        this.setDisplay();
    }

    private void setDisplay() {
        if (currentUser.isMe()) {
            binding.llRemark.setVisibility(View.GONE);
            binding.ivRemark2.setVisibility(View.GONE);
            binding.llUsername.setVisibility(View.VISIBLE);
            binding.llSymbol.setVisibility(View.VISIBLE);
//            binding.tvUsernameDesc.setVisibility(View.GONE);
            binding.llUserAction.setVisibility(View.GONE);
        } else if (currentUser.isFriend()) {
            binding.llUserAction.setVisibility(View.VISIBLE);
            binding.ivUserAdd.setVisibility(View.GONE);
        } else {
            binding.llUsername.setVisibility(View.VISIBLE);
            binding.llRemark.setVisibility(View.GONE);
            binding.ivRemark.setVisibility(View.GONE);
            binding.ivRemark2.setVisibility(View.GONE);
        }
    }

    private void loadServerUserInfo() {
        if (currentUser.isMe()) return;

        indexStandard.loadServerUserInfo(currentUser.getUserId(), currentUser.getUpdateTime())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Friend>() {
                    @Override
                    public void accept(Friend info) {
                        if (currentUser.getSymbol() != (info.getSymbol())) {
                            currentUser.setSymbol(info.getSymbol());
                        }
                        if (currentUser.getUserName() != (info.getName())) {
                            currentUser.setUserName(info.getName());
                        }
                        if (currentUser.getRemark() != (info.getRemark())) {
                            currentUser.setRemark(info.getRemark());
                        }
                        if (currentUser.getUserImage() != (info.getHeadImage())) {
                            currentUser.setUserImage(info.getHeadImage());
                            bindInfo(true);
                        } else {
                            bindInfo(false);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        //Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT)
                        // .show();
                    }
                });
    }

    private void loadCircleInfos() {
        circleAdapter = new CircleUserIndexAdapter(activity, new ICircleUserItemListener() {
            @Override
            public void onItemNullViewClick() {
            }

            @Override
            public void onItemViewClick(Circle info) {
                UITransfer.toCircleDetailActivity(activity, info.getCircleId());
            }

            @Override
            public void onBackImageClick() {

            }
        });
        binding.rvCircleContainer.setAdapter(circleAdapter);
        binding.rvCircleContainer.setLayoutManager(new LinearLayoutManager(activity));

        indexStandard.loadLocalCircleInfos(currentUser.getUserId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Circle>>() {
                    @Override
                    public void accept(List<Circle> infos) {
                        circleAdapter.addInfos(infos);
                        if (circleAdapter.getItemCount() > 0)
                            binding.tvCircleTitle.setVisibility(View.VISIBLE);
                        loadServerCircleInfos(infos);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        loadServerCircleInfos(null);
                        //Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT)
                        // .show();
                    }
                });
    }

    private void loadServerCircleInfos(List<Circle> localInfos) {
        if (!NetworkUtils.isConnected()) {
            return;
        }

        indexStandard.loadServerCircleInfos(currentUser.getUserId(), localInfos)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Circle>>() {
                    @Override
                    public void accept(List<Circle> infos) {
                        circleAdapter.updateInfos(infos);
                        if (circleAdapter.getItemCount() > 0)
                            binding.tvCircleTitle.setVisibility(View.VISIBLE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        //Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT)
                        // .show();
                    }
                });
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
            if (friendRemark.getFriendId() == currentUser.getUserId()) {
                currentUser.setRemark(friendRemark.getFriendRemark());
                bindInfo(false);
            }
        }
    }
}
