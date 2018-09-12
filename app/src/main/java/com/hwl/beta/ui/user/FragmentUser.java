package com.hwl.beta.ui.user;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.hwl.beta.R;
import com.hwl.beta.databinding.FragmentUserBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.net.ResponseBase;
import com.hwl.beta.net.user.NetUserFriendInfo;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.GetFriendsResponse;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.BaseFragment;
import com.hwl.beta.ui.common.FriendComparator;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.widget.SideBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/27.
 */

public class FragmentUser extends BaseFragment {
    FragmentUserBinding binding;
//    List<Friend> users;
//    int friendCount = 0;
//    FriendAdapter friendAdapter;
    Activity activity;
//    FriendComparator pinyinComparator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        activity = getActivity();
//        pinyinComparator = new FriendComparator();
//        users = new ArrayList<>();
//        friendAdapter = new FriendAdapter(activity, users);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false);
//        binding.setFriendAdapter(friendAdapter);
//        initView();
//
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
        return binding.getRoot();
    }

    @Override
    protected void onFragmentFirstVisible() {
//        binding.pbLoading.setVisibility(View.VISIBLE);
//        Observable.just(1)
//                .map(new Function<Integer, Boolean>() {
//                    @Override
//                    public Boolean apply(Integer integer) throws Exception {
//                        initUsers();
//                        return true;
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean aBoolean) throws Exception {
//                        binding.pbLoading.setVisibility(View.GONE);
//                        if (aBoolean) {
//                            loadServerFriendInfo();
//                            friendAdapter.notifyDataSetChanged();
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        binding.pbLoading.setVisibility(View.GONE);
//                    }
//                });

    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateFriendCount(Integer ebType) {
//        if (ebType == EventBusConstant.EB_TYPE_FRIEND_REQUEST_UPDATE) {
//            if (users.size() >= 3) {
//                Friend user = users.get(2);
//                if (user == null) return;
//                user.setMessageCount(MessageCountSP.getFriendRequestCountDesc());
//            }
//        } else if (ebType == EventBusConstant.EB_TYPE_CIRCLE_MESSAGE_UPDATE) {
//            Friend user = users.get(0);
//            if (user == null) return;
//            user.setMessageCount(MessageCountSP.getCircleMessageCountDesc());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateFriend(Friend friend) {
//        if (friend == null) return;
//        boolean isExists = false;
//        for (int i = 0; i < users.size(); i++) {
//            if (friend.getId() == users.get(i).getId()) {
//                isExists = true;
//                break;
//            }
//        }
//        if (!isExists) {
//            users.add(friend);
//            Collections.sort(users, pinyinComparator);
//            friendAdapter.notifyDataSetChanged();
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void deleteFriend(EventDeleteFriend friend) {
//        if (friend == null || friend.getFriendId() <= 0) return;
//        for (int i = 0; i < users.size(); i++) {
//            if (friend.getFriendId() == users.get(i).getId()) {
//                users.remove(i);
//                friendAdapter.notifyDataSetChanged();
//                break;
//            }
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateRemark(EventUpdateFriendRemark remark) {
//        if (remark == null || remark.getFriendId() <= 0)
//            return;
//        for (int i = 0; i < users.size(); i++) {
//            if (remark.getFriendId() == users.get(i).getId()) {
//                users.get(i).setFirstLetter(remark.getFirstLetter());
//                users.get(i).setRemark(remark.getFriendRemark());
//                friendAdapter.notifyDataSetChanged();
//                break;
//            }
//        }
//    }

    private void initUsers() {
//        List<Friend> friends = DaoUtils.getFriendManagerInstance().getAll();
//        if (friends != null) {
//            users.addAll(friends);
//        }
//        friendCount = users.size();
//        Friend func1 = new Friend();
//        func1.setId(-1);
//        func1.setName("圈子");
//        func1.setFirstLetter("*");
//        func1.setImageRes(R.drawable.circle);
//        func1.setMessageCount("0");
//        users.add(func1);
//
//        Friend func2 = new Friend();
//        func2.setId(-2);
//        func2.setName("群组");
//        func2.setFirstLetter("*");
//        func2.setImageRes(R.drawable.group);
//        func2.setMessageCount("0");
//        users.add(func2);
//
//        Friend func3 = new Friend();
//        func3.setId(-3);
//        func3.setName("新好友");
//        func3.setFirstLetter("*");
//        func3.setImageRes(R.drawable.new_friend);
//        func3.setMessageCount(MessageCountSP.getFriendRequestCountDesc());
//        users.add(func3);
//        Collections.sort(users, pinyinComparator);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
//        binding.lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Friend friend = users.get(position);
//                if (friend.getId() > 0) {
//                    UITransfer.toUserIndexActivity(activity, friend.getId(), friend.getName(),
//                            friend.getHeadImage());
//                } else {
//                    transfer(friend.getId());
//                }
//            }
//
//            private void transfer(long fid) {
//                switch ((int) fid) {
//                    case -1:
//                        UITransfer.toCircleIndexActivity(activity);
//                        break;
//                    case -2:
//                        UITransfer.toGroupActivity(activity);
//                        break;
//                    case -3:
//                        UITransfer.toNewFriendActivity(activity);
//                        break;
//                }
//            }
//        });
//        binding.sidrbarLetter.setTextView(binding.tvLetter);
//        binding.sidrbarLetter.setOnTouchingLetterChangedListener(new SideBar
//                .OnTouchingLetterChangedListener() {
//            @Override
//            public void onTouchingLetterChanged(String letter) {
//                //Toast.makeText(getContext(), letter, Toast.LENGTH_SHORT);
//                // 该字母首次出现的位置
//                int position = friendAdapter.getPositionForSection(letter.charAt(0));
//                if (position != -1) {
//                    binding.lvFriends.setSelection(position);
//                }
//            }
//        });
    }

    private void loadServerFriendInfo() {
//        Log.d("FragmentUser", "friendCount:" + friendCount + "  UserSP:" + UserSP
// .getFriendCount());
//        if (friendCount < UserSP.getFriendCount()) {
//            binding.pbLoading.setVisibility(View.VISIBLE);
//            UserService.getFriends()
//                    .flatMap(new Function<ResponseBase<GetFriendsResponse>,
//                            ObservableSource<NetUserFriendInfo>>() {
//                        @Override
//                        public ObservableSource<NetUserFriendInfo> apply
//                                (ResponseBase<GetFriendsResponse> response) throws Exception {
//                            if (response != null && response.getResponseBody() != null &&
//                                    response.getResponseBody().getUserFriendInfos() != null &&
//                                    response.getResponseBody().getUserFriendInfos().size() > 0) {
//                                return Observable.fromIterable(response.getResponseBody()
//                                        .getUserFriendInfos());
//                            }
//                            return null;
//                        }
//                    })
//                    .filter(new Predicate<NetUserFriendInfo>() {
//                        @Override
//                        public boolean test(NetUserFriendInfo netUserFriendInfo) throws Exception {
//                            if (netUserFriendInfo == null) return false;
//                            for (int i = 0; i < users.size(); i++) {
//                                if (users.get(i).getId() == netUserFriendInfo.getId()) {
//                                    return false;
//                                }
//                            }
//                            return true;
//                        }
//                    })
//                    .doOnNext(new Consumer<NetUserFriendInfo>() {
//                        @Override
//                        public void accept(NetUserFriendInfo netUserFriendInfo) throws Exception {
//                            Friend friend = DBFriendAction.convertToFriendInfo(netUserFriendInfo);
//                            DaoUtils.getFriendManagerInstance().save(friend);
//                            users.add(friend);
//                        }
//                    })
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new DefaultObserver<NetUserFriendInfo>() {
//                        int updateCount = 0;
//
//                        @Override
//                        public void onNext(NetUserFriendInfo info) {
//                            updateCount++;
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            binding.pbLoading.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            if (updateCount > 0) {
//                                friendAdapter.notifyDataSetChanged();
//                            }
//                            binding.pbLoading.setVisibility(View.GONE);
//                        }
//                    });
//        }
    }
}
