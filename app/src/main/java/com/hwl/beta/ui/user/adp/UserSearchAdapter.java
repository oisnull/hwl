package com.hwl.beta.ui.user.adp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hwl.beta.R;
import com.hwl.beta.databinding.UserSearchItemBinding;
import com.hwl.beta.net.user.UserSearchInfo;
import com.hwl.beta.ui.user.action.IUserSearchItemListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/27.
 */

public class UserSearchAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    UserSearchItemBinding itemBinding;
    List<UserSearchInfo> users;
    IUserSearchItemListener itemListener;

    public UserSearchAdapter(Context context, IUserSearchItemListener itemListener) {
        this(context, new ArrayList<UserSearchInfo>(), itemListener);
    }

    public UserSearchAdapter(Context context, List<UserSearchInfo> users, IUserSearchItemListener
            itemListener) {
        this.context = context;
        this.users = users;
        this.itemListener = itemListener;
        inflater = LayoutInflater.from(context);
    }

    public void clearAndAddUsers(List<UserSearchInfo> users) {
        this.users.clear();
        if (users != null && users.size() > 0)
            this.users.addAll(users);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public UserSearchInfo getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return users.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            itemBinding = UserSearchItemBinding.inflate(inflater, parent, false);
            convertView = itemBinding.getRoot();
            convertView.setTag(itemBinding);
        } else {
            itemBinding = (UserSearchItemBinding) convertView.getTag();
        }
//        itemBinding.setUser(users.get(position));
//        itemBinding.setAction(itemListener);
//        itemBinding.setImage(new ImageViewBean(users.get(position).getHeadImage()));
        return convertView;
    }
}
