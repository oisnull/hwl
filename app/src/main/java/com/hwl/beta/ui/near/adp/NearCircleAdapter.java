package com.hwl.beta.ui.near.adp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwl.beta.R;
import com.hwl.beta.databinding.NearItemBinding;
import com.hwl.beta.databinding.NearItemNullBinding;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleLike;
import com.hwl.beta.db.ext.NearCircleExt;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.near.action.INearCircleItemListener;
import com.hwl.beta.ui.near.holder.NearCircleNullViewHolder;
import com.hwl.beta.ui.near.holder.NearCircleViewHolder;
import com.hwl.beta.ui.user.bean.ImageViewBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/16.
 */

public class NearCircleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<NearCircleExt> nearCircles;
    INearCircleItemListener itemListener;
    LayoutInflater inflater;
    long myUserId;

    public NearCircleAdapter(Context context, List<NearCircleExt> nearCircles, INearCircleItemListener itemListener) {
        this.context = context;
        this.nearCircles = nearCircles;
        this.itemListener = itemListener;
        inflater = LayoutInflater.from(context);
        myUserId = UserSP.getUserId();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new NearCircleNullViewHolder((NearItemNullBinding) DataBindingUtil.inflate(inflater, R.layout.near_item_null, parent, false));
        } else {
            return new NearCircleViewHolder((NearItemBinding) DataBindingUtil.inflate(inflater, R.layout.near_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NearCircleExt info = nearCircles.get(position);
        if (holder instanceof NearCircleNullViewHolder) {
            NearCircleNullViewHolder viewHolder = (NearCircleNullViewHolder) holder;
            viewHolder.setItemBinding(itemListener);
        } else if (holder instanceof NearCircleViewHolder) {
            NearCircleViewHolder viewHolder = (NearCircleViewHolder) holder;
            viewHolder.setItemBinding(itemListener,
                    position,
                    info.getInfo(),
                    info.getImages(),
                    info.getLikes(),
                    info.getComments(),
                    new ImageViewBean(info.getInfo().getPublishUserImage())
            );

            if (info.getInfo().getPublishUserId() == myUserId) {
                viewHolder.getItemBinding().ivDelete.setVisibility(View.VISIBLE);
            } else {
                viewHolder.getItemBinding().ivDelete.setVisibility(View.GONE);
            }
        }
    }

    public void addComment(NearCircleComment comment) {
        if (comment == null || comment.getNearCircleId() <= 0 || comment.getCommentUserId() <= 0)
            return;

        int position = -1;
        List<NearCircleComment> comments = null;
        for (int i = 0; i < nearCircles.size(); i++) {
            if (nearCircles.get(i).getInfo().getNearCircleId() == comment.getNearCircleId()) {
                comments = nearCircles.get(i).getComments();
                if (comments == null) {
                    comments = new ArrayList<>();
                    nearCircles.get(i).setComments(comments);
                }
                position = i;
                break;
            }
        }


        comments.add(comment);
        if (position == -1) {
            notifyItemChanged(0);
        } else {
            notifyItemChanged(position);
        }
    }

    public void addLike(int position, NearCircleLike likeInfo) {
        NearCircleExt info = nearCircles.get(position);
        if (info.getLikes() == null) {
            info.setLikes(new ArrayList<NearCircleLike>());
        }

        if (likeInfo == null) {
            //取消点赞
            info.getInfo().setIsLiked(false);
            for (int i = 0; i < info.getLikes().size(); i++) {
                if (info.getLikes().get(i).getLikeUserId() == myUserId) {
                    info.getLikes().remove(i);
                    notifyItemChanged(position);
                    break;
                }
            }
        } else {
            //点赞
            info.getInfo().setIsLiked(true);
            info.getLikes().add(info.getLikes().size(), likeInfo);
            notifyItemChanged(position);
        }
    }

    public void remove(int position) {
        nearCircles.remove(position);
        notifyItemRangeRemoved(position, 1);
        notifyItemRangeChanged(position, nearCircles.size() - 1);
    }

    @Override
    public int getItemCount() {
        return nearCircles.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (nearCircles.get(position).getInfo().getContentType() == NetConstant.CIRCLE_CONTENT_NULL)
            return 0;
        return 1;
    }
}
