package com.hwl.beta.ui.chat.adp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwl.beta.R;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.user.bean.ImageViewBean;
import com.hwl.beta.utils.StringUtils;

import java.util.List;

public class ChatGroupUserAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<GroupUserInfo> users;

    public ChatGroupUserAdapter(Context context, List<GroupUserInfo> users) {
        this.users = users;
        inflater = LayoutInflater.from(context);
    }

    public void addUsers(List<GroupUserInfo> userInfos) {
        if (userInfos == null) return;
        for (int i = 0; i < userInfos.size(); i++) {
            if (!users.contains(userInfos.get(i))) {
                users.add(0, userInfos.get(i));
            }
        }
        notifyDataSetChanged();
    }

    public int getUserCount() {
        int count = 0;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId() > 0) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public GroupUserInfo getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return users.get(position).getId() == null ? 0 : users.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupUserInfo user = users.get(position);
		if (user.getUserId() == UserSP.getUserId()) {
			user.setUserName(UserSP.getUserName());
			user.setUserImage(UserSP.getUserHeadImage());
		}
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.group_user_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivHeader = convertView.findViewById(R.id.iv_header);
            viewHolder.tvName = convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (user.getId() != null && user.getId() == -1) {
            viewHolder.ivHeader.setBackgroundResource(R.drawable.layout_border);
            viewHolder.ivHeader.setImageResource(R.drawable.ic_add);
            viewHolder.tvName.setText("");
        } else {
            viewHolder.ivHeader.setBackgroundResource(0);
            ImageViewBean.loadImage(viewHolder.ivHeader, user.getUserImage());
            if (StringUtils.isBlank(user.getUserName())) {
                viewHolder.tvName.setText("--");
            } else {
                viewHolder.tvName.setText(StringUtils.cutString(user.getUserName(), 5));
            }
        }
        return convertView;
    }


    class ViewHolder {
        ImageView ivHeader;
        TextView tvName;
    }
}
