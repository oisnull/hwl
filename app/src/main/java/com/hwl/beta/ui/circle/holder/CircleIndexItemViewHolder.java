package com.hwl.beta.ui.circle.holder;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hwl.beta.databinding.CircleIndexItemBinding;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.db.entity.CircleLike;
import com.hwl.beta.ui.circle.action.ICircleItemListener;
import com.hwl.beta.ui.circle.action.ICircleLikeItemListener;
import com.hwl.beta.ui.circle.adp.CircleCommentAdapter;
import com.hwl.beta.ui.common.NineImagesAdapter;
import com.hwl.beta.ui.convert.DBCircleAction;
import com.hwl.beta.ui.user.bean.ImageViewBean;

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
                               final Circle info) {
        this.itemBinding.setAction(itemListener);
        this.itemBinding.setPosition(position);
        this.itemBinding.setImage(new ImageViewBean(info.getPublishUserImage()));
        this.itemBinding.setInfo(info);
        this.itemBinding.rvComments.setAdapter(new CircleCommentAdapter(context,
                info.getComments(), itemListener));
        this.itemBinding.rvComments.setLayoutManager(new LinearLayoutManager(context));
        this.itemBinding.fblLikeContainer.removeAllViews();


        if (info.getImages() == null || info.getImages().size() <= 0) {
            this.itemBinding.rvImages.setVisibility(View.GONE);
        } else {
            NineImagesAdapter imagesAdapter = new NineImagesAdapter(context,
                    DBCircleAction.convertToNineImageModels(info.getImages()),
                    new NineImagesAdapter.ImageItemListener() {
                        @Override
                        public void onImageClick(int position, String imageUrl) {
                            itemListener.onImageClick(position, info.getImages());
                        }
                    });
            this.itemBinding.rvImages.setVisibility(View.VISIBLE);
            this.itemBinding.rvImages.setAdapter(imagesAdapter);
            this.itemBinding.rvImages.setLayoutManager(new GridLayoutManager(context,
                    imagesAdapter.getColumnCount()));
        }

        boolean isShowActionContainer = false;
        if (info.getLikes() != null && info.getLikes().size() > 0) {
            isShowActionContainer = true;
            this.itemBinding.rlLikeContainer.setVisibility(View.VISIBLE);
            CircleUserLikeOperate.setLikeInfos(itemBinding.fblLikeContainer, info.getLikes(),
                    itemListener);
        } else {
            this.itemBinding.rlLikeContainer.setVisibility(View.GONE);
        }

        if (info.getComments() != null && info.getComments().size() > 0) {
            isShowActionContainer = true;
            this.itemBinding.rlCommentContainer.setVisibility(View.VISIBLE);
        } else {
            this.itemBinding.rlCommentContainer.setVisibility(View.GONE);
        }

        if (isShowActionContainer) {
            this.itemBinding.llActionContainer.setVisibility(View.VISIBLE);
        } else {
            this.itemBinding.llActionContainer.setVisibility(View.GONE);
        }
    }

    public void setLikeInfo(CircleLike likeInfo, ICircleLikeItemListener itemListener) {
        if (likeInfo == null) return;
        if (!itemBinding.rlLikeContainer.isShown())
            itemBinding.rlLikeContainer.setVisibility(View.VISIBLE);
        if (!itemBinding.llActionContainer.isShown())
            itemBinding.llActionContainer.setVisibility(View.VISIBLE);
        CircleUserLikeOperate.setLikeInfo(itemBinding.fblLikeContainer, likeInfo, itemListener);
    }

    public void cancelLikeInfo(CircleLike likeInfo) {
        if (likeInfo == null) return;
        CircleUserLikeOperate.cancelLikeInfo(itemBinding.fblLikeContainer,
                likeInfo.getLikeUserId());
        if (itemBinding.fblLikeContainer.getChildCount() <= 0) {
            itemBinding.rlLikeContainer.setVisibility(View.GONE);
        }
    }

    public void setCommentInfo(CircleComment comment) {
        if (comment == null) return;
        if (!itemBinding.rlCommentContainer.isShown())
            itemBinding.rlCommentContainer.setVisibility(View.VISIBLE);
        if (!itemBinding.llActionContainer.isShown())
            itemBinding.llActionContainer.setVisibility(View.VISIBLE);

        CircleCommentAdapter commentAdapter =
                (CircleCommentAdapter) itemBinding.rvComments.getAdapter();
        commentAdapter.addComment(comment);
    }

    public void deleteCommentInfo(CircleComment comment) {
        if (comment == null) return;
        CircleCommentAdapter commentAdapter =
                (CircleCommentAdapter) itemBinding.rvComments.getAdapter();
        commentAdapter.deleteComment(comment);
        if (commentAdapter.getItemCount() <= 0) {
            itemBinding.rlCommentContainer.setVisibility(View.GONE);
        }
    }

    public CircleIndexItemBinding getItemBinding() {
        return this.itemBinding;
    }
}
