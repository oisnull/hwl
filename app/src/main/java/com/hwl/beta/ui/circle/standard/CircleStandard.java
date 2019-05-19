package com.hwl.beta.ui.circle.standard;

import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.CircleLike;

import java.util.List;

import io.reactivex.Observable;

public interface CircleStandard {

    Observable<List<Circle>> loadLocalInfos();

    Observable<List<Circle>> loadServerInfos(long minCircleId, List<Circle> localInfos);

    Observable deleteInfo(long minCircleId);

    Observable<CircleLike> setLike(Circle info, boolean isLike);

	Observable updateCircleBackImage(String localPath);

    Observable<Circle> loadLocalDetails(long circleId);

    Observable<Circle> loadServerDetails(long circleId,String updateTime);
}
