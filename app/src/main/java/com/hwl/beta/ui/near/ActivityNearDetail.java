package com.hwl.beta.ui.near;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

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
import com.hwl.beta.ui.near.action.INearCircleCommentItemListener;
import com.hwl.beta.ui.near.action.INearCircleDetailListener;
import com.hwl.beta.ui.near.adp.NearCircleCommentAdapter;
import com.hwl.beta.ui.near.logic.NearLogic;
import com.hwl.beta.ui.near.standard.NearStandard;
import com.hwl.beta.ui.user.bean.ImageViewBean;
import com.hwl.beta.ui.widget.CircleActionMorePop;
import com.hwl.beta.ui.widget.MultiImageView;
import com.hwl.beta.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class ActivityNearDetail extends BaseActivity {

    Activity activity;
    NearActivityDetailBinding binding;
    NearStandard nearStandard;
    NearCircle info;
    INearCircleDetailListener itemListener;
    NearCircleCommentAdapter commentAdapter;

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


        // if (info == null) {
        // info = new NearCircleExt(nearCircleId);
        // binding.pbCircleLoading.setVisibility(View.VISIBLE);
        // binding.svCircleContainer.setVisibility(View.GONE);
        // this.loadFromServer(true);
        // } else {
        // binding.pbCircleLoading.setVisibility(View.GONE);
        // binding.svCircleContainer.setVisibility(View.VISIBLE);
        // bindData();
        // this.loadFromServer(false);
        // }

        // this.setLikeViews(info.getLikes());
        // commentAdapter = new NearCircleCommentAdapter(activity, info.getComments(), new
        // NearCircleCommentItemListener());
        // binding.rvComments.setAdapter(commentAdapter);
        // binding.rvComments.setLayoutManager(new LinearLayoutManager(activity));
    }

    private void bindData() {
        ImageViewBean.loadImage(binding.ivHeader, info.getPublishUserImage());
        binding.tvUsername.setText(info.getPublishUserName());
        binding.tvPosDesc.setText(info.getFromPosDesc());
        binding.tvPublicTime.setText(info.getShowTime());

        if (StringUtils.isBlank(info.getContent())) {
            binding.tvContent.setVisibility(View.GONE);
        } else {
            binding.tvContent.setVisibility(View.VISIBLE);
            binding.tvContent.setText(info.getContent());
        }

        if (info.getPublishUserId() == UserSP.getUserId()) {
            binding.ivDelete.setVisibility(View.VISIBLE);
        } else {
            binding.ivDelete.setVisibility(View.GONE);
        }

        if (info.getImages() != null && info.getImages().size() > 0) {
            binding.mivImages.setImageListener(new MultiImageView.IMultiImageListener() {
                @Override
                public void onImageClick(int position, String imageUrl) {
                    itemListener.onImageClick(position);
                }
            });
            binding.mivImages.setImagesData(DBNearCircleAction.convertToMultiImages(info.getImages()));
            binding.mivImages.setVisibility(View.VISIBLE);
        } else {
            binding.mivImages.setVisibility(View.GONE);
        }
    }

    private void loadDetails() {
        long nearCircleId = getIntent().getLongExtra("nearcircleid", 0);
        nearStandard.loadDetails(nearCircleId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NearCircle>() {
                    @Override
                    public void accept(NearCircle info) throws Exception {
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


        // nearStandard.loadDetails(nearCircleId)
        // .subscribe(new NetDefaultObserver<GetNearCircleDetailResponse>() {
        // @Override
        // protected void onSuccess(GetNearCircleDetailResponse response) {
        // List<NearCircleComment> comments = DBNearCircleAction.convertToNearCircleCommentInfos
        // (response.getNearCircleInfo().getCommentInfos());
        // if (comments != null && comments.size() > 0) {
        // comments.removeAll(info.getComments());
        // info.getComments().addAll(comments);
        // }
        // info.setLikes(DBNearCircleAction.convertToNearCircleLikeInfos(response
        // .getNearCircleInfo().getLikeInfos()));
        // if (response.getNearCircleInfo() != null) {
        // if (isResetInfo) {
        // info.setInfo(DBNearCircleAction.convertToNearCircleInfo(response.getNearCircleInfo()));
        // info.setImages(DBNearCircleAction.convertToNearCircleImageInfos(response
        // .getNearCircleInfo().getNearCircleId(), response.getNearCircleInfo().getPublishUserId
        // (), response.getNearCircleInfo().getImages()));
        // bindData();
        // } else {
        // if (response.getNearCircleInfo().getUpdateTime() != null && !response
        // .getNearCircleInfo().getUpdateTime().equals(info.getUpdateTime())) {
        // info.setUpdateTime(response.getNearCircleInfo().getUpdateTime());
        // setLikeViews(info.getLikes());
        // commentAdapter.notifyItemRangeChanged(0, info.getComments().size());
        // }
        // }
        // saveInfo(response.getNearCircleInfo().getUpdateTime());
        // } else {
        // Toast.makeText(activity, "数据已经被用户删除!", Toast.LENGTH_SHORT).show();
        // finish();
        // }
        // binding.pbCircleLoading.setVisibility(View.GONE);
        // binding.svCircleContainer.setVisibility(View.VISIBLE);
        // }

        // @Override
        // protected void onError(String resultMessage) {
        // super.onError(resultMessage);
        // binding.pbCircleLoading.setVisibility(View.GONE);
        // binding.svCircleContainer.setVisibility(View.GONE);
        // }
        // });
    }

    // private void saveInfo(String lastUpdateTime) {
    // if (StringUtils.isBlank(lastUpdateTime) || lastUpdateTime.equals(info.getUpdateTime()))
    // return;
    // if (info != null && info != null && info.getPublishUserId() == myUserId) {
    // DaoUtils.getNearCircleManagerInstance().save(info);
    // DaoUtils.getNearCircleManagerInstance().deleteImages(info.getNearCircleId());
    // DaoUtils.getNearCircleManagerInstance().deleteComments(info.getNearCircleId());
    // DaoUtils.getNearCircleManagerInstance().deleteLikes(info.getNearCircleId());
    // DaoUtils.getNearCircleManagerInstance().saveImages(info.getNearCircleId(), info.getImages());
    // DaoUtils.getNearCircleManagerInstance().saveComments(info.getNearCircleId(), info
    // .getComments());
    // DaoUtils.getNearCircleManagerInstance().saveLikes(info.getNearCircleId(), info.getLikes());
    // }
    // }

    // private void setLikeView(final NearCircleLike likeInfo) {
    // if (likeInfo == null) {
    // return;
    // }
    // int size = DisplayUtils.dp2px(activity, 25);
    // FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(size, size);
    // params.rightMargin = 2;
    // params.bottomMargin = 2;
    // ImageView ivLikeUser = new ImageView(activity);
    // ImageViewBean.loadImage(ivLikeUser, likeInfo.getLikeUserImage());
    // ivLikeUser.setOnClickListener(new View.OnClickListener() {
    // @Override
    // public void onClick(View v) {
    // itemListener.onLikeUserHeadClick(likeInfo);
    // }
    // });
    // binding.fblLikeContainer.addView(ivLikeUser, params);
    // }

    // private void setLikeViews(final List<NearCircleLike> likes) {
    // if (likes == null || likes.size() <= 0) return;
    // binding.fblLikeContainer.removeAllViews();
    // for (int i = 0; i < likes.size(); i++) {
    // setLikeView(likes.get(i));
    // }
    // }

    // private void removeLikeView() {
    // if (info == null || info.getLikes() == null || info.getLikes().size() <= 0)
    // return;
    // for (int i = 0; i < info.getLikes().size(); i++) {
    // if (info.getLikes().get(i).getLikeUserId() == myUserId) {
    //EventBus.getDefault().post(new EventActionCircleLike(EventBusConstant
    // .EB_TYPE_ACTINO_REMOVE, info.getLikes().get(i)));
    // info.getLikes().remove(i);
    // binding.fblLikeContainer.removeViewAt(i);
    // break;
    // }
    // }
    // }

    private class NearCircleDetailListener implements INearCircleDetailListener {

        private CircleActionMorePop mMorePopupWindow;
        boolean isRunning = false;

        @Override
        public void onItemViewClick(View view) {
            KeyBoardAction.hideSoftInput(view);
        }

        @Override
        public void onUserHeadClick() {
            UITransfer.toUserIndexActivity(activity, info.getPublishUserId(),
                    info.getPublishUserName(), info.getPublishUserImage());
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
            mMorePopupWindow.show(0, view, info.getIsLiked());
        }

        private void setLike() {
            if (isRunning) return;
            isRunning = true;

            final boolean isLike = !info.getIsLiked();
            nearStandard.setLike(info, isLike)
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
                            deleteCircle(info.getNearCircleId());
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
            if (info.getImages() != null && info.getImages().size() > 0) {
                List<String> imageUrls = new ArrayList<>(info.getImages().size());
                for (int i = 0; i < info.getImages().size(); i++) {
                    imageUrls.add(info.getImages().get(i).getImageUrl());
                }
                UITransfer.toImageBrowseActivity(activity, ActivityImageBrowse.MODE_VIEW,
                        position, imageUrls);
            }
        }
    }

    private class NearCircleCommentItemListener implements INearCircleCommentItemListener {
        @Override
        public void onCommentUserClick(NearCircleComment comment) {
            itemListener.onCommentUserClick(comment);
        }

        @Override
        public void onReplyUserClick(NearCircleComment comment) {
            itemListener.onReplyUserClick(comment);
        }

        @Override
        public void onContentClick(NearCircleComment comment) {
            itemListener.onCommentContentClick(comment);
        }
    }
}
