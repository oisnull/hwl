package com.hwl.beta.ui.circle.adp;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hwl.beta.R;
import com.hwl.beta.databinding.CircleCommentItemBinding;
import com.hwl.beta.databinding.CircleCommentReplyItemBinding;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.ui.circle.action.ICircleCommentItemListener;
import com.hwl.beta.ui.circle.holder.CircleCommentReplyViewHolder;
import com.hwl.beta.ui.circle.holder.CircleCommentViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/3/17.
 */

public class CircleCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<CircleComment> comments;
    LayoutInflater inflater;
    ICircleCommentItemListener itemListener;

    public CircleCommentAdapter(Context context, List<CircleComment> comments, ICircleCommentItemListener itemListener) {
        this.context = context;
        this.comments = comments;
        this.itemListener = itemListener;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new CircleCommentViewHolder((CircleCommentItemBinding) DataBindingUtil.inflate(inflater, R.layout.circle_comment_item, parent, false));
        } else {
            return new CircleCommentReplyViewHolder((CircleCommentReplyItemBinding) DataBindingUtil.inflate(inflater, R.layout.circle_comment_reply_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CircleComment comment = comments.get(position);
        if (holder instanceof CircleCommentViewHolder) {
            ((CircleCommentViewHolder) holder).setItemBinding(itemListener, comment);
        } else if (holder instanceof CircleCommentReplyViewHolder) {
            ((CircleCommentReplyViewHolder) holder).setItemBinding(itemListener, comment);
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

    public void addComment(CircleComment comment) {
        if (comment == null || comment.getCommentId() <= 0) return;

        int pos = comments.indexOf(comment);
        if (pos == -1) {
            comments.add(comment);
            notifyItemChanged(comments.size() - 1);
        } else
            notifyItemChanged(pos);
    }

    public void addComments(List<CircleComment> infos) {
        if (infos == null || infos.size() <= 0) return;

        int pos = comments.size();
        comments.addAll(infos);
        notifyItemRangeChanged(pos,comments.size());
    }

    public void deleteComment(CircleComment comment) {
        if (comment == null || comment.getCommentId() <= 0) return;

        comments.remove(comment);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
