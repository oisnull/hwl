package com.hwl.beta.ui.circle.standard;

import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.Friend;

import java.util.List;

import io.reactivex.Observable;

public interface CircleUserStandard {

    Observable<List<Circle>> loadLocalInfos();

    Observable<List<Circle>> loadServerInfos(long minCircleId,List<Circle> localInfos);

    Observable deleteInfo(long minCircleId);

    Friend getLocalUserInfo();

    Observable loadServerUserInfo();
}
