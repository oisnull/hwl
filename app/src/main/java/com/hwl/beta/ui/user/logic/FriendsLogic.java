package com.hwl.beta.ui.user.logic;

import com.hwl.beta.R;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.GetFriendsResponse;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.convert.DBFriendAction;
import com.hwl.beta.ui.user.standard.FriendsStandard;
import com.hwl.im.common.DefaultConsumer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
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
        List<Friend> friends = DaoUtils.getFriendManagerInstance().getAllFriends();
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
    public Observable<List<Friend>> loadServerFriends(final List<Friend> localFriends) {
        if ((localFriends.size() - 3) >= UserSP.getFriendCount()) {
            return Observable.error(new Throwable("已经是最新的数据了"));
        }

       return UserService.getFriends()
		.map(new Function<GetFriendsResponse,List<Friend>>(){
			@Override
            public List<Friend> apply(GetFriendsResponse response) throws Exception {
               List<Friend> serverFriends = DBFriendAction.convertToFriendInfos(response.getUserFriendInfos());
                if (serverFriends != null) {
                    serverFriends.removeAll(localFriends);
                    DaoUtils.getFriendManagerInstance().saveList(serverFriends);
                }
            }
		});
    }
}
