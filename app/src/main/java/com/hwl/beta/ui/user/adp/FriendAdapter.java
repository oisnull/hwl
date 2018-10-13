package com.hwl.beta.ui.user.adp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hwl.beta.R;
import com.hwl.beta.databinding.UserLetterItemBinding;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.ui.user.bean.ImageViewBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/28.
 */

public class FriendAdapter extends BaseAdapter {
    final Context context;
    private LayoutInflater inflater;
    List<Friend> users;
    UserLetterItemBinding itemBinding;

    public FriendAdapter(Context context) {
        this.context = context;
        this.users = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    public FriendAdapter(Context context, List<Friend> users) {
        this(context);
        this.users = users;
    }

    public void addFriends(List<Friend> friends) {
        if (friends == null) return;
        this.users.addAll(friends);
        notifyDataSetChanged();
    }

    public Friend getFirendRequestItem() {
        if (this.users != null && this.users.size() >= 3)
            return this.users.get(2);
        return null;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Friend getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return users.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            itemBinding = DataBindingUtil.inflate(inflater, R.layout.user_letter_item, parent,
                    false);
            convertView = itemBinding.getRoot();
            convertView.setTag(itemBinding);
        } else {
            itemBinding = (UserLetterItemBinding) convertView.getTag();
        }
        Friend user = users.get(position);
        itemBinding.setUser(user);
        itemBinding.setImage(new ImageViewBean(user.getHeadImage(), user.getImageRes()));

        if (user.getId() <= 0) {
            itemBinding.tvLetter.setVisibility(View.GONE);
        } else {
            int section = user.getFirstLetter().charAt(0);
            //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                itemBinding.tvLetter.setVisibility(View.VISIBLE);
            } else {
                itemBinding.tvLetter.setVisibility(View.GONE);
            }
        }
        if (user.getMessageCount().equals("0")) {
            itemBinding.tvMsgCount.setVisibility(View.GONE);
        } else {
            itemBinding.tvMsgCount.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = users.get(i).getFirstLetter();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }
}
