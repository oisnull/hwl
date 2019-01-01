package com.hwl.beta.ui.chat.adp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.hwl.beta.R;
import com.hwl.beta.badge.Badge;
import com.hwl.beta.badge.QBadgeView;
import com.hwl.beta.databinding.ChatRecordItemBinding;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.ChatRecordMessageComparator;
import com.hwl.beta.ui.immsg.IMConstant;
import com.hwl.beta.ui.widget.BadgeNumber;
import com.hwl.beta.utils.DateUtils;
import com.hwl.beta.utils.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2018/4/2.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<ChatRecordMessage> records;
    private IAdapterListener adapterListener;
    private ChatRecordMessageComparator dateComparator;
    private int totalMessageCount = 0;
    private long myUserId;

    public RecordAdapter(Context context, List<ChatRecordMessage> records, IAdapterListener
            adapterListener) {
        this.context = context;
        this.records = records;
        this.adapterListener = adapterListener;
        inflater = LayoutInflater.from(context);
        dateComparator = new ChatRecordMessageComparator();
        myUserId = UserSP.getUserId();
    }

    public ChatRecordMessage getRecordMessage(int position) {
        return this.records.get(position);
    }

    public void updateRecord(ChatRecordMessage record) {
        this.updateRecord(record, true);
    }

    public void updateRecord(ChatRecordMessage record, boolean isSort) {
        if (record == null || !record.hasRecordId()) return;
        int position = records.indexOf(record);
        if (position != -1) {
            records.remove(position);
        }
        records.add(record);

        if (isSort) {
            Collections.sort(records, dateComparator);
            notifyItemRangeChanged(0, records.size());
        } else {
            notifyItemChanged(position);
        }
    }

    public void removeRecord(String groupGuid) {
        if (StringUtils.isEmpty(groupGuid)) return;
        for (int i = 0; i < records.size(); i++) {
            if (groupGuid.equals(records.get(i).getGroupGuid())) {
                records.remove(i);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void removeRecord(long recordId) {
        if (recordId <= 0) return;
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getRecordId() == recordId) {
                records.remove(i);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void updateGroupName(String groupGuid, String groupName) {
        if (StringUtils.isEmpty(groupGuid)) return;
        for (int i = 0; i < records.size(); i++) {
            if (groupGuid.equals(records.get(i).getGroupGuid())) {
                records.get(i).setGroupName(groupName);
                records.get(i).setTitle(groupName);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void updateFriendRemark(long friendId, String remark) {
        if (friendId <= 0) return;
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getFromUserId() == friendId && records.get(i)
                    .getRecordType() == IMConstant.CHAT_RECORD_TYPE_USER) {
                if (!records.get(i).getFromUserName().equals(remark)) {
                    records.get(i).setFromUserName(remark);
                    records.get(i).setTitle(remark);
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    @Override
    public RecordAdapter.RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecordViewHolder((ChatRecordItemBinding) DataBindingUtil.inflate(inflater, R
                .layout.chat_record_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecordAdapter.RecordViewHolder holder, final int position) {
        ChatRecordItemBinding itemBinding = holder.getItemBinding();
        ChatRecordMessage record = records.get(position);
        itemBinding.setRecord(record);
        itemBinding.setPosition(position);
        itemBinding.tvTime.setText(DateUtils.getChatShowTime(record.getSendTime()));
        itemBinding.ivNotify.setVisibility(record.isShield() ? View.VISIBLE : View.GONE);
        if (record.isShield()) {
            if (record.getUnreadCount() > 0)
                holder.badge.setBadgeText("");
        } else {
            holder.badge.setBadgeNumber(record.getUnreadCount());
        }

        switch (record.getRecordType()) {
            case IMConstant.CHAT_RECORD_TYPE_GROUP:
                itemBinding.ivGroupImage.setVisibility(View.VISIBLE);
                itemBinding.ivRecordImage.setVisibility(View.GONE);
                itemBinding.ivGroupImage.setImagesData(record.getGroupUserImages());
                break;
            default:
                itemBinding.ivGroupImage.setVisibility(View.GONE);
                itemBinding.ivRecordImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(record.getRecordImage(myUserId))
                        .placeholder(R.drawable.empty_photo)
                        .error(R.drawable.empty_photo)
                        .into(itemBinding.ivRecordImage);
                break;
        }

        if (position == 0) {
            totalMessageCount = 0;
        }

        if (record.getUnreadCount() > 0) {
            totalMessageCount = record.getUnreadCount() + totalMessageCount;
        }

        if ((position + 1) == getItemCount() && adapterListener != null) {
            adapterListener.onLoadComplete(totalMessageCount);
        }
        itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterListener.onItemClick(position);
            }
        });
        itemBinding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                adapterListener.onItemLongClick(v, position);
                return false;
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return records.get(position).getRecordId();
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public void updateGroupImage(String groupGuid, List<String> groupUserImages) {
        if (StringUtils.isBlank(groupGuid) || groupUserImages == null || groupUserImages.size()
                <= 0)
            return;
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getRecordType() == IMConstant.CHAT_RECORD_TYPE_GROUP && records
                    .get(i).getGroupGuid().equals(groupGuid)) {
//                records.get(i).setGroupUserImages(groupUserImages);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public interface IAdapterListener {
        void onLoadComplete(int messageTotalCount);

        void onItemClick(int position);

        void onItemLongClick(View view, int position);
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder {
        ChatRecordItemBinding itemBinding;
        Badge badge;

        public RecordViewHolder(ChatRecordItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            badge = BadgeNumber.bindBadgeView(context, itemBinding.llImageContainer);
            badge = new QBadgeView(context).bindTarget(itemBinding.llImageContainer);
            badge.setBadgeGravity(Gravity.TOP | Gravity.END);
//            badge.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
//                @Override
//                public void onDragStateChanged(int dragState, Badge badge, View targetView) {
//                    if (dragState == STATE_SUCCEED) {
//                    }
//                }
//            });
        }

        public ChatRecordItemBinding getItemBinding() {
            return itemBinding;
        }
    }
}
