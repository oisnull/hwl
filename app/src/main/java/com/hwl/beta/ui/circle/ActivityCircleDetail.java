package com.hwl.beta.ui.circle;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.hwl.beta.R;
import com.hwl.beta.databinding.CircleActivityDetailBinding;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.db.entity.CircleLike;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.circle.action.ICircleCommentItemListener;
import com.hwl.beta.ui.circle.action.ICircleDetailListener;
import com.hwl.beta.ui.circle.adp.CircleCommentAdapter;
import com.hwl.beta.ui.circle.logic.CircleLogic;
import com.hwl.beta.ui.circle.standard.CircleStandard;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.KeyBoardAction;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.convert.DBCircleAction;
import com.hwl.beta.ui.dialog.LoadingDialog;
import com.hwl.beta.ui.imgselect.ActivityImageBrowse;
import com.hwl.beta.ui.user.bean.ImageViewBean;
import com.hwl.beta.ui.widget.CircleActionMorePop;
import com.hwl.beta.ui.widget.MultiImageView;
import com.hwl.beta.utils.DisplayUtils;
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
    CircleCommentAdapter commentAdapter;
    Circle currentInfo;

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

        this.loadDetails();
    }

    private void loadDetails() {
        binding.pbCircleLoading.setVisibility(View.VISIBLE);
        binding.svCircleContainer.setVisibility(View.GONE);

        final long circleId = getIntent().getLongExtra("circleid", 0);
        circleStandard.loadLocalDetails(circleId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Circle>() {
                    @Override
                    public void accept(Circle info) throws Exception {
                        if (info == null) {
                            loadServerDetails(circleId, null);
                        } else {
                            currentInfo = info;
                            bindData();
                            binding.pbCircleLoading.setVisibility(View.GONE);
                            binding.svCircleContainer.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        binding.pbCircleLoading.setVisibility(View.GONE);
                        binding.svCircleContainer.setVisibility(View.GONE);
                        Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadServerDetails(long circleId, String updateTime) {
        circleStandard.loadServerDetails(circleId, updateTime)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Circle>() {
                    @Override
                    public void accept(Circle info) throws Exception {
                        if (info != null) {
                            currentInfo = info;
                            bindData();
                        }
                        binding.pbCircleLoading.setVisibility(View.GONE);
                        binding.svCircleContainer.setVisibility(View.VISIBLE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        binding.pbCircleLoading.setVisibility(View.GONE);
                        binding.svCircleContainer.setVisibility(View.GONE);
                        Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void bindData() {
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
            binding.mivImages.setImageListener(new MultiImageView.IMultiImageListener() {
                @Override
                public void onImageClick(int position, String imageUrl) {
                    itemListener.onImageClick(position);
                }
            });
            binding.mivImages.setImagesData(DBCircleAction.convertToMultiImages(currentInfo.getImages()));
            binding.mivImages.setVisibility(View.VISIBLE);
        } else {
            binding.mivImages.setVisibility(View.GONE);
        }

        this.setLikeViews(currentInfo.getLikes());
        commentAdapter = new CircleCommentAdapter(activity, currentInfo.getComments(),
                new CircleCommentItemListener());
        binding.rvComments.setAdapter(commentAdapter);
        binding.rvComments.setLayoutManager(new LinearLayoutManager(activity));
    }

    private void setLikeView(final CircleLike likeInfo) {
        if (likeInfo == null) {
            return;
        }
        int size = DisplayUtils.dp2px(activity, 25);
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(size, size);
        params.rightMargin = 2;
        params.bottomMargin = 2;
        ImageView ivLikeUser = new ImageView(activity);
        ImageViewBean.loadImage(ivLikeUser, likeInfo.getLikeUserImage());
        ivLikeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onLikeUserHeadClick(likeInfo);
            }
        });
        binding.fblLikeContainer.addView(ivLikeUser, params);
    }

    private void setLikeViews(final List<CircleLike> likes) {
        if (likes == null || likes.size() <= 0) return;
        binding.fblLikeContainer.removeAllViews();
        for (int i = 0; i < likes.size(); i++) {
            setLikeView(likes.get(i));
        }
    }

    private class CircleDetailListener implements ICircleDetailListener {

        private CircleActionMorePop mMorePopupWindow;
        boolean isRunning = false;

        @Override
        public void onItemViewClick(View view) {
            KeyBoardAction.hideSoftInput(view);
        }

        @Override
        public void onMyUserHeadClick() {
            UITransfer.toCircleUserIndexActivity(activity, UserSP.getUserId(),
                    UserSP.getUserName(), UserSP.getUserHeadImage());
        }

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
//            if (comment.getCommentUserId() == myUserId) {
//                UITransfer.toCircleCommentPublishActivity(activity, comment.getCircleId(), info
//                .getInfo().getPublishUserId(), info.getCircleMessageContent());
//            } else {
//                UITransfer.toCircleCommentPublishActivity(activity, comment.getCircleId(), info
//                .getInfo().getPublishUserId(), comment.getCommentUserId(), comment
//                .getCommentUserName(), info.getCircleMessageContent());
//            }
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
                    //setEmotionStatus(true, "输入评论内容");
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
        public void onDeleteClick(Circle info) {
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
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            LoadingDialog.hide();
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

    private class CircleCommentItemListener implements ICircleCommentItemListener {
        @Override
        public void onCommentUserClick(CircleComment comment) {
            itemListener.onCommentUserClick(comment);
        }

        @Override
        public void onReplyUserClick(CircleComment comment) {
            itemListener.onReplyUserClick(comment);
        }

        @Override
        public void onContentClick(CircleComment comment) {
            itemListener.onCommentContentClick(comment);
        }
    }
}
