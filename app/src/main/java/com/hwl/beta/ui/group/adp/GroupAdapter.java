package com.hwl.beta.ui.group.adp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwl.beta.R;
import com.hwl.beta.databinding.GroupHeadItemBinding;
import com.hwl.beta.databinding.GroupItemBinding;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.group.holder.GroupHeadItemViewHolder;
import com.hwl.beta.ui.group.holder.GroupItemViewHolder;
import com.hwl.beta.utils.StringUtils;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<GroupInfo> groups;
    private LayoutInflater inflater;

    public GroupAdapter(Context context, List<GroupInfo> groups) {
        this.groups = groups;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            default:
            case 0:
                return new GroupHeadItemViewHolder((GroupHeadItemBinding) DataBindingUtil.inflate
                        (inflater, R.layout.group_head_item, parent, false));
            case 1:
                return new GroupItemViewHolder((GroupItemBinding) DataBindingUtil.inflate
                        (inflater, R.layout.group_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final GroupInfo groupInfo = groups.get(position);
        if (holder instanceof GroupHeadItemViewHolder) {
            ((GroupHeadItemViewHolder) holder).setItemBinding((groups.size() - 1) + " 个群组");
        } else if (holder instanceof GroupItemViewHolder) {
            ((GroupItemViewHolder) holder).setItemBinding(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UITransfer.toChatGroupActivity(v.getContext(), groupInfo.getGroupGuid());
                }
            }, groupInfo.getGroupImages(), groupInfo.getGroupName());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (StringUtils.isNotBlank(groups.get(position).getGroupGuid()))
            return 1;
        return 0;
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public int getGroupCount() {
        int count = 0;
        for (int i = 0; i < groups.size(); i++) {
            if (!groups.get(i).getIsSystem()) {
                count++;
            }
        }
        return count;
    }

    public void addGroup(GroupInfo groupInfo) {
        if (groupInfo == null) return;

        if (groups.contains(groupInfo)) return;
        groups.add(groupInfo);
        notifyDataSetChanged();
    }

    public void addGroups(List<GroupInfo> groupInfos) {
        if (groupInfos == null || groupInfos.size() <= 0) return;

        groups.removeAll(groupInfos);
        groups.addAll(groupInfos);
        notifyDataSetChanged();
    }

    public void removeGroup(String groupGuid) {
        if (StringUtils.isEmpty(groupGuid)) return;
        for (int i = 0; i < groups.size(); i++) {
            if (groupGuid.equals(groups.get(i).getGroupGuid())) {
                groups.remove(i);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public List<GroupInfo> getGroups() {
        return groups;
    }
}
