package com.hwl.beta.ui.group.adp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hwl.beta.R;
import com.hwl.beta.databinding.GroupUserItemHBinding;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.group.holder.GroupUserItemHViewHolder;

import java.util.List;

public class GroupUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private List<GroupUserInfo> users;

    public GroupUserAdapter(Context context, List<GroupUserInfo> users) {
        this.users = users;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupUserItemHViewHolder(GroupUserItemHBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final GroupUserInfo user = users.get(position);
		if (user.getUserId() == UserSP.getUserId()) {
			user.setUserName(UserSP.getUserName());
			user.setUserImage(UserSP.getUserHeadImage());
		}
        View.OnClickListener clickListener = v -> {
            UITransfer.toUserIndexV2Activity(v.getContext(),
                    user.getUserId(),
                    user.getUserName(),
                    user.getUserImage());
        };
        ((GroupUserItemHViewHolder) holder).setItemBinding(clickListener, user.getUserImage(),
                user.getUserName(), user.getDistance() + "米");
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
