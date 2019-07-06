package com.hwl.beta.ui.near;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.NearActivityDetailBinding;
import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleLike;
import com.hwl.beta.emotion.EmotionDefaultPanelV2;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.ClipboardAction;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.convert.DBNearCircleAction;
import com.hwl.beta.ui.dialog.LoadingDialog;
import com.hwl.beta.ui.imgselect.ActivityImageBrowse;
import com.hwl.beta.ui.near.action.INearCircleDetailListener;
import com.hwl.beta.ui.near.adp.NearCircleCommentAdapter;
import com.hwl.beta.ui.common.NineImagesAdapter;
import com.hwl.beta.ui.near.holder.UserLikeOperate;
import com.hwl.beta.ui.near.logic.NearLogic;
import com.hwl.beta.ui.near.standard.NearStandard;
import com.hwl.beta.ui.user.bean.ImageViewBean;
import com.hwl.beta.ui.widget.CircleActionMorePop;
import com.hwl.beta.utils.NetworkUtils;
import com.hwl.beta.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class ActivityNearDetail extends BaseActivity {

    FragmentActivity activity;
    NearActivityDetailBinding binding;
    NearStandard nearStandard;
    INearCircleDetailListener itemListener;
    NearCircle currentInfo;
    EmotionPanelListener emotionPanelListener;

    //1.load info from local db
    //2.if non exists in local, than load from services,
    //	if exists in local, than bind info and set the status is loading for comment and like
    //3.if services also non exists, show the status of deleting,
    //	if exists in services, bind comment and like

    //network status
    //non exists status

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        nearStandard = new NearLogic();
        itemListener = new NearCircleDetailListener();
        binding = DataBindingUtil.setContentView(activity, R.layout.near_activity_detail);
        binding.setAction(itemListener);

        initView();
    }

    private void initView() {
        binding.tbTitle.setTitle("附近动态详细")
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        emotionPanelListener = new EmotionPanelListener();
        binding.edpEmotion.setPanelVisibility(View.GONE);
        binding.edpEmotion.setPanelListener(emotionPanelListener);

        this.loadDetails();
    }

    //0 both are hidden
    //1 show loading
    //2 show content
    private void showContentLoading(int status) {
        switch (status) {
            case 0:
                binding.pbCircleLoading.setVisibility(View.GONE);
                binding.svCircleContainer.setVisibility(View.GONE);
                break;
            case 1:
                binding.pbCircleLoading.setVisibility(View.VISIBLE);
                binding.svCircleContainer.setVisibility(View.GONE);
                break;
            case 2:
                binding.pbCircleLoading.setVisibility(View.GONE);
                binding.svCircleContainer.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void loadDetails() {
        final long nearCircleId = getIntent().getLongExtra("nearcircleid", 0);
        nearStandard.loadLocalDetails(nearCircleId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NearCircle>() {
                    @Override
                    public void accept(NearCircle info) {
                        currentInfo = info;

                        if (currentInfo == null) {
                            showContentLoading(1);
                            loadServerDetails(nearCircleId, null);
                        } else {
                            bindInfo();
                            loadServerDetails(nearCircleId, currentInfo.getUpdateTime());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        showContentLoading(1);
                        loadServerDetails(nearCircleId, null);
//                        Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT)
//                        .show();
                    }
                });
    }

    private void loadServerDetails(long nearCircleId, String updateTime) {
        if (!NetworkUtils.isConnected()) {
            showContentLoading(2);
            bindComments();
            return;
        }

        nearStandard.loadServerDetails(nearCircleId, updateTime)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NearCircle>() {
                    @Override
                    public void accept(NearCircle info) {
                        if (info != null && currentInfo == null) {
                            currentInfo = info;
                            bindInfo();
                        } else
                            currentInfo = info;

                        showContentLoading(2);
                        bindComments();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        showContentLoading(2);
                        bindComments();
                    }
                });
    }

    private void bindInfo() {
        if (currentInfo == null) return;

        ImageViewBean.loadImage(binding.ivHeader, currentInfo.getPublishUserImage());
        binding.tvUsername.setText(currentInfo.getPublishUserName());
        binding.tvPosDesc.setText(currentInfo.getFromPosDesc());
        binding.tvPublicTime.setText(currentInfo.getShowTime());

        if (StringUtils.isBlank(currentInfo.getContent())) {
            binding.tvContent.setVisibility(View.GONE);
        } else {
            binding.tvContent.setVisibility(View.VISIBLE);
            binding.tvContent.setText(currentInfo.getContent());
        }

        if (currentInfo.getPublishUserId() == UserSP.getUserId()) {
            binding.ivDelete.setVisibility(View.VISIBLE);
        } else {
            binding.ivDelete.setVisibility(View.GONE);
        }

        if (currentInfo.getImages() != null && currentInfo.getImages().size() > 0) {
            NineImagesAdapter imagesAdapter = new NineImagesAdapter(activity,
                    DBNearCircleAction.convertToNineImageModels(currentInfo.getImages()),
                    new NineImagesAdapter.ImageItemListener() {
                        @Override
                        public void onImageClick(int position, String imageUrl) {
                            itemListener.onImageClick(position);
                        }
                    });
            binding.rvImages.setVisibility(View.VISIBLE);
            binding.rvImages.setAdapter(imagesAdapter);
            binding.rvImages.setLayoutManager(new GridLayoutManager(activity,
                    imagesAdapter.getColumnCount()));
        } else {
            binding.rvImages.setVisibility(View.GONE);
        }

        binding.llActionContainer.setVisibility(View.GONE);
        binding.llLikeLoading.setVisibility(View.VISIBLE);
    }

    private void bindComments() {
        binding.llLikeLoading.setVisibility(View.GONE);
        if (currentInfo == null) return;

        boolean isShowActionContainer = false;
        binding.fblLikeContainer.removeAllViews();
        if (currentInfo.getLikes() != null && currentInfo.getLikes().size() > 0) {
            isShowActionContainer = true;
            binding.fblLikeContainer.setVisibility(View.VISIBLE);
            UserLikeOperate.setLikeInfos(binding.fblLikeContainer, currentInfo.getLikes(),
                    itemListener);
        } else {
            binding.fblLikeContainer.setVisibility(View.GONE);
        }

        binding.rvComments.setAdapter(new NearCircleCommentAdapter(activity,
                currentInfo.getComments(), itemListener));
        binding.rvComments.setLayoutManager(new LinearLayoutManager(activity));
        if (currentInfo.getComments() != null && currentInfo.getComments().size() > 0) {
            isShowActionContainer = true;
            binding.rvComments.setVisibility(View.VISIBLE);
        } else {
            binding.rlCommentContainer.setVisibility(View.GONE);
        }

        if (isShowActionContainer) {
            binding.llActionContainer.setVisibility(View.VISIBLE);
        } else {
            binding.llActionContainer.setVisibility(View.GONE);
        }
    }

    private void getComments() {
        if (currentInfo == null) return;

        long lastCommentId =
                currentInfo.getComments() != null && currentInfo.getComments().size() > 0 ?
                        currentInfo.getComments().get(currentInfo.getComments().size() - 1).getCommentId() : 0;
        nearStandard.getComments(currentInfo, lastCommentId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<NearCircleComment>>() {
                    @Override
                    public void accept(List<NearCircleComment> comments) throws Exception {
                        setCommentInfos(comments);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void setLikeInfo(NearCircleLike likeInfo) {
        if (likeInfo == null) return;

        if (!binding.rlLikeContainer.isShown())
            binding.rlLikeContainer.setVisibility(View.VISIBLE);
        if (!binding.llActionContainer.isShown())
            binding.llActionContainer.setVisibility(View.VISIBLE);

        UserLikeOperate.setLikeInfo(binding.fblLikeContainer, likeInfo, itemListener);
    }

    public void cancelLikeInfo(NearCircleLike likeInfo) {
        if (likeInfo == null) return;

        UserLikeOperate.cancelLikeInfo(binding.fblLikeContainer, likeInfo.getLikeUserId());

        if (binding.fblLikeContainer.getChildCount() <= 0) {
            binding.rlLikeContainer.setVisibility(View.GONE);
        }
    }

    public void setCommentInfo(NearCircleComment comment) {
        if (comment == null) return;

        if (!binding.rlCommentContainer.isShown())
            binding.rlCommentContainer.setVisibility(View.VISIBLE);
        if (!binding.llActionContainer.isShown())
            binding.llActionContainer.setVisibility(View.VISIBLE);

        NearCircleCommentAdapter commentAdapter =
                (NearCircleCommentAdapter) binding.rvComments.getAdapter();
        commentAdapter.addComment(comment);
    }

    public void setCommentInfos(List<NearCircleComment> comments) {
        if (comments == null || comments.size() <= 0) return;

        if (!binding.rlCommentContainer.isShown())
            binding.rlCommentContainer.setVisibility(View.VISIBLE);
        if (!binding.llActionContainer.isShown())
            binding.llActionContainer.setVisibility(View.VISIBLE);

        NearCircleCommentAdapter commentAdapter =
                (NearCircleCommentAdapter) binding.rvComments.getAdapter();
        commentAdapter.addComments(comments);
    }

    public void deleteCommentInfo(NearCircleComment comment) {
        if (comment == null) return;
        NearCircleCommentAdapter commentAdapter =
                (NearCircleCommentAdapter) binding.rvComments.getAdapter();
        commentAdapter.deleteComment(comment);
        if (commentAdapter.getItemCount() <= 0) {
            binding.rlCommentContainer.setVisibility(View.GONE);
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
        private long replyUserId;

        public void setReplyUserId(long replyUserId) {
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
            nearStandard.addComment(currentInfo, content, replyUserId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<NearCircleComment>() {
                        @Override
                        public void accept(NearCircleComment info) throws Exception {
                            LoadingDialog.hide();
                            setEmotionStatus(false);
                            setCommentInfo(info);
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

    private class NearCircleDetailListener implements INearCircleDetailListener {

        private CircleActionMorePop mMorePopupWindow;
        private boolean isRunning = false;

        @Override
        public void onUserHeadClick() {
            UITransfer.toUserIndexActivity(activity, currentInfo.getPublishUserId(),
                    currentInfo.getPublishUserName(), currentInfo.getPublishUserImage());
        }

        @Override
        public void onLikeUserHeadClick(NearCircleLike likeInfo) {
            UITransfer.toUserIndexActivity(activity, likeInfo.getLikeUserId(),
                    likeInfo.getLikeUserName(), likeInfo.getLikeUserImage());
        }

        @Override
        public void onCommentUserClick(NearCircleComment comment) {
            UITransfer.toUserIndexActivity(activity, comment.getCommentUserId(),
                    comment.getCommentUserName(), comment.getCommentUserImage());
        }

        @Override
        public void onReplyUserClick(NearCircleComment comment) {
            UITransfer.toUserIndexActivity(activity, comment.getReplyUserId(),
                    comment.getReplyUserName(), comment.getReplyUserImage());
        }

        @Override
        public void onCommentContentClick(NearCircleComment comment) {
            if (StringUtils.isNotBlank(comment.getCommentUserName()) && !UserSP.getUserShowName().equals(comment.getCommentUserName())) {
                setEmotionStatus(true, "回复 " + comment.getCommentUserName() + " :");
            } else {
                setEmotionStatus(true, "输入评论内容");
            }
            emotionPanelListener.setReplyUserId(comment.getCommentUserId());
        }

        @Override
        public boolean onCommentLongClick(View view, final NearCircleComment comment) {
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

        private void deleteComment(final NearCircleComment comment) {
            nearStandard.deleteComment(currentInfo, comment)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String lastUpdateTime) {
                            //comment.setLastUpdateTime(lastUpdateTime);
                            deleteCommentInfo(comment);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        @Override
        public void onContentClick() {

        }

        @Override
        public void onMoreCommentClick() {

        }

        @Override
        public void onMoreActionClick(final View view) {
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
                }

                @Override
                public void onLikeClick(int position) {
                    setLike();
                }
            });
            mMorePopupWindow.show(0, view, currentInfo.getIsLiked());
        }

        private void setLike() {
            if (isRunning) return;
            isRunning = true;

            final boolean isLike = !currentInfo.getIsLiked();
            nearStandard.setLike(currentInfo, isLike)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<NearCircleLike>() {
                        @Override
                        public void accept(NearCircleLike info) {
                            isRunning = false;
                            currentInfo.setIsLiked(isLike);
                            currentInfo.setUpdateTime(info.getLastUpdateTime());
                            if (isLike)
                                setLikeInfo(info);
                            else
                                cancelLikeInfo(info);
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
        public void onDeleteClick() {
            new AlertDialog.Builder(activity)
                    .setMessage("信息删除后,不能恢复,确认删除 ?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteCircle(currentInfo.getNearCircleId());
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }

        private void deleteCircle(long nearCircleId) {
            LoadingDialog.show(activity);
            nearStandard.deleteInfo(nearCircleId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) {
                            LoadingDialog.hide();
                            finish();
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
            UITransfer.toNearPublishActivity(activity);
        }

        @Override
        public void onImageClick(int position) {
            if (currentInfo.getImages() != null && currentInfo.getImages().size() > 0) {
                List<String> imageUrls = new ArrayList<>(currentInfo.getImages().size());
                for (int i = 0; i < currentInfo.getImages().size(); i++) {
                    imageUrls.add(currentInfo.getImages().get(i).getImageUrl());
                }
                UITransfer.toImageBrowseActivity(activity, ActivityImageBrowse.MODE_VIEW,
                        position, imageUrls);
            }
        }
    }
}
