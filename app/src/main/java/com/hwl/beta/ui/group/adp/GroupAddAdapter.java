package com.hwl.beta.ui.group.adp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.hwl.beta.R;
import com.hwl.beta.databinding.UserActionItemBinding;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.ui.user.bean.ImageViewBean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/28.
 */

public class GroupAddAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Friend> users;
    private UserActionItemBinding itemBinding;
    private IGroupAddItemListener itemListener;
    private List<GroupUserInfo> groupUsers;

    public GroupAddAdapter(Context context, List<Friend> users, IGroupAddItemListener
            itemListener) {
        this.context = context;
        this.users = users;
        this.itemListener = itemListener;
        inflater = LayoutInflater.from(context);
    }

    public void setGroupUsers(List<GroupUserInfo> groupUsers) {
        this.groupUsers = groupUsers;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            itemBinding = DataBindingUtil.inflate(inflater, R.layout.user_action_item, parent,
                    false);
            convertView = itemBinding.getRoot();
            convertView.setTag(itemBinding);
        } else {
            itemBinding = (UserActionItemBinding) convertView.getTag();
        }
        final Friend user = users.get(position);
        itemBinding.setUser(user);
        itemBinding.setImage(new ImageViewBean(user.getHeadImage()));

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

        if (groupUsers != null && groupUsers.size() > 0 && isExistsGroupUser(user.getId())) {
            itemBinding.tvSelect.setVisibility(View.VISIBLE);
            itemBinding.cbSelect.setVisibility(View.GONE);
        } else {
            itemBinding.tvSelect.setVisibility(View.GONE);
            itemBinding.cbSelect.setVisibility(View.VISIBLE);
            itemBinding.cbSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onCheckBoxClick(v, user, position);
                }
            });
        }
        return convertView;
    }

    private boolean isExistsGroupUser(long userId) {
        for (int i = 0; i < groupUsers.size(); i++) {
            if (groupUsers.get(i).getUserId() == (userId)) {
                return true;
            }
        }
        return false;
    }

    public boolean setCheckBox(View rootView) {
        CheckBox cbSelect = rootView.findViewById(R.id.cb_select);
        if (cbSelect == null) return false;
        if (cbSelect.isChecked()) {
            cbSelect.setChecked(false);
            return false;
        } else {
            cbSelect.setChecked(true);
            return true;
        }
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

    public interface IGroupAddItemListener {
        void onCheckBoxClick(View v, Friend friend, int position);
    }
}
