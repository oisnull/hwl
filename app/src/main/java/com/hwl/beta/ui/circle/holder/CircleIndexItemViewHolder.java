package com.hwl.beta.ui.circle.holder;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.android.flexbox.FlexboxLayout;
import com.hwl.beta.databinding.CircleIndexItemBinding;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.db.entity.CircleImage;
import com.hwl.beta.db.entity.CircleLike;
import com.hwl.beta.ui.circle.action.ICircleCommentItemListener;
import com.hwl.beta.ui.circle.action.ICircleItemListener;
import com.hwl.beta.ui.circle.adp.CircleCommentAdapter;
import com.hwl.beta.ui.common.NineImagesAdapter;
import com.hwl.beta.ui.convert.DBCircleAction;
import com.hwl.beta.ui.user.bean.ImageViewBean;
import com.hwl.beta.utils.DisplayUtils;

import java.util.List;

public class CircleIndexItemViewHolder extends RecyclerView.ViewHolder {

    private CircleIndexItemBinding itemBinding;
    private Context context;

    public CircleIndexItemViewHolder(Context context, CircleIndexItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
        this.context = context;
    }

    public void setItemBinding(final ICircleItemListener itemListener,
                               int position,
                               ImageViewBean imageBean,
                               Circle info,
                               final List<CircleImage> images,
                               List<CircleLike> likes,
                               List<CircleComment> comments) {
        this.itemBinding.setAction(itemListener);
        this.itemBinding.setPosition(position);
        this.itemBinding.setImage(imageBean);
        this.itemBinding.setInfo(info);

        if (images != null && images.size() > 0) {
            NineImagesAdapter imagesAdapter = new NineImagesAdapter(context,
                    DBCircleAction.convertToNineImageModels(images),
                    new NineImagesAdapter.ImageItemListener() {
                        @Override
                        public void onImageClick(int position, String imageUrl) {
                            itemListener.onImageClick(position, images);
                        }
                    });
            this.itemBinding.rvImages.setVisibility(View.VISIBLE);
            this.itemBinding.rvImages.setAdapter(imagesAdapter);
            this.itemBinding.rvImages.setLayoutManager(new GridLayoutManager(context,
                    imagesAdapter.getColumnCount()));
        } else {
            this.itemBinding.rvImages.setVisibility(View.GONE);
        }

        boolean isShowActionContainer = false;
        if (likes != null && likes.size() > 0) {
            isShowActionContainer = true;
            this.itemBinding.rlLikeContainer.setVisibility(View.VISIBLE);
            this.setLikeViews(likes, itemListener);
        } else {
            this.itemBinding.rlLikeContainer.setVisibility(View.GONE);
        }

        if (comments != null && comments.size() > 0) {
            isShowActionContainer = true;
            this.itemBinding.rlCommentContainer.setVisibility(View.VISIBLE);
            Context context = this.itemBinding.rvComments.getContext();
            this.itemBinding.rvComments.setVisibility(View.VISIBLE);
            this.itemBinding.rvComments.setAdapter(
                    new CircleCommentAdapter(
                            context,
                            comments,
                            new ICircleCommentItemListener() {
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
                    )
            );
            this.itemBinding.rvComments.setLayoutManager(new LinearLayoutManager(context));
        } else {
            this.itemBinding.rlCommentContainer.setVisibility(View.GONE);
        }
        if (isShowActionContainer) {
            this.itemBinding.llActionContainer.setVisibility(View.VISIBLE);
        } else {
            this.itemBinding.llActionContainer.setVisibility(View.GONE);
        }
        this.itemBinding.executePendingBindings();
    }

    private void setLikeViews(final List<CircleLike> likes, final ICircleItemListener itemListener) {
        if (likes == null || likes.size() <= 0) return;
        this.itemBinding.fblLikeContainer.removeAllViews();
        Context context = this.itemBinding.rlLikeContainer.getContext();
        int size = DisplayUtils.dp2px(context, 25);
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(size, size);
        params.rightMargin = 2;
        params.bottomMargin = 2;
        ImageView ivLikeUser;
        for (int i = 0; i < likes.size(); i++) {
            final CircleLike like = likes.get(i);
            ivLikeUser = new ImageView(context);
            ImageViewBean.loadImage(ivLikeUser, like.getLikeUserImage());
            ivLikeUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onLikeUserHeadClick(like);
                }
            });
            this.itemBinding.fblLikeContainer.addView(ivLikeUser, params);
        }
    }

    public CircleIndexItemBinding getItemBinding() {
        return this.itemBinding;
    }
}
