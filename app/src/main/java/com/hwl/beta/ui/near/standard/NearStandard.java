package com.hwl.beta.ui.near.standard;

import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.db.entity.NearCircleLike;

import java.util.List;

import io.reactivex.Observable;

public interface NearStandard {

    Observable<List<NearCircle>> loadLocalInfos();

    Observable<List<NearCircle>> loadServerInfos(long minNearCircleId, List<NearCircle> localInfos);

    Observable deleteInfo(long nearCircleId);

    Observable<NearCircleLike> setLike(long nearCircleId, boolean isLike);
}
