package com.hwl.beta.ui.near;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.hwl.beta.R;
import com.hwl.beta.databinding.NearActivityDetailBinding;
import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleLike;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.KeyBoardAction;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.convert.DBNearCircleAction;
import com.hwl.beta.ui.dialog.LoadingDialog;
import com.hwl.beta.ui.imgselect.ActivityImageBrowse;
import com.hwl.beta.ui.near.action.INearCircleDetailListener;
import com.hwl.beta.ui.near.adp.NearCircleCommentAdapter;
import com.hwl.beta.ui.common.NineImagesAdapter;
import com.hwl.beta.ui.near.logic.NearLogic;
import com.hwl.beta.ui.near.standard.NearStandard;
import com.hwl.beta.ui.user.bean.ImageViewBean;
import com.hwl.beta.ui.widget.CircleActionMorePop;
import com.hwl.beta.utils.DisplayUtils;
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

        this.loadDetails();
    }

    private void loadDetails() {
        binding.pbCircleLoading.setVisibility(View.VISIBLE);
        binding.svCircleContainer.setVisibility(View.GONE);

        final long nearCircleId = getIntent().getLongExtra("nearcircleid", 0);
        nearStandard.loadLocalDetails(nearCircleId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NearCircle>() {
                    @Override
                    public void accept(NearCircle info) throws Exception {
                        if (info == null) {
                            loadServerDetails(nearCircleId, null);
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

    private void loadServerDetails(long nearCircleId, String updateTime) {
        nearStandard.loadServerDetails(nearCircleId, updateTime)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NearCircle>() {
                    @Override
                    public void accept(NearCircle info) throws Exception {
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

        if (currentInfo.getLikes() != null && currentInfo.getLikes().size() > 0) {
            binding.fblLikeContainer.setVisibility(View.VISIBLE);
            this.setLikeViews(currentInfo.getLikes());
        } else {
            binding.fblLikeContainer.setVisibility(View.GONE);
        }

        if (currentInfo.getComments() != null && currentInfo.getComments().size() > 0) {
            binding.rvComments.setVisibility(View.VISIBLE);
            binding.rvComments.setAdapter(new NearCircleCommentAdapter(activity,
                    currentInfo.getComments(),
                    itemListener));
            binding.rvComments.setLayoutManager(new LinearLayoutManager(activity));
        } else {
            binding.rlCommentContainer.setVisibility(View.GONE);
        }
    }

    private void setLikeView(final NearCircleLike likeInfo) {
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

    private void setLikeViews(final List<NearCircleLike> likes) {
        if (likes == null || likes.size() <= 0) return;
        binding.fblLikeContainer.removeAllViews();
        for (int i = 0; i < likes.size(); i++) {
            setLikeView(likes.get(i));
        }
    }

    private void removeLikeView() {
        if (currentInfo == null || currentInfo.getLikes() == null || currentInfo.getLikes().size() <= 0)
            return;
        for (int i = 0; i < currentInfo.getLikes().size(); i++) {
            if (currentInfo.getLikes().get(i).getLikeUserId() == UserSP.getUserId()) {
                currentInfo.getLikes().remove(i);
                binding.fblLikeContainer.removeViewAt(i);
                break;
            }
        }
    }

    private class NearCircleDetailListener implements INearCircleDetailListener {

        private CircleActionMorePop mMorePopupWindow;
        boolean isRunning = false;

        @Override
        public void onItemViewClick(View view) {
            KeyBoardAction.hideSoftInput(view);
        }

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
//           if (comment.getCommentUserId() == myUserId) {
//               UITransfer.toNearCommentPublishActivity(activity, comment.getNearCircleId(),
// info.getPublishUserId(), info.getNearCircleMessageContent());
//           } else {
//               UITransfer.toNearCommentPublishActivity(activity, comment.getNearCircleId(),
// info.getPublishUserId(), comment.getCommentUserId(), comment.getCommentUserName(), info
// .getNearCircleMessageContent());
//           }
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
            nearStandard.setLike(currentInfo, isLike)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<NearCircleLike>() {
                        @Override
                        public void accept(NearCircleLike info) {
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
//                            nearCircleAdapter.remove(position);
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
