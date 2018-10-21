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
import com.hwl.beta.ui.common.FriendComparator;
import com.hwl.beta.ui.user.bean.ImageViewBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/12/28.
 */

public class FriendAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List<Friend> users;
    UserLetterItemBinding itemBinding;
    FriendComparator pinyinComparator;

    public FriendAdapter(Context context) {
        this(context, new ArrayList<Friend>());
    }

    public FriendAdapter(Context context, List<Friend> users) {
        this.context = context;
        this.users = users;
        inflater = LayoutInflater.from(context);
        pinyinComparator = new FriendComparator();
    }

    public void deleteFriend(long friendId) {
        for (int i = 0; i < users.size(); i++) {
            if (friendId == users.get(i).getId()) {
                this.users.remove(i);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void updateFriendRemark(long friendId, String remark) {
        if (friendId <= 0) return;
        Friend friend = null;
        for (int i = 0; i < users.size(); i++) {
            if (friendId == users.get(i).getId()) {
                friend = users.get(i);
                friend.setRemark(remark);
                break;
            }
        }
        if (friend == null) return;
        Collections.sort(users, pinyinComparator);
        notifyDataSetChanged();
    }

    public void addFriend(Friend friend) {
        if (friend == null) return;
        boolean isExists = false;
        for (int i = 0; i < users.size(); i++) {
            if (friend.getId() == users.get(i).getId()) {
                isExists = true;
                break;
            }
        }
        if (!isExists) {
            users.add(friend);
            Collections.sort(users, pinyinComparator);
            notifyDataSetChanged();
        }
    }

    public void addFriends(List<Friend> friends) {
        if (friends == null) return;
        this.users.addAll(friends);
        Collections.sort(users, pinyinComparator);
        notifyDataSetChanged();
    }

    public List<Friend> getFriends() {
        return this.users;
    }

    public Friend getFirendRequestItem() {
        if (this.users != null && this.users.size() >= 3)
            return this.users.get(2);
        return null;
    }

    public int getFriendCount() {
        return users.size() - 3;
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
