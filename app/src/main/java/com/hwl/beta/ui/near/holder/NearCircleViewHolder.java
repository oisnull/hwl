package com.hwl.beta.ui.near.holder;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hwl.beta.databinding.NearItemBinding;
import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleImage;
import com.hwl.beta.db.entity.NearCircleLike;
import com.hwl.beta.ui.convert.DBNearCircleAction;
import com.hwl.beta.ui.near.action.INearCircleCommentItemListener;
import com.hwl.beta.ui.near.action.INearCircleItemListener;
import com.hwl.beta.ui.near.action.INearCircleLikeItemListener;
import com.hwl.beta.ui.near.adp.NearCircleCommentAdapter;
import com.hwl.beta.ui.common.NineImagesAdapter;
import com.hwl.beta.ui.user.bean.ImageViewBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/16.
 */

public class NearCircleViewHolder extends RecyclerView.ViewHolder {

    private NearItemBinding itemBinding;
    private Context context;
    private NearCircleCommentAdapter commentAdapter;

    public NearCircleViewHolder(Context context, NearItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
        this.context = context;
        this.commentAdapter = null;
    }

    public void setItemBinding(final INearCircleItemListener itemListener,
                               int position,
                               final NearCircle info,
                               final List<NearCircleImage> images,
                               List<NearCircleLike> likes,
                               List<NearCircleComment> comments,
                               ImageViewBean headImage
    ) {
        this.itemBinding.setAction(itemListener);
        this.itemBinding.setPosition(position);
        this.itemBinding.setInfo(info);
        this.itemBinding.setImage(headImage);

        if (images == null || images.size() <= 0) {
            this.itemBinding.rvImages.setVisibility(View.GONE);
        } else {
            NineImagesAdapter imagesAdapter = new NineImagesAdapter(context,
                    DBNearCircleAction.convertToNineImageModels(images),
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
        }

        if ("哈哈".equals(info.getContent())) {
            String te = info.getContent();
        }

        boolean isShowActionContainer = false;
        if (likes != null && likes.size() > 0) {
            isShowActionContainer = true;
            this.itemBinding.rlLikeContainer.setVisibility(View.VISIBLE);
            this.itemBinding.fblLikeContainer.removeAllViews();
            UserLikeOperate.setLikeInfos(itemBinding.fblLikeContainer, likes, itemListener);
        } else {
            this.itemBinding.rlLikeContainer.setVisibility(View.GONE);
        }

        if (comments != null && comments.size() > 0) {
            isShowActionContainer = true;
            commentAdapter = new NearCircleCommentAdapter(context, comments, itemListener);
            this.itemBinding.rlCommentContainer.setVisibility(View.VISIBLE);
            this.itemBinding.rvComments.setAdapter(commentAdapter);
            this.itemBinding.rvComments.setLayoutManager(new LinearLayoutManager(context));
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

    public void setCommentInfo(NearCircleComment comment,
                               INearCircleCommentItemListener itemListener) {
        if (comment == null) return;
        if (!itemBinding.rlCommentContainer.isShown())
            itemBinding.rlCommentContainer.setVisibility(View.VISIBLE);
        if (!itemBinding.llActionContainer.isShown())
            itemBinding.llActionContainer.setVisibility(View.VISIBLE);

        if (commentAdapter == null) {
            List<NearCircleComment> comments = new ArrayList<>();
            comments.add(comment);
            commentAdapter = new NearCircleCommentAdapter(context, comments, itemListener);
            itemBinding.rvComments.setAdapter(commentAdapter);
            itemBinding.rvComments.setLayoutManager(new LinearLayoutManager(context));
        } else {
            commentAdapter.addComment(comment);
        }
    }

    public NearItemBinding getItemBinding() {
        return this.itemBinding;
    }
}
