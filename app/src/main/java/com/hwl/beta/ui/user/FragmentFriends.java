package com.hwl.beta.ui.user;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.hwl.beta.R;
import com.hwl.beta.databinding.UserFragmentFriendsBinding;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.ui.common.BaseFragment;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.common.rxext.RXDefaultObserver;
import com.hwl.beta.ui.ebus.EventBusConstant;
import com.hwl.beta.ui.ebus.EventMessageModel;
import com.hwl.beta.ui.ebus.bean.EventDeleteFriend;
import com.hwl.beta.ui.ebus.bean.EventUpdateFriendRemark;
import com.hwl.beta.ui.user.adp.FriendAdapter;
import com.hwl.beta.ui.user.logic.FriendsLogic;
import com.hwl.beta.ui.user.standard.FriendsStandard;
import com.hwl.beta.ui.widget.SideBar;
import com.hwl.im.common.DefaultConsumer;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2017/12/27.
 */

public class FragmentFriends extends BaseFragment {
    UserFragmentFriendsBinding binding;
    FriendsStandard friendsStandard;
    FriendAdapter friendAdapter;
    FragmentActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        activity = getActivity();
        friendsStandard = new FriendsLogic();
        friendAdapter = new FriendAdapter(activity);

        binding = UserFragmentFriendsBinding.inflate(inflater, container, false);

        initView();
        return binding.getRoot();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEventMessage(EventMessageModel messageModel) {
        switch (messageModel.getMessageType()) {
            case EventBusConstant.EB_TYPE_FRIEND_REQUEST_UPDATE:
                Friend item = friendAdapter.getFirendRequestItem();
                item.setMessageCount(MessageCountSP.getFriendRequestCountDesc());
                break;
            case EventBusConstant.EB_TYPE_CIRCLE_MESSAGE_UPDATE:
                Friend item2 = friendAdapter.getCircleMessageItem();
                item2.setMessageCount(MessageCountSP.getCircleMessageCountDesc());
                break;
            case EventBusConstant.EB_TYPE_FRIEND_ADD:
                friendAdapter.addFriend((Friend) messageModel.getMessageModel());
                break;
            case EventBusConstant.EB_TYPE_FRIEND_DELETE:
                friendAdapter.deleteFriend(((EventDeleteFriend) messageModel.getMessageModel())
                        .getFriendId());
                break;
            case EventBusConstant.EB_TYPE_FRIEND_UPDATE_REMARK:
                EventUpdateFriendRemark friendRemark = (EventUpdateFriendRemark) messageModel
                        .getMessageModel();
                friendAdapter.updateFriendRemark(friendRemark.getFriendId(), friendRemark
                        .getFriendRemark());
                break;
        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        binding.pbLoading.setVisibility(View.VISIBLE);
        friendsStandard.loadLocalFriends(new DefaultConsumer<List<Friend>>() {
            @Override
            public void accept(List<Friend> friends) {
                friendAdapter.addFriends(friends);
                loadServerFriendInfo();
            }
        }, new DefaultConsumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                binding.pbLoading.setVisibility(View.GONE);
            }
        });
    }

    private void initView() {
        binding.lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ((int) id) {
                    case -1:
                        UITransfer.toCircleIndexActivity(activity);
                        break;
                    case -2:
                        UITransfer.toGroupActivity(activity);
                        break;
                    case -3:
                        UITransfer.toNewFriendActivity(activity);
                        break;
                    default:
                        Friend friend = friendAdapter.getItem(position);
                        UITransfer.toUserIndexV2Activity(activity, friend.getId(), friend.getName(),
                                friend.getHeadImage());
                        break;
                }
            }
        });

        binding.sidrbarLetter.setTextView(binding.tvLetter);
        binding.sidrbarLetter.setOnTouchingLetterChangedListener(new SideBar
                .OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String letter) {
                int position = friendAdapter.getPositionForSection(letter.charAt(0));
                if (position != -1) {
                    binding.lvFriends.setSelection(position);
                }
            }
        });
    }

    private void loadServerFriendInfo() {
        friendsStandard.loadServerFriends(friendAdapter.getFriends())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RXDefaultObserver<List<Friend>>(false) {
                    @Override
                    protected void onSuccess(List<Friend> friends) {
                        binding.pbLoading.setVisibility(View.GONE);
                        friendAdapter.addFriends(friends);
                    }

                    @Override
                    protected void onError(String message) {
                        super.onError(message);
                        binding.pbLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onRelogin() {
                        binding.pbLoading.setVisibility(View.GONE);
                        UITransfer.toReloginDialog(activity);
                    }
                });
    }
}
