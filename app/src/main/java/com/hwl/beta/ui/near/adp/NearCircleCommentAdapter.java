package com.hwl.beta.ui.near.adp;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hwl.beta.R;
import com.hwl.beta.databinding.NearCommentItemBinding;
import com.hwl.beta.databinding.NearCommentReplyItemBinding;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.ui.near.action.INearCircleCommentItemListener;
import com.hwl.beta.ui.near.holder.NearCommentReplyViewHolder;
import com.hwl.beta.ui.near.holder.NearCommentViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/3/17.
 */

public class NearCircleCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<NearCircleComment> comments;
    LayoutInflater inflater;
    INearCircleCommentItemListener itemListener;

    public NearCircleCommentAdapter(Context context, List<NearCircleComment> comments,
                                    INearCircleCommentItemListener itemListener) {
        this.context = context;
        this.comments = comments;
        this.itemListener = itemListener;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new NearCommentViewHolder(NearCommentItemBinding.inflate(inflater, parent, false));
        } else {
            return new NearCommentReplyViewHolder(NearCommentReplyItemBinding.inflate(inflater, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NearCircleComment comment = comments.get(position);
        if (holder instanceof NearCommentViewHolder) {
            ((NearCommentViewHolder) holder).setItemBinding(itemListener, comment);
        } else if (holder instanceof NearCommentReplyViewHolder) {
            ((NearCommentReplyViewHolder) holder).setItemBinding(itemListener, comment);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (comments.get(position).getReplyUserId() > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public void addComment(NearCircleComment comment) {
        if (comment == null || comment.getCommentId() <= 0) return;

        int pos = comments.indexOf(comment);
        if (pos == -1) {
            comments.add(comment);
            notifyItemChanged(comments.size() - 1);
        } else
            notifyItemChanged(pos);
    }

    public void addComments(List<NearCircleComment> infos) {
        if (infos == null || infos.size() <= 0) return;

        int pos = comments.size();
        comments.addAll(infos);
		notifyItemRangeChanged(pos,comments.size());
    }

    public void deleteComment(NearCircleComment comment) {
        if (comment == null || comment.getCommentId() <= 0) return;

        comments.remove(comment);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
