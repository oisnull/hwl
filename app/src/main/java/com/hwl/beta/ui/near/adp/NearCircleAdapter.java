package com.hwl.beta.ui.near.adp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwl.beta.R;
import com.hwl.beta.databinding.NearItemBinding;
import com.hwl.beta.databinding.NearItemNullBinding;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleLike;
import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.near.action.INearCircleItemListener;
import com.hwl.beta.ui.near.holder.NearCircleNullViewHolder;
import com.hwl.beta.ui.near.holder.NearCircleViewHolder;
import com.hwl.beta.ui.user.bean.ImageViewBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2018/2/16.
 */

public class NearCircleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<NearCircle> nearCircles;
    INearCircleItemListener itemListener;
    LayoutInflater inflater;
    long myUserId;

    public NearCircleAdapter(Context context, List<NearCircle> nearCircles,
                             INearCircleItemListener itemListener) {
        this.context = context;
        this.nearCircles = nearCircles == null ? new ArrayList<NearCircle>() : nearCircles;
        this.itemListener = itemListener;
        inflater = LayoutInflater.from(context);
        myUserId = UserSP.getUserId();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new NearCircleNullViewHolder((NearItemNullBinding) DataBindingUtil.inflate(inflater, R.layout.near_item_null, parent, false));
        } else {
            return new NearCircleViewHolder(context,
                    (NearItemBinding) DataBindingUtil.inflate(inflater,
                            R.layout.near_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NearCircle info = nearCircles.get(position);
        if (holder instanceof NearCircleNullViewHolder) {
            NearCircleNullViewHolder viewHolder = (NearCircleNullViewHolder) holder;
            viewHolder.setItemBinding(itemListener);
        } else if (holder instanceof NearCircleViewHolder) {
            NearCircleViewHolder viewHolder = (NearCircleViewHolder) holder;
            viewHolder.setItemBinding(itemListener,
                    position,
                    info,
                    info.getImages(),
                    info.getLikes(),
                    info.getComments(),
                    new ImageViewBean(info.getPublishUserImage())
            );

            if (info.getPublishUserId() == myUserId) {
                viewHolder.getItemBinding().ivDelete.setVisibility(View.VISIBLE);
            } else {
                viewHolder.getItemBinding().ivDelete.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position,
                                 @NonNull List<Object> payloads) {
        if (payloads == null || payloads.size() <= 0) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            NearCircleViewHolder viewHolder = (NearCircleViewHolder) holder;
            Object obj = payloads.get(0);
            if (obj instanceof NearCircleLike) {
                if (nearCircles.get(position).getIsLiked()) {
                    viewHolder.setLikeInfo((NearCircleLike) obj, itemListener);
                } else {
                    viewHolder.cancelLikeInfo((NearCircleLike) obj);
                }
            } else if (obj instanceof NearCircleComment) {
                viewHolder.setCommentInfo((NearCircleComment) obj, itemListener);
            }
        }
    }

    public List<NearCircle> getInfos() {
        return this.nearCircles;
    }

    public void updateInfos(List<NearCircle> infos) {
        updateInfos(false, infos);
    }

    public void updateInfos(boolean isRefresh, List<NearCircle> infos) {
        if (infos == null || infos.size() <= 0) return;
        removeEmptyInfo();

        if (getItemCount() <= 0) {
            nearCircles.addAll(infos);
            notifyDataSetChanged();
        } else {
            if (isRefresh) {
                boolean isClear =
                        (infos.get(infos.size() - 1).getNearCircleId() - nearCircles.get(0).getNearCircleId()) > 1;
                if (isClear) {
                    nearCircles.clear();
                    nearCircles.addAll(0, infos);
                } else {
                    nearCircles.removeAll(infos);
                    nearCircles.addAll(0, infos);
                    sortInfos(nearCircles);
                }
            } else {
                nearCircles.addAll(infos);
            }
            notifyDataSetChanged();
        }
    }

    private void sortInfos(List<NearCircle> infos) {
        if (infos == null || infos.size() <= 0) return;
        Collections.sort(infos, new Comparator<NearCircle>() {
            public int compare(NearCircle arg0, NearCircle arg1) {
                return (int) (arg1.getNearCircleId() - arg0.getNearCircleId());
            }
        });
    }

    public void setEmptyInfo() {
        nearCircles.clear();
        nearCircles.add(new NearCircle(0, NetConstant.CIRCLE_CONTENT_NULL));
        notifyDataSetChanged();
    }

    private void removeEmptyInfo() {
        int position = nearCircles.indexOf(new NearCircle(0, NetConstant.CIRCLE_CONTENT_NULL));
        if (position != -1) {
            nearCircles.remove(position);
        }
    }

    public long getMinId() {
        if (getItemCount() <= 0) {
            return 0;
        }
        return nearCircles.get(getItemCount() - 1).getNearCircleId();
    }

    public void addComment(int position, NearCircleComment comment) {
        if (comment == null || comment.getNearCircleId() <= 0 || comment.getCommentUserId() <= 0)
            return;

        NearCircle info = nearCircles.get(position);
//        if (info.getComments() == null) {
//            info.setComments(new ArrayList<NearCircleComment>());
//        }

//        info.getComments().add(comment);
        info.setUpdateTime(comment.getLastUpdateTime());
        notifyItemChanged(position, comment);
    }

    public void setLike(int position, NearCircleLike likeInfo, boolean isLike) {
        NearCircle info = nearCircles.get(position);
        if (info.getLikes() == null) {
            info.setLikes(new ArrayList<NearCircleLike>());
        }

        if (isLike) {
            info.setIsLiked(true);
            info.setUpdateTime(likeInfo.getLastUpdateTime());
            info.getLikes().add(likeInfo);
        } else {
            info.setIsLiked(false);
            info.setUpdateTime(likeInfo.getLastUpdateTime());
            info.getLikes().remove(likeInfo);
        }
//        notifyItemChanged(position);
        notifyItemChanged(position, likeInfo);
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
        if (nearCircles.get(position).getContentType() == NetConstant.CIRCLE_CONTENT_NULL)
            return 0;
        return 1;
    }
}
