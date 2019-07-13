package com.hwl.beta.ui.circle;

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
import com.hwl.beta.databinding.CircleActivityDetailBinding;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.db.entity.CircleLike;
import com.hwl.beta.emotion.EmotionDefaultPanelV2;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.circle.action.ICircleDetailListener;
import com.hwl.beta.ui.circle.adp.CircleCommentAdapter;
import com.hwl.beta.ui.circle.holder.CircleUserLikeOperate;
import com.hwl.beta.ui.circle.logic.CircleLogic;
import com.hwl.beta.ui.circle.standard.CircleStandard;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.ClipboardAction;
import com.hwl.beta.ui.common.NineImagesAdapter;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.convert.DBCircleAction;
import com.hwl.beta.ui.dialog.LoadingDialog;
import com.hwl.beta.ui.imgselect.ActivityImageBrowse;
import com.hwl.beta.ui.user.bean.ImageViewBean;
import com.hwl.beta.ui.widget.CircleActionMorePop;
import com.hwl.beta.utils.NetworkUtils;
import com.hwl.beta.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class ActivityCircleDetail extends BaseActivity {

    FragmentActivity activity;
    CircleActivityDetailBinding binding;
    CircleStandard circleStandard;
    ICircleDetailListener itemListener;
    Circle currentInfo;
    EmotionPanelListener emotionPanelListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        circleStandard = new CircleLogic();
        itemListener = new CircleDetailListener();
        binding = DataBindingUtil.setContentView(activity, R.layout.circle_activity_detail);
        binding.setAction(itemListener);

        initView();
    }

    private void initView() {
        binding.tbTitle.setTitle("动态详细")
                .setImageRightHide()
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
        final long circleId = getIntent().getLongExtra("circleid", 0);
        circleStandard.loadLocalDetails(circleId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Circle>() {
                    @Override
                    public void accept(Circle info) {
                        currentInfo = info;
                        if (currentInfo == null) {
                            showContentLoading(1);
                            loadServerDetails(circleId, null);
                        } else {
                            bindInfo();
                            loadServerDetails(circleId, currentInfo.getUpdateTime());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        showContentLoading(1);
                        loadServerDetails(circleId, null);
                    }
                });
    }

    private void loadServerDetails(long circleId, String updateTime) {
        if (!NetworkUtils.isConnected()) {
            showContentLoading(2);
            bindComments();
            return;
        }

        circleStandard.loadServerDetails(circleId, updateTime)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Circle>() {
                    @Override
                    public void accept(Circle info) throws Exception {
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
                    DBCircleAction.convertToNineImageModels(currentInfo.getImages()),
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
            binding.rlLikeContainer.setVisibility(View.VISIBLE);
            CircleUserLikeOperate.setLikeInfos(binding.fblLikeContainer, currentInfo.getLikes(),
                    itemListener);
        } else {
            binding.rlLikeContainer.setVisibility(View.GONE);
        }

        binding.rvComments.setAdapter(new CircleCommentAdapter(activity,
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
//        circleStandard.getComments(currentInfo, lastCommentId)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<NearCircleComment>>() {
//                    @Override
//                    public void accept(List<NearCircleComment> comments) throws Exception {
//                        setCommentInfos(comments);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) {
//                        Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT)
//                        .show();
//                    }
//                });
    }

    public void setLikeInfo(CircleLike likeInfo) {
        if (likeInfo == null) return;

        if (!binding.rlLikeContainer.isShown())
            binding.rlLikeContainer.setVisibility(View.VISIBLE);
        if (!binding.llActionContainer.isShown())
            binding.llActionContainer.setVisibility(View.VISIBLE);

        CircleUserLikeOperate.setLikeInfo(binding.fblLikeContainer, likeInfo, itemListener);
    }

    public void cancelLikeInfo(CircleLike likeInfo) {
        if (likeInfo == null) return;

        CircleUserLikeOperate.cancelLikeInfo(binding.fblLikeContainer, likeInfo.getLikeUserId());

        if (binding.fblLikeContainer.getChildCount() <= 0) {
            binding.rlLikeContainer.setVisibility(View.GONE);
        }
    }

    public void setCommentInfo(CircleComment comment) {
        if (comment == null) return;

        if (!binding.rlCommentContainer.isShown())
            binding.rlCommentContainer.setVisibility(View.VISIBLE);
        if (!binding.llActionContainer.isShown())
            binding.llActionContainer.setVisibility(View.VISIBLE);

        CircleCommentAdapter commentAdapter =
                (CircleCommentAdapter) binding.rvComments.getAdapter();
        commentAdapter.addComment(comment);
    }

    public void setCommentInfos(List<CircleComment> comments) {
        if (comments == null || comments.size() <= 0) return;

        if (!binding.rlCommentContainer.isShown())
            binding.rlCommentContainer.setVisibility(View.VISIBLE);
        if (!binding.llActionContainer.isShown())
            binding.llActionContainer.setVisibility(View.VISIBLE);

        CircleCommentAdapter commentAdapter =
                (CircleCommentAdapter) binding.rvComments.getAdapter();
        commentAdapter.addComments(comments);
    }

    public void deleteCommentInfo(CircleComment comment) {
        if (comment == null) return;
        CircleCommentAdapter commentAdapter =
                (CircleCommentAdapter) binding.rvComments.getAdapter();
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
            circleStandard.addComment(currentInfo, content, replyUserId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<CircleComment>() {
                        @Override
                        public void accept(CircleComment info) throws Exception {
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

    private class CircleDetailListener implements ICircleDetailListener {

        private CircleActionMorePop mMorePopupWindow;
        private boolean isRunning = false;

//        @Override
//        public void onItemViewClick(View view) {
//            KeyBoardAction.hideSoftInput(view);
//        }
//
//        @Override
//        public void onMyUserHeadClick() {
//            UITransfer.toCircleUserIndexActivity(activity, UserSP.getUserId(),
//                    UserSP.getUserName(), UserSP.getUserHeadImage());
//        }

        @Override
        public void onUserHeadClick() {
            UITransfer.toUserIndexActivity(activity, currentInfo.getPublishUserId(),
                    currentInfo.getPublishUserName(), currentInfo.getPublishUserImage());
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
            emotionPanelListener.setReplyUserId(comment.getCommentUserId());
        }

        @Override
        public boolean onCommentLongClick(View view, final CircleComment comment) {
            PopupMenu popup = new PopupMenu(activity, view);
            popup.getMenuInflater().inflate(R.menu.popup_comment_menu, popup.getMenu());
            if (comment.getCommentUserId() != UserSP.getUserId()) {
                popup.getMenu().removeItem(R.id.pop_comment_delete);
            }
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
            circleStandard.deleteComment(currentInfo, comment)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String lastUpdateTime) {
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
            circleStandard.setLike(currentInfo, isLike)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<CircleLike>() {
                        @Override
                        public void accept(CircleLike info) {
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
        public void onMoreCommentClick() {

        }

        @Override
        public void onDeleteClick() {
            new AlertDialog.Builder(activity)
                    .setMessage("信息删除后,不能恢复,确认删除 ?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteCircle();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }

        private void deleteCircle() {
            LoadingDialog.show(activity);
            circleStandard.deleteInfo(currentInfo.getCircleId())
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
            UITransfer.toCirclePublishActivity(activity);
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
