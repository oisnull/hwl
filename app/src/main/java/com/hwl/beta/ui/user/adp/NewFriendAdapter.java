package com.hwl.beta.ui.user.adp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hwl.beta.R;
import com.hwl.beta.databinding.UserNewFriendItemBinding;
import com.hwl.beta.db.entity.FriendRequest;
import com.hwl.beta.ui.user.action.INewFriendItemListener;
import com.hwl.beta.ui.user.bean.ImageViewBean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/8.
 */

public class NewFriendAdapter extends BaseAdapter {

    final Context context;
    private LayoutInflater inflater;
    List<FriendRequest> users;
    INewFriendItemListener itemListener;
    UserNewFriendItemBinding itemBinding;

    public NewFriendAdapter(Context context, List<FriendRequest> users, INewFriendItemListener itemListener) {
        this.context = context;
        this.users = users;
        this.itemListener = itemListener;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public FriendRequest getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return users.get(position).getFriendId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            itemBinding = DataBindingUtil.inflate(inflater, R.layout.user_new_friend_item, parent, false);
            convertView = itemBinding.getRoot();
            convertView.setTag(itemBinding);
        } else {
            itemBinding = (UserNewFriendItemBinding) convertView.getTag();
        }
        FriendRequest user = users.get(position);
        itemBinding.setUser(user);
        itemBinding.setAction(itemListener);
        itemBinding.setImage(new ImageViewBean(user.getFriendHeadImage()));
        return convertView;
    }
}
