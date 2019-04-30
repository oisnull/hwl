package com.hwl.beta.ui.circle;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityCircleDetailBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.db.entity.CircleLike;
import com.hwl.beta.db.ext.CircleExt;
import com.hwl.beta.mq.send.CircleMessageSend;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.circle.CircleService;
import com.hwl.beta.net.circle.body.GetCircleDetailResponse;
import com.hwl.beta.net.circle.body.SetLikeInfoResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.busbean.EventActionCircleComment;
import com.hwl.beta.ui.busbean.EventActionCircleLike;
import com.hwl.beta.ui.busbean.EventBusConstant;
import com.hwl.beta.ui.circle.action.ICircleCommentItemListener;
import com.hwl.beta.ui.circle.action.ICircleDetailListener;
import com.hwl.beta.ui.circle.adp.CircleCommentAdapter;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.KeyBoardAction;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.convert.DBCircleAction;
import com.hwl.beta.ui.imgselect.ActivityImageBrowse;
import com.hwl.beta.ui.user.bean.ImageViewBean;
import com.hwl.beta.ui.widget.CircleActionMorePop;
import com.hwl.beta.ui.widget.MultiImageView;
import com.hwl.beta.utils.DisplayUtils;
import com.hwl.beta.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityCircleDetail extends BaseActivity {

    Activity activity;
    ActivityCircleDetailBinding binding;
    ICircleDetailListener itemListener;
    CircleCommentAdapter commentAdapter;
    CircleExt info;
    long myUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        myUserId = UserSP.getUserId();
        itemListener = new CircleDetailListener();
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_circle_detail);
        binding.setAction(itemListener);

        initView();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addComment(EventActionCircleComment action) {
        if (action.getActionType() != EventBusConstant.EB_TYPE_ACTINO_ADD) return;
        CircleComment comment = action.getComment();
        if (comment == null || comment.getCircleId() <= 0 || comment.getCommentUserId() <= 0)
            return;

        int position = info.getComments().size();
        info.getComments().add(position, comment);
        commentAdapter.notifyItemInserted(position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

        //数据源可能会从三个位置来，从本地数据库，根据id,序列化传进来
        long circleid = getIntent().getLongExtra("circleid", 0);
        info = (CircleExt) getIntent().getSerializableExtra("circleext");
        if (info == null) {
            info = DaoUtils.getCircleManagerInstance().get(circleid);
        }

        if (info == null) {
            info = new CircleExt(circleid);
            binding.pbCircleLoading.setVisibility(View.VISIBLE);
            binding.svCircleContainer.setVisibility(View.GONE);
            this.loadFromServer(true);
        } else {
            binding.pbCircleLoading.setVisibility(View.GONE);
            binding.svCircleContainer.setVisibility(View.VISIBLE);
            bindData();
            this.loadFromServer(false);
        }

        this.setLikeViews(info.getLikes());
        commentAdapter = new CircleCommentAdapter(activity, info.getComments(), new CircleCommentItemListener());
        binding.rvComments.setAdapter(commentAdapter);
        binding.rvComments.setLayoutManager(new LinearLayoutManager(activity));
    }

    private void bindData() {
        ImageViewBean.loadImage(binding.ivHeader, info.getInfo().getPublishUserImage());
        binding.tvUsername.setText(info.getInfo().getPublishUserName());
        binding.tvPosDesc.setText(info.getInfo().getFromPosDesc());
        binding.tvPublicTime.setText(info.getInfo().getShowTime());

        if (StringUtils.isBlank(info.getInfo().getContent())) {
            binding.tvContent.setVisibility(View.GONE);
        } else {
            binding.tvContent.setVisibility(View.VISIBLE);
            binding.tvContent.setText(info.getInfo().getContent());
        }
        if (info.getInfo().getPublishUserId() == myUserId) {
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
            binding.mivImages.setImagesData(DBCircleAction.convertToMultiImages(info.getImages()));
            binding.mivImages.setVisibility(View.VISIBLE);
        } else {
            binding.mivImages.setVisibility(View.GONE);
        }
    }

    private void loadFromServer(final boolean isResetInfo) {
        CircleService.getCircleDetail(info.getInfo().getCircleId())
                .subscribe(new NetDefaultObserver<GetCircleDetailResponse>() {
                    @Override
                    protected void onSuccess(GetCircleDetailResponse response) {
                        List<CircleComment> comments = DBCircleAction.convertToCircleCommentInfos(response.getCircleInfo().getCommentInfos());
                        if (comments != null && comments.size() > 0) {
                            comments.removeAll(info.getComments());
                            info.getComments().addAll(comments);
                        }
                        info.setLikes(DBCircleAction.convertToCircleLikeInfos(response.getCircleInfo().getLikeInfos()));
                        if (response.getCircleInfo() != null) {
                            if (isResetInfo) {
                                info.setInfo(DBCircleAction.convertToCircleInfo(response.getCircleInfo()));
                                info.setImages(DBCircleAction.convertToCircleImageInfos(response.getCircleInfo().getCircleId(), response.getCircleInfo().getPublishUserId(), response.getCircleInfo().getImages()));
                                bindData();
                            } else {
                                if (response.getCircleInfo().getUpdateTime() != null && !response.getCircleInfo().getUpdateTime().equals(info.getInfo().getUpdateTime())) {
                                    info.getInfo().setUpdateTime(response.getCircleInfo().getUpdateTime());
                                    setLikeViews(info.getLikes());
                                    commentAdapter.notifyItemRangeChanged(0, info.getComments().size());
                                }
                            }
                            saveInfo(response.getCircleInfo().getUpdateTime());
                        } else {
                            Toast.makeText(activity, "数据已经被用户删除!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        binding.pbCircleLoading.setVisibility(View.GONE);
                        binding.svCircleContainer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    protected void onError(String resultMessage) {
                        super.onError(resultMessage);
                        binding.pbCircleLoading.setVisibility(View.GONE);
                        binding.svCircleContainer.setVisibility(View.GONE);
                    }
                });
    }

    private void saveInfo(String lastUpdateTime) {
        //如果没有新的更新就不保存
        if (StringUtils.isBlank(lastUpdateTime) || lastUpdateTime.equals(info.getInfo().getUpdateTime()))
            return;
        //只存在我发布的信息
        if (info != null && info.getInfo() != null && info.getInfo().getPublishUserId() == myUserId) {
            DaoUtils.getCircleManagerInstance().save(info.getInfo());
            DaoUtils.getCircleManagerInstance().deleteImages(info.getInfo().getCircleId());
            DaoUtils.getCircleManagerInstance().deleteComments(info.getInfo().getCircleId());
            DaoUtils.getCircleManagerInstance().deleteLikes(info.getInfo().getCircleId());
            DaoUtils.getCircleManagerInstance().saveImages(info.getInfo().getCircleId(), info.getImages());
            DaoUtils.getCircleManagerInstance().saveComments(info.getInfo().getCircleId(), info.getComments());
            DaoUtils.getCircleManagerInstance().saveLikes(info.getInfo().getCircleId(), info.getLikes());
        }
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

    private void removeLikeView() {
        if (info == null || info.getLikes() == null || info.getLikes().size() <= 0)
            return;
        for (int i = 0; i < info.getLikes().size(); i++) {
            if (info.getLikes().get(i).getLikeUserId() == myUserId) {
                EventBus.getDefault().post(new EventActionCircleLike(EventBusConstant.EB_TYPE_ACTINO_REMOVE, info.getLikes().get(i)));
                info.getLikes().remove(i);
                binding.fblLikeContainer.removeViewAt(i);
                break;
            }
        }
    }

    private class CircleDetailListener implements ICircleDetailListener {

        private CircleActionMorePop mMorePopupWindow;
        boolean isRuning = false;

        @Override
        public void onItemViewClick(View view) {
            KeyBoardAction.hideSoftInput(view);
        }

        @Override
        public void onMyUserHeadClick() {
            UITransfer.toCircleUserIndexActivity(activity, UserSP.getUserId(), UserSP.getUserName(), UserSP.getUserHeadImage(), UserSP.getUserCirclebackimage(), UserSP.getLifeNotes());
        }

        @Override
        public void onUserHeadClick() {
            UITransfer.toUserIndexActivity(activity, info.getInfo().getPublishUserId(), info.getInfo().getPublishUserName(), info.getInfo().getPublishUserImage());
        }

        @Override
        public void onLikeUserHeadClick(CircleLike likeInfo) {
            UITransfer.toUserIndexActivity(activity, likeInfo.getLikeUserId(), likeInfo.getLikeUserName(), likeInfo.getLikeUserImage());
        }

        @Override
        public void onCommentUserClick(CircleComment comment) {
            UITransfer.toUserIndexActivity(activity, comment.getCommentUserId(), comment.getCommentUserName(), comment.getCommentUserImage());
        }

        @Override
        public void onReplyUserClick(CircleComment comment) {
            UITransfer.toUserIndexActivity(activity, comment.getReplyUserId(), comment.getReplyUserName(), comment.getReplyUserImage());
        }

        @Override
        public void onCommentContentClick(CircleComment comment) {
            if (comment.getCommentUserId() == myUserId) {
                UITransfer.toCircleCommentPublishActivity(activity, comment.getCircleId(), info.getInfo().getPublishUserId(), info.getCircleMessageContent());
            } else {
                UITransfer.toCircleCommentPublishActivity(activity, comment.getCircleId(), info.getInfo().getPublishUserId(), comment.getCommentUserId(), comment.getCommentUserName(), info.getCircleMessageContent());
            }
        }

        @Override
        public void onContentClick() {

        }

        @Override
        public void onMoreActionClick(final View view) {
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
                    UITransfer.toCircleCommentPublishActivity(activity, info.getInfo().getCircleId(), info.getInfo().getPublishUserId(), info.getCircleMessageContent());
                }

                @Override
                public void onLikeClick(int position) {
                    setLikeInfo(position, info);
                }
            });
            mMorePopupWindow.show(0, view, info.getInfo().getIsLiked());
        }

        private void setLikeInfo(final int position, final CircleExt info) {
            if (isRuning || info == null || info.getInfo() == null || info.getInfo().getCircleId() <= 0)
                return;
            isRuning = true;
            final boolean isLiked = info.getInfo().getIsLiked();
            CircleService.setLikeInfo(isLiked ? 0 : 1, info.getInfo().getCircleId())
                    .subscribe(new NetDefaultObserver<SetLikeInfoResponse>() {
                        @Override
                        protected void onSuccess(SetLikeInfoResponse response) {
                            isRuning = false;
                            if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
                                if (isLiked) {
                                    //取消点赞
                                    info.getInfo().setIsLiked(false);
                                    removeLikeView();
                                    CircleMessageSend.sendDeleteLikeMessage(info.getInfo().getCircleId(), info.getInfo().getPublishUserId()).subscribe();
                                } else {
                                    //点赞
                                    info.getInfo().setIsLiked(true);
                                    CircleLike likeInfo = new CircleLike();
                                    likeInfo.setCircleId(info.getInfo().getCircleId());
                                    likeInfo.setLikeUserId(myUserId);
                                    likeInfo.setLikeUserName(UserSP.getUserName());
                                    likeInfo.setLikeUserImage(UserSP.getUserHeadImage());
                                    likeInfo.setLikeTime(new Date());
                                    info.getLikes().add(likeInfo);
                                    setLikeView(likeInfo);
                                    EventBus.getDefault().post(new EventActionCircleLike(EventBusConstant.EB_TYPE_ACTINO_ADD, likeInfo));
                                    CircleMessageSend.sendAddLikeMessage(info.getInfo().getCircleId(), info.getInfo().getPublishUserId(), info.getCircleMessageContent()).subscribe();
                                }
                                saveCircleInfo();
                            } else {
                                onError("操作失败");
                            }
                        }

                        @Override
                        protected void onError(String resultMessage) {
                            super.onError(resultMessage);
                            isRuning = false;
                        }
                    });
        }

        private void saveCircleInfo() {
            if (!DaoUtils.getCircleManagerInstance().isExists(info.getInfo().getCircleId()))
                return;
            DaoUtils.getCircleManagerInstance().save(info.getInfo());
            DaoUtils.getCircleManagerInstance().deleteImages(info.getInfo().getCircleId());
            DaoUtils.getCircleManagerInstance().deleteComments(info.getInfo().getCircleId());
            DaoUtils.getCircleManagerInstance().deleteLikes(info.getInfo().getCircleId());
            DaoUtils.getCircleManagerInstance().saveImages(info.getInfo().getCircleId(), info.getImages());
            DaoUtils.getCircleManagerInstance().saveComments(info.getInfo().getCircleId(), info.getComments());
            DaoUtils.getCircleManagerInstance().saveLikes(info.getInfo().getCircleId(), info.getLikes());
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
        }

        @Override
        public void onPublishClick() {
            UITransfer.toCirclePublishActivity(activity);
        }

        @Override
        public void onImageClick(int position) {
            if (info.getImages() != null && info.getImages().size() > 0) {
                List<String> imageUrls = new ArrayList<>(info.getImages().size());
                for (int i = 0; i < info.getImages().size(); i++) {
                    imageUrls.add(info.getImages().get(i).getImageUrl());
                }
                UITransfer.toImageBrowseActivity(activity, ActivityImageBrowse.MODE_VIEW, position, imageUrls);
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
