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

    public Circle getInfo(long circleId) {
        if (circleId <= 0) return null;

        for (int i = 0; i < circles.size(); i++) {
            if (circles.get(i).getCircleId() == circleId) {
                return circles.get(i);
            }
        }
        return null;
    }

    public int getInfoPosition(long circleId) {
        if (circleId <= 0) return -1;

        for (int i = 0; i < circles.size(); i++) {
            if (circles.get(i).getCircleId() == circleId) {
                return i;
            }
        }
        return -1;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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
            viewHolder.setMessageItemVisibility(messageCount > 0 ? View.VISIBLE : View.GONE);
        } else if (holder instanceof CircleIndexItemViewHolder) {
            Circle info = circles.get(position);
            CircleIndexItemViewHolder viewHolder = (CircleIndexItemViewHolder) holder;
            viewHolder.setItemBinding(itemListener, position, info);

            if (info.getPublishUserId() == myUserId) {
                viewHolder.getItemBinding().ivDelete.setVisibility(View.VISIBLE);
            } else {
                viewHolder.getItemBinding().ivDelete.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position,
                                 List<Object> payloads) {
        if (payloads != null && payloads.size() > 0) {
            InfoPayload payload = (InfoPayload) payloads.get(0);
            CircleIndexItemViewHolder viewHolder = (CircleIndexItemViewHolder) holder;
            switch (payload.operateType) {
                case InfoPayload.ADD_LIKE:
                    viewHolder.setLikeInfo(payload.like, itemListener);
                    break;
                case InfoPayload.CANCEL_LIKE:
                    viewHolder.cancelLikeInfo(payload.like);
                    break;
                case InfoPayload.ADD_COMMENT:
                    viewHolder.setCommentInfo(payload.comment);
                    break;
                case InfoPayload.CANCEL_COMMENT:
                    viewHolder.deleteCommentInfo(payload.comment);
                    break;
            }
        } else {
            super.onBindViewHolder(holder, position, null);
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

    public void updateMsgcount() {
        for (int i = 0; i < circles.size(); i++) {
            if (circles.get(i).getItemType() == DBConstant.CIRCLE_ITEM_MSGCOUNT) {
                notifyItemChanged(i);
                return;
            }
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

    public void updateInfos(List<Circle> infos) {
        if (infos == null || infos.size() <= 0) return;

        if (getItemCount() <= 0) {
            circles.addAll(infos);
        } else {
            removeEmptyInfo();
            circles.removeAll(infos);
            circles.addAll(infos);
        }
        sortInfos(circles);
        notifyDataSetChanged();
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
                if (arg0.getCircleId() == 0 || arg1.getCircleId() == 0) return 0;
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

    public void addComment(int position, CircleComment comment) {
        if (comment == null || comment.getCircleId() <= 0 || comment.getCommentUserId() <= 0)
            return;

        Circle info = circles.get(position);
        if (info == null) return;

        info.getComments().add(comment);
        info.setUpdateTime(comment.getLastUpdateTime());
        notifyItemChanged(position, new InfoPayload(InfoPayload.ADD_COMMENT, comment));
    }

    public void deleteComment(int position, CircleComment comment) {
        if (comment == null || comment.getCircleId() <= 0)
            return;

        Circle info = circles.get(position);
        if (info == null) return;

        info.getComments().remove(comment);
        info.setUpdateTime(comment.getLastUpdateTime());
        notifyItemChanged(position, new InfoPayload(InfoPayload.CANCEL_COMMENT, comment));
    }

    public void setLike(int position, CircleLike likeInfo, boolean isLike) {
        Circle info = circles.get(position);
        if (info == null) return;

        if (isLike) {
            info.setIsLiked(true);
            info.setUpdateTime(likeInfo.getLastUpdateTime());
            info.getLikes().add(likeInfo);
            notifyItemChanged(position, new InfoPayload(InfoPayload.ADD_LIKE, likeInfo));
        } else {
            info.setIsLiked(false);
            info.setUpdateTime(likeInfo.getLastUpdateTime());
            info.getLikes().remove(likeInfo);
            notifyItemChanged(position, new InfoPayload(InfoPayload.CANCEL_LIKE, likeInfo));
        }
    }

    public void remove(int position) {
        circles.remove(position);
        notifyItemRangeRemoved(position, 1);
        notifyItemRangeChanged(position, circles.size() - 1);
    }

    @Override
    public int getItemCount() {
        return circles.size();
    }

    private class InfoPayload {
        public static final int ADD_LIKE = 1;
        public static final int CANCEL_LIKE = 2;
        public static final int ADD_COMMENT = 3;
        public static final int CANCEL_COMMENT = 4;

        public int operateType;
        public CircleLike like;
        public CircleComment comment;

        public InfoPayload(int operateType, CircleLike like) {
            this.operateType = operateType;
            this.like = like;
        }

        public InfoPayload(int operateType, CircleComment comment) {
            this.operateType = operateType;
            this.comment = comment;
        }
    }
}
