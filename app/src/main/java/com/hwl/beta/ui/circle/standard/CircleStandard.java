package com.hwl.beta.ui.circle.standard;

import com.hwl.beta.db.entity.Circle;

import java.util.List;

import io.reactivex.Observable;

public interface CircleStandard {

    Observable<List<Circle>> loadLocalInfos();

    Observable<List<Circle>> loadServerInfos(long minCircleId, List<Circle> localInfos);

    Observable deleteInfo(long minCircleId);

    Observable setLike(long minCircleId,boolean isLike);
}
