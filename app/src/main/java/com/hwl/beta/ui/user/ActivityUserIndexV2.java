package com.hwl.beta.ui.user;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hwl.beta.R;
import com.hwl.beta.databinding.UserActivityIndexV2Binding;
import com.hwl.beta.ui.common.BaseActivity;

/**
 * Created by Administrator on 2019/07/08.
 */
public class ActivityUserIndexV2 extends BaseActivity {

    Activity activity;
    UserActivityIndexV2Binding binding;
    UserIndexStandard indexStandard;
    UserIndexBean currentUser;
    CircleUserIndexAdapter circleAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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

        binding = DataBindingUtil.setContentView(activity, R.layout.user_activity_index_v2);
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
				indexStandard.deleteFriend(userBean.getUserId())
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
						//Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
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

        binding.tbTitle.setTitle("User Details")
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
                        DialogUtils.showUserActionDialog(activity, deleteUserClickListener,addBlackClickListener);
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
						DialogUtils.showAddFriendDialog(activity, title, content, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								LoadingDialog.show(activity, "好友请求发送中...");
								KeyBoardAction.hideSoftInput(activity);

								IMClientEntry.sendAddFriendMessage(currentUser.getUserId(), content, new
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
                });

		binding.ivUserMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
            UITransfer.toChatUserActivity(activity, currentUser.getUserId(), currentUser.getShowName(),
                    currentUser.getUserImage());
                    }
                });

		this.setDisplay();
		this.bindInfo(true);
		this.loadServerUserInfo();
		this.loadCircleInfos();
    }

	private void bindInfo(boolean isReloadHeader){
		if(isReloadHeader)
			ImageViewBean.loadImage(binding.ivHeader, currentUser.getUserImage());

		if (StringUtils.isNotBlank(currentUser.getRemark())) {
			binding.tvRemark.setText(currentUser.getRemark());
            binding.llRemark.setVisibility(View.VISIBLE);
        } else {
            binding.llRemark.setVisibility(View.GONE);
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
	}

	private void setDisplay(){
		if(currentUser.isMe()){
            binding.llRemark.setVisibility(View.GONE);
            binding.tvUsernameDesc.setVisibility(View.GONE);
            binding.llUserAction.setVisibility(View.GONE);
		}else if(currentUser.isFriend()){
            binding.llUserAction.setVisibility(View.VISIBLE);
            binding.isUserAdd.setVisibility(View.GONE);
		}else{
			
		}
	}

    private void loadServerUserInfo() {
		if(currentUser.isMe()) return;

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
                        }else{
							bindInfo(false);
						}
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) {
						//Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
					}
				});
    }

	private void loadCircleInfos(){
        circleAdapter = new CircleUserIndexAdapter(activity, new ICircleUserItemListener() {
			@Override
			public void onItemNullViewClick() {
			}

			@Override
			public void onItemViewClick(Circle info) {
				UITransfer.toCircleDetailActivity(activity, info.getCircleId());
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
						loadServerCircleInfos(infos);
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) {
						loadServerCircleInfos(null);
						//Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
					}
				});
	}

    private void loadServerCircleInfos(List<Circle> localInfos) {
        if (!NetworkUtils.isConnected()) {
            return;
        }

        circleStandard.loadServerCircleInfos(currentUser.getUserId(), localInfos)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Circle>>() {
                    @Override
                    public void accept(List<Circle> infos) {
                        circleAdapter.updateInfos(infos);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        //Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
