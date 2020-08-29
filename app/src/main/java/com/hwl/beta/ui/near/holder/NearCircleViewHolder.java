package com.hwl.beta.ui.near.holder;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.hwl.beta.databinding.NearItemBinding;
import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleLike;
import com.hwl.beta.ui.convert.DBNearCircleAction;
import com.hwl.beta.ui.near.action.INearCircleItemListener;
import com.hwl.beta.ui.near.action.INearCircleLikeItemListener;
import com.hwl.beta.ui.near.adp.NearCircleCommentAdapter;
import com.hwl.beta.ui.common.NineImagesAdapter;
import com.hwl.beta.ui.user.bean.ImageViewBean;

/**
 * Created by Administrator on 2018/2/16.
 */

public class NearCircleViewHolder extends RecyclerView.ViewHolder {

    private NearItemBinding itemBinding;
    private Context context;

    public NearCircleViewHolder(Context context, NearItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
        this.context = context;
    }

    public void setItemBinding(final INearCircleItemListener itemListener,
                               int position,
                               final NearCircle info
    ) {
        this.itemBinding.setAction(itemListener);
        this.itemBinding.setPosition(position);
        this.itemBinding.setInfo(info);
        this.itemBinding.setImage(new ImageViewBean(info.getPublishUserImage()));
        this.itemBinding.rvComments.setAdapter(new NearCircleCommentAdapter(context,
                info.getComments(), itemListener));
        this.itemBinding.rvComments.setLayoutManager(new LinearLayoutManager(context));
        this.itemBinding.fblLikeContainer.removeAllViews();

        if (info.getImages() == null || info.getImages().size() <= 0) {
            this.itemBinding.rvImages.setVisibility(View.GONE);
        } else {
            NineImagesAdapter imagesAdapter = new NineImagesAdapter(context,
                    DBNearCircleAction.convertToNineImageModels(info.getImages()),
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
            UserLikeOperate.setLikeInfos(itemBinding.fblLikeContainer, info.getLikes(),
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

    public void setLikeInfo(NearCircleLike likeInfo, INearCircleLikeItemListener itemListener) {
        if (likeInfo == null) return;
        if (!itemBinding.rlLikeContainer.isShown())
            itemBinding.rlLikeContainer.setVisibility(View.VISIBLE);
        if (!itemBinding.llActionContainer.isShown())
            itemBinding.llActionContainer.setVisibility(View.VISIBLE);
        UserLikeOperate.setLikeInfo(itemBinding.fblLikeContainer, likeInfo, itemListener);
    }

    public void cancelLikeInfo(NearCircleLike likeInfo) {
        if (likeInfo == null) return;
        UserLikeOperate.cancelLikeInfo(itemBinding.fblLikeContainer, likeInfo.getLikeUserId());
        if (itemBinding.fblLikeContainer.getChildCount() <= 0) {
            itemBinding.rlLikeContainer.setVisibility(View.GONE);
        }
    }

    public void setCommentInfo(NearCircleComment comment) {
        if (comment == null) return;
        if (!itemBinding.rlCommentContainer.isShown())
            itemBinding.rlCommentContainer.setVisibility(View.VISIBLE);
        if (!itemBinding.llActionContainer.isShown())
            itemBinding.llActionContainer.setVisibility(View.VISIBLE);

        NearCircleCommentAdapter commentAdapter =
                (NearCircleCommentAdapter) itemBinding.rvComments.getAdapter();
        commentAdapter.addComment(comment);
    }

    public void deleteCommentInfo(NearCircleComment comment) {
        if (comment == null) return;
        NearCircleCommentAdapter commentAdapter =
                (NearCircleCommentAdapter) itemBinding.rvComments.getAdapter();
        commentAdapter.deleteComment(comment);
        if (commentAdapter.getItemCount() <= 0) {
            itemBinding.rlCommentContainer.setVisibility(View.GONE);
        }
    }

    public NearItemBinding getItemBinding() {
        return this.itemBinding;
    }
}
