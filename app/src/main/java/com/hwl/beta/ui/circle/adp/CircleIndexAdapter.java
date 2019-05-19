package com.hwl.beta.ui.circle.adp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwl.beta.R;
import com.hwl.beta.databinding.CircleHeadItemBinding;
import com.hwl.beta.databinding.CircleIndexItemBinding;
import com.hwl.beta.databinding.CircleItemNullBinding;
import com.hwl.beta.databinding.CircleMsgcountItemBinding;
import com.hwl.beta.db.DBConstant;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.db.entity.CircleLike;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.circle.action.ICircleItemListener;
import com.hwl.beta.ui.circle.holder.CircleHeadItemViewHolder;
import com.hwl.beta.ui.circle.holder.CircleIndexItemViewHolder;
import com.hwl.beta.ui.circle.holder.CircleItemNullViewHolder;
import com.hwl.beta.ui.circle.holder.CircleMsgcountItemViewHolder;
import com.hwl.beta.ui.user.bean.ImageViewBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CircleIndexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Circle> circles;
    private ICircleItemListener itemListener;
    private LayoutInflater inflater;
    private long myUserId;
    private Context context;

    public CircleIndexAdapter(Context context, ICircleItemListener itemListener) {
        this.circles = new ArrayList<>();
        this.itemListener = itemListener;
        this.context = context;
        inflater = LayoutInflater.from(context);
        myUserId = UserSP.getUserId();
    }

    public List<Circle> getInfos() {
        return this.circles;
    }

    public long getMinId() {
        if (getItemCount() <= 0) {
            return 0;
        }
        return this.circles.get(getItemCount() - 1).getCircleId();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
            default:
                return new CircleItemNullViewHolder((CircleItemNullBinding) DataBindingUtil.inflate(inflater, R.layout.circle_item_null, parent, false));
            case 1:
                return new CircleHeadItemViewHolder((CircleHeadItemBinding) DataBindingUtil.inflate(inflater, R.layout.circle_head_item, parent, false));
            case 2:
                return new CircleIndexItemViewHolder(context,
                        (CircleIndexItemBinding) DataBindingUtil.inflate(inflater,
                                R.layout.circle_index_item, parent, false));
            case 3:
                return new CircleMsgcountItemViewHolder((CircleMsgcountItemBinding) DataBindingUtil.inflate(inflater, R.layout.circle_msgcount_item, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (circles.get(position).getItemType()) {
            case DBConstant.CIRCLE_ITEM_NULL:
            default:
                return 0;
            case DBConstant.CIRCLE_ITEM_HEAD:
                return 1;
            case DBConstant.CIRCLE_ITEM_DATA:
                return 2;
            case DBConstant.CIRCLE_ITEM_MSGCOUNT:
                return 3;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Circle info = circles.get(position);
        if (holder instanceof CircleItemNullViewHolder) {
            CircleItemNullViewHolder viewHolder = (CircleItemNullViewHolder) holder;
            viewHolder.setItemBinding(itemListener);
        } else if (holder instanceof CircleHeadItemViewHolder) {
            CircleHeadItemViewHolder viewHolder = (CircleHeadItemViewHolder) holder;
            viewHolder.setItemBinding(itemListener, UserSP.getUserName(),
                    new ImageViewBean(UserSP.getUserHeadImage(), UserSP.getUserCirclebackimage()));
        } else if (holder instanceof CircleMsgcountItemViewHolder) {
            CircleMsgcountItemViewHolder viewHolder = (CircleMsgcountItemViewHolder) holder;
            int messageCount = MessageCountSP.getCircleMessageCount();
            viewHolder.setItemBinding(itemListener, messageCount);
            if (messageCount > 0) {
                viewHolder.itemView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.itemView.setVisibility(View.GONE);
            }
        } else if (holder instanceof CircleIndexItemViewHolder) {
            CircleIndexItemViewHolder viewHolder = (CircleIndexItemViewHolder) holder;
            viewHolder.setItemBinding(itemListener, position,
                    new ImageViewBean(info.getPublishUserImage()), info, info.getImages(),
                    info.getLikes(), info.getComments());

            if (info.getPublishUserId() == myUserId) {
                viewHolder.getItemBinding().ivDelete.setVisibility(View.VISIBLE);
            } else {
                viewHolder.getItemBinding().ivDelete.setVisibility(View.GONE);
            }
        }
    }

    public void updateInfos(List<Circle> infos) {
        if (infos == null || infos.size() <= 0) return;

        if (getItemCount() <= 0) {
            circles.addAll(infos);
            notifyDataSetChanged();
        } else {
            removeEmptyInfo();
            sortInfos(infos);
            circles.addAll(getCircleItemPosition(), infos);
            notifyDataSetChanged();
        }
    }

    public void updateHead(Friend info) {
        if (info == null) return;
        if (circles.get(0).getItemType() != DBConstant.CIRCLE_ITEM_HEAD) return;
        boolean isChanged = false;
        if (circles.get(0).getCircleBackImage() != info.getCircleBackImage()) {
            circles.get(0).setCircleBackImage(info.getCircleBackImage());
            isChanged = true;
        }
        if (circles.get(0).getLifeNotes() != info.getLifeNotes()) {
            circles.get(0).setLifeNotes(info.getLifeNotes());
            isChanged = true;
        }

        if (isChanged)
            notifyItemChanged(0);
    }

    private int getCircleItemPosition() {
        for (int i = 0; i < circles.size(); i++) {
            if (circles.get(i).getCircleId() > 0)
                return i;
        }
        return circles.size();
    }

    private void sortInfos(List<Circle> infos) {
        if (infos == null || infos.size() <= 0) return;
        Collections.sort(infos, new Comparator<Circle>() {
            public int compare(Circle arg0, Circle arg1) {
                return (int) (arg1.getCircleId() - arg0.getCircleId());
            }
        });
    }

    private void removeEmptyInfo() {
        for (int i = 0; i < circles.size(); i++) {
            if (circles.get(i).getItemType() == DBConstant.CIRCLE_ITEM_NULL) {
                circles.remove(i);
                break;
            }
        }
    }

    public void setEmptyInfo() {
        removeEmptyInfo();
        circles.add(getCircleItemPosition(), new Circle(DBConstant.CIRCLE_ITEM_NULL));
        notifyDataSetChanged();
    }

    public void addComment(CircleComment comment) {
        if (comment == null || comment.getCircleId() <= 0 || comment.getCommentUserId() <= 0)
            return;

        int position = -1;
        List<CircleComment> comments = null;
        for (int i = 0; i < circles.size(); i++) {
            if (circles.get(i) != null && circles.get(i).getCircleId() == comment.getCircleId()) {
                comments = circles.get(i).getComments();
                if (comments == null) {
                    comments = new ArrayList<>();
                    circles.get(i).setComments(comments);
                }
                position = i;
                break;
            }
        }

        if (comments == null) return;
        comments.add(comment);
        notifyItemChanged(position);
    }

    public void removeComment(CircleComment comment) {
        if (comment == null || comment.getCircleId() <= 0 || comment.getCommentUserId() <= 0) {
            return;
        }

        for (int i = 0; i < circles.size(); i++) {
            if (circles.get(i) != null && circles.get(i).getCircleId() == comment.getCircleId()) {
                Circle info = circles.get(i);
                if (info.getComments() != null && info.getComments().size() > 0) {
                    int len = info.getComments().size();
                    for (int j = 0; j < len; j++) {
                        if (info.getComments().get(j) != null && info.getComments().get(j).getCommentUserId() == myUserId) {
                            info.getComments().remove(j);
                            notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }

        }
    }

    public void remove(int position) {
        circles.remove(position);
        notifyItemRangeRemoved(position, 1);
        notifyItemRangeChanged(position, circles.size() - 1);
    }

    public void addLike(int position, CircleLike likeInfo) {
        Circle info = circles.get(position);
        if (info.getLikes() == null) {
            info.setLikes(new ArrayList<CircleLike>());
        }

        if (likeInfo == null) {
            //取消点赞
            info.setIsLiked(false);
            for (int i = 0; i < info.getLikes().size(); i++) {
                if (info.getLikes().get(i) != null && info.getLikes().get(i).getLikeUserId() == myUserId) {
                    info.getLikes().remove(i);
                    notifyItemChanged(position);
                    break;
                }
            }
        } else {
            //点赞
            info.setIsLiked(true);
            info.getLikes().add(info.getLikes().size(), likeInfo);
            notifyItemChanged(position);
        }
    }

    public void addLike(CircleLike likeInfo) {
        if (likeInfo == null || likeInfo.getCircleId() <= 0 || likeInfo.getLikeUserId() <= 0)
            return;

        int position = -1;
        List<CircleLike> likes = null;
        for (int i = 0; i < circles.size(); i++) {
            if (circles.get(i) != null && circles.get(i).getCircleId() == likeInfo.getCircleId()) {
                likes = circles.get(i).getLikes();
                if (likes == null) {
                    likes = new ArrayList<>();
                    circles.get(i).setLikes(likes);
                }
                position = i;
                break;
            }
        }

        if (likes == null) return;
        likes.add(likeInfo);
        if (position == -1) {
            notifyItemChanged(0);
        } else {
            notifyItemChanged(position);
        }
    }

    public void removeLike(CircleLike likeInfo) {
        if (likeInfo == null || likeInfo.getCircleId() <= 0 || likeInfo.getLikeUserId() <= 0)
            return;
        for (int i = 0; i < circles.size(); i++) {
            if (circles.get(i) != null && circles.get(i).getCircleId() == likeInfo.getCircleId()) {
                Circle info = circles.get(i);
                if (info.getLikes() != null && info.getLikes().size() > 0) {
                    int len = info.getLikes().size();
                    for (int j = 0; j < len; j++) {
                        if (info.getLikes().get(j) != null && info.getLikes().get(j).getLikeUserId() == myUserId) {
                            info.getLikes().remove(j);
                            notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return circles.size();
    }
}
