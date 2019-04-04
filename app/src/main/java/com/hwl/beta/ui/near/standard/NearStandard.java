package com.hwl.beta.ui.near.standard;

import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.im.common.DefaultConsumer;

import java.util.List;

public interface NearStandard {

    Observalbe<List<NearCircle>> loadLocalInfos();

    Observalbe<List<NearCircle>> loadServerInfos(long minNearCircleId,List<NearCircle> localInfos);

	Observalbe deleteInfo(long nearCircleId);

	Observalbe setLike(long nearCircleId,boolean isLike);
}
