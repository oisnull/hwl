package com.hwl.beta.ui.circle;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.CircleActivityIndexBinding;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.db.entity.CircleImage;
import com.hwl.beta.db.entity.CircleLike;
import com.hwl.beta.emotion.EmotionDefaultPanelV2;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.circle.action.ICircleItemListener;
import com.hwl.beta.ui.circle.adp.CircleIndexAdapter;
import com.hwl.beta.ui.circle.logic.CircleLogic;
import com.hwl.beta.ui.circle.standard.CircleStandard;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.ClipboardAction;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.dialog.LoadingDialog;
import com.hwl.beta.ui.ebus.EventBusConstant;
import com.hwl.beta.ui.ebus.EventMessageModel;
import com.hwl.beta.ui.imgselect.ActivityImageBrowse;
import com.hwl.beta.ui.imgselect.bean.ImageSelectType;
import com.hwl.beta.ui.widget.CircleActionMorePop;
import com.hwl.beta.utils.NetworkUtils;
import com.hwl.beta.utils.StringUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class ActivityCircleIndex extends BaseActivity {

    Activity activity;
    CircleActivityIndexBinding binding;
    CircleStandard circleStandard;
    CircleIndexAdapter circleAdapter;
    EmotionPanelListener emotionPanelListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        circleStandard = new CircleLogic();
        binding = DataBindingUtil.setContentView(activity, R.layout.circle_activity_index);
        initView();
    }

    private void initView() {
        binding.tbTitle.setTitle("我的圈子")
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.widget.PopupMenu popup = new android.widget.PopupMenu(activity, v);
                        popup.getMenuInflater().inflate(R.menu.popup_circle_menu, popup.getMenu());
                        popup.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.pop_circle_publish:
                                        UITransfer.toCirclePublishActivity(activity);
                                        break;
                                    case R.id.pop_circle_messages:
                                        UITransfer.toCircleMessagesActivity(activity);
                                        break;
                                }
                                return true;
                            }
                        });
                        popup.show();
                    }
                })
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        circleAdapter = new CircleIndexAdapter(activity, new CircleItemListener());
        binding.rvCircleContainer.setAdapter(circleAdapter);
        binding.rvCircleContainer.setLayoutManager(new LinearLayoutManager(activity));
        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadServerInfos(0);
            }
        });
        binding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                loadServerInfos(circleAdapter.getMinId());
            }
        });

        emotionPanelListener = new EmotionPanelListener();
        binding.refreshLayout.setEnableLoadMore(false);
        binding.edpEmotion.setPanelVisibility(View.GONE);
        binding.edpEmotion.setPanelListener(emotionPanelListener);

        if (MessageCountSP.getCircleMessageCount() > 0) {
            circleAdapter.updateMsgcount();
        }

        this.loadLocalInfos();
    }

    private void loadLocalInfos() {
        circleStandard.loadLocalInfos()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Circle>>() {
                    @Override
                    public void accept(List<Circle> infos) {
                        circleAdapter.updateInfos(infos);
                        loadServerInfos(0);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadServerInfos(long infoId) {
        if (!NetworkUtils.isConnected()) {
            showResult();
            return;
        }

        circleStandard.loadServerInfos(infoId, circleAdapter.getInfos())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Circle>>() {
                    @Override
                    public void accept(List<Circle> infos) {
                        circleAdapter.updateInfos(infos);
                        showResult();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        showResult();
                        Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showResult() {
        if (circleAdapter.getItemCount() <= 0) {
            circleAdapter.setEmptyInfo();
            binding.refreshLayout.setEnableLoadMore(false);
        }
        binding.refreshLayout.finishRefresh();
        binding.refreshLayout.finishLoadMore();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {

            LoadingDialog.show(activity, "正在上传...");
            circleStandard.updateCircleBackImage(data.getStringExtra("localpath"))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String img) {
                            LoadingDialog.hide();
                            circleAdapter.notifyItemChanged(0);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            LoadingDialog.hide();
                            Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
        if (messageModel.getMessageType() == EventBusConstant.EB_TYPE_CIRCLE_MESSAGE_UPDATE) {
            circleAdapter.updateMsgcount();
        }
    }

    public void setEmotionStatus(boolean isShow) {
        setEmotionStatus(isShow, null);
    }

    public void setEmotionStatus(boolean isShow, String hintText) {
        if (isShow) {
            binding.edpEmotion.toggleKeyboardView();
            binding.edpEmotion.setHintMessage(hintText);
            binding.edpEmotion.setPanelVisibility(View.VISIBLE);
        } else {
            binding.edpEmotion.reset();
            binding.edpEmotion.setPanelVisibility(View.GONE);
        }
    }

    private class EmotionPanelListener implements EmotionDefaultPanelV2.IPanelListener {
        private int position;
        private Circle info;
        private long replyUserId;

        public void setCircleInfo(int position, Circle info) {
            this.position = position;
            this.info = info;
            this.replyUserId = 0;
        }

        public void setCircleInfo(int position, Circle info, long replyUserId) {
            setCircleInfo(position, info);
            this.replyUserId = replyUserId;
        }

        @Override
        public void onHeightChanged(int currentHeight) {
        }

        @Override
        public void cancelClick() {
            setEmotionStatus(false);
        }

        @Override
        public boolean sentClick(String content) {
            if (StringUtils.isBlank(content)) {
                Toast.makeText(activity, "发送的内容不能为空", Toast.LENGTH_SHORT).show();
                return false;
            }

            LoadingDialog.show(activity, "正在发送,请稍后...");
            circleStandard.addComment(info, content, replyUserId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<CircleComment>() {
                        @Override
                        public void accept(CircleComment info) throws Exception {
                            setEmotionStatus(false);
                            LoadingDialog.hide();
                            circleAdapter.addComment(position, info);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            LoadingDialog.hide();
                            Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            return true;
        }
    }

    private class CircleItemListener implements ICircleItemListener {

        private CircleActionMorePop mMorePopupWindow;
        private boolean isRunning = false;

        @Override
        public void onCircleBackImageClick() {
            final Dialog circleBackImageDialog = new Dialog(activity, R.style.BottomDialog);
            LinearLayout root = (LinearLayout) LayoutInflater.from(activity).inflate(
                    R.layout.circle_back_image_action_dialog, null);
            root.findViewById(R.id.btn_change).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UITransfer.toImageSelectActivity(activity, ImageSelectType
                            .CIRCLE_BACK_IMAGE, 1);
                    circleBackImageDialog.dismiss();
                }
            });
            circleBackImageDialog.setContentView(root);
            circleBackImageDialog.show();
        }

        @Override
        public void onMyUserHeadClick() {
            UITransfer.toCircleUserIndexActivity(activity, UserSP.getUserId(),
                    UserSP.getUserName(), UserSP.getUserHeadImage());
        }

        @Override
        public void onUserHeadClick(Circle info) {
            UITransfer.toUserIndexActivity(activity, info.getPublishUserId(),
                    info.getPublishUserName(), info.getPublishUserImage());
        }

        @Override
        public void onContentClick(Circle info) {

        }

        @Override
        public void onLikeUserHeadClick(CircleLike likeInfo) {
            UITransfer.toUserIndexActivity(activity, likeInfo.getLikeUserId(),
                    likeInfo.getLikeUserName(), likeInfo.getLikeUserImage());
        }

        @Override
        public void onCommentUserClick(CircleComment comment) {
            UITransfer.toUserIndexActivity(activity, comment.getCommentUserId(),
                    comment.getCommentUserName(), comment.getCommentUserImage());
        }

        @Override
        public void onReplyUserClick(CircleComment comment) {
            UITransfer.toUserIndexActivity(activity, comment.getReplyUserId(),
                    comment.getReplyUserName(), comment.getReplyUserImage());
        }

        @Override
        public void onCommentContentClick(CircleComment comment) {
            if (StringUtils.isNotBlank(comment.getCommentUserName()) && !UserSP.getUserShowName().equals(comment.getCommentUserName())) {
                setEmotionStatus(true, "回复 " + comment.getCommentUserName() + " :");
            } else {
                setEmotionStatus(true, "输入评论内容");
            }
            Circle currentInfo = circleAdapter.getInfo(comment.getCircleId());
            if (currentInfo == null) return;
            emotionPanelListener.setCircleInfo(circleAdapter.getInfoPosition(comment.getCircleId()), currentInfo, comment.getCommentUserId());
        }

        @Override
        public boolean onCommentLongClick(View view, final CircleComment comment) {
            PopupMenu popup = new PopupMenu(activity, view);
            popup.getMenuInflater().inflate(R.menu.popup_comment_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.pop_comment_copy:
                            ClipboardAction.copy(activity, comment.getContent());
                            break;
                        case R.id.pop_comment_delete:
                            deleteComment(comment);
                            break;
                    }
                    return true;
                }
            });
            popup.show();
            return false;
        }

        private void deleteComment(final CircleComment comment) {
            Circle info = circleAdapter.getInfo(comment.getCircleId());
            circleStandard.deleteComment(info, comment)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String lastUpdateTime) {
                            comment.setLastUpdateTime(lastUpdateTime);
                            int pos = circleAdapter.getInfoPosition(comment.getCircleId());
                            circleAdapter.deleteComment(pos, comment);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        @Override
        public void onMoreActionClick(final View view, int position, final Circle info) {
            if (info == null) return;
            if (mMorePopupWindow == null) {
                mMorePopupWindow = new CircleActionMorePop(activity);
            }
            mMorePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    view.setVisibility(View.VISIBLE);
                }
            });
            mMorePopupWindow.setActionMoreListener(new CircleActionMorePop.IActionMoreListener() {
                @Override
                public void onCommentClick(int position) {
                    setEmotionStatus(true, "输入评论内容");
                    emotionPanelListener.setCircleInfo(position, info);
                }

                @Override
                public void onLikeClick(int position) {
                    setLike(position, info);
                }
            });
            mMorePopupWindow.show(position, view, info.getIsLiked());
        }

        private void setLike(final int position, Circle info) {
            if (isRunning) return;
            isRunning = true;

            final boolean isLike = !info.getIsLiked();
            circleStandard.setLike(info, isLike)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<CircleLike>() {
                        @Override
                        public void accept(CircleLike info) {
                            isRunning = false;
                            circleAdapter.setLike(position, info, isLike);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            isRunning = false;
                            Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        @Override
        public void onMoreCommentClick() {

        }

        @Override
        public void onDeleteClick(final int position, final Circle info) {
            new AlertDialog.Builder(activity)
                    .setMessage("信息删除后,不能恢复,确认删除 ?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteCircle(position, info.getCircleId());
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }

        private void deleteCircle(final int position, long circleId) {
            LoadingDialog.show(activity);
            circleStandard.deleteInfo(circleId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) {
                            circleAdapter.remove(position);
                            LoadingDialog.hide();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            LoadingDialog.hide();
                            Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        @Override
        public void onPublishClick() {
            UITransfer.toCirclePublishActivity(activity);
        }

        @Override
        public void onMsgcountClick() {
            UITransfer.toCircleMessagesActivity(activity);
        }

        @Override
        public void onImageClick(int position, List<CircleImage> images) {
            if (images != null && images.size() > 0) {
                List<String> imageUrls = new ArrayList<>(images.size());
                for (int i = 0; i < images.size(); i++) {
                    imageUrls.add(images.get(i).getImageUrl());
                }
                UITransfer.toImageBrowseActivity(activity, ActivityImageBrowse.MODE_VIEW,
                        position, imageUrls);
            }
        }
    }
}
