package com.hwl.beta.ui.user.logic;

import com.hwl.beta.R;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.net.ResponseBase;
import com.hwl.beta.net.user.NetUserFriendInfo;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.GetFriendsResponse;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.ui.user.standard.FriendsStandard;
import com.hwl.im.common.DefaultConsumer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FriendsLogic implements FriendsStandard {
    @Override
    public void loadLocalFriends(final DefaultConsumer<List<Friend>> succCallback,
                                 final DefaultConsumer<Throwable> errorCallback) {
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) {
                emitter.onNext(getLocalFriends());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(succCallback, errorCallback);

    }

    private List<Friend> getLocalFriends() {
        List<Friend> friends = DaoUtils.getFriendManagerInstance().getAll();
        if (friends == null) {
            friends = new ArrayList<>();
        }
        Friend func1 = new Friend();
        func1.setId(-1);
        func1.setName("圈子");
        func1.setFirstLetter("*");
        func1.setImageRes(R.drawable.circle);
        func1.setMessageCount("0");
        friends.add(0, func1);

        Friend func2 = new Friend();
        func2.setId(-2);
        func2.setName("群组");
        func2.setFirstLetter("*");
        func2.setImageRes(R.drawable.group);
        func2.setMessageCount("0");
        friends.add(1, func2);

        Friend func3 = new Friend();
        func3.setId(-3);
        func3.setName("新好友");
        func3.setFirstLetter("*");
        func3.setImageRes(R.drawable.new_friend);
        func3.setMessageCount(MessageCountSP.getFriendRequestCountDesc());
        friends.add(2, func3);

        return friends;
    }

    @Override
    public List<Friend> getServerFriends() {
//        UserService.getFriends()
//                .flatMap(new Function<ResponseBase<GetFriendsResponse>,
//                                        ObservableSource<NetUserFriendInfo>>() {
//                    @Override
//                    public ObservableSource<NetUserFriendInfo> apply
//                            (ResponseBase<GetFriendsResponse> response) throws Exception {
//                        if (response != null && response.getResponseBody() != null &&
//                                response.getResponseBody().getUserFriendInfos() != null &&
//                                response.getResponseBody().getUserFriendInfos().size() > 0) {
//                            return Observable.fromIterable(response.getResponseBody()
//                                    .getUserFriendInfos());
//                        }
//                        return null;
//                    }
//                })
//                .filter(new Predicate<NetUserFriendInfo>() {
//                    @Override
//                    public boolean test(NetUserFriendInfo netUserFriendInfo) throws Exception {
//                        if (netUserFriendInfo == null) return false;
//                        for (int i = 0; i < users.size(); i++) {
//                            if (users.get(i).getId() == netUserFriendInfo.getId()) {
//                                return false;
//                            }
//                        }
//                        return true;
//                    }
//                })
//                .doOnNext(new Consumer<NetUserFriendInfo>() {
//                    @Override
//                    public void accept(NetUserFriendInfo netUserFriendInfo) throws Exception {
//                        Friend friend = DBFriendAction.convertToFriendInfo(netUserFriendInfo);
//                        DaoUtils.getFriendManagerInstance().save(friend);
//                        users.add(friend);
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DefaultObserver<NetUserFriendInfo>() {
//                    int updateCount = 0;
//
//                    @Override
//                    public void onNext(NetUserFriendInfo info) {
//                        updateCount++;
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        binding.pbLoading.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        if (updateCount > 0) {
//                            friendAdapter.notifyDataSetChanged();
//                        }
//                        binding.pbLoading.setVisibility(View.GONE);
//                    }
//                });

        return null;
    }
}
