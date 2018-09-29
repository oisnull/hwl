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
import com.hwl.beta.databinding.UserFragmentFriendsBinding;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.ui.common.BaseFragment;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.user.adp.FriendAdapter;
import com.hwl.beta.ui.user.logic.FriendsLogic;
import com.hwl.beta.ui.user.standard.FriendsStandard;
import com.hwl.beta.ui.widget.SideBar;
import com.hwl.im.common.DefaultConsumer;

import java.util.List;

/**
 * Created by Administrator on 2017/12/27.
 */

public class FragmentFriends extends BaseFragment {
    UserFragmentFriendsBinding binding;
    FriendsStandard friendsStandard;
    FriendAdapter friendAdapter;
    Activity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        activity = getActivity();
        friendsStandard = new FriendsLogic();
        friendAdapter = new FriendAdapter(activity);

        binding = DataBindingUtil.inflate(inflater, R.layout.user_fragment_friends, container,
                false);
        binding.setFriendAdapter(friendAdapter);

        initView();
        return binding.getRoot();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void onFragmentFirstVisible() {
        binding.pbLoading.setVisibility(View.VISIBLE);
        friendsStandard.loadLocalFriends(new DefaultConsumer<List<Friend>>() {
            @Override
            public void accept(List<Friend> friends) {
                binding.pbLoading.setVisibility(View.GONE);
                friendAdapter.addFriends(friends);
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
                if (id > 0) {
//                    UITransfer.toUserIndexActivity(activity, friend.getId(), friend.getName(),
//                            friend.getHeadImage());
                } else {
                    transfer(id);
                }
            }

            private void transfer(long fid) {
                switch ((int) fid) {
                    case -1:
//                        UITransfer.toCircleIndexActivity(activity);
                        break;
                    case -2:
//                        UITransfer.toGroupActivity(activity);
                        break;
                    case -3:
                        UITransfer.toNewFriendActivity(activity);
                        break;
                }
            }
        });
        binding.sidrbarLetter.setTextView(binding.tvLetter);
        binding.sidrbarLetter.setOnTouchingLetterChangedListener(new SideBar
                .OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String letter) {
                //Toast.makeText(getContext(), letter, Toast.LENGTH_SHORT);
                // 该字母首次出现的位置
                int position = friendAdapter.getPositionForSection(letter.charAt(0));
                if (position != -1) {
                    binding.lvFriends.setSelection(position);
                }
            }
        });
    }

    private void loadServerFriendInfo() {
//        Log.d("FragmentFriends", "friendCount:" + friendCount + "  UserSP:" + UserSP
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
//                        public boolean test(NetUserFriendInfo netUserFriendInfo) throws
// Exception {
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
