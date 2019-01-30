package com.hwl.beta.ui.near.standard;

import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.im.common.DefaultConsumer;

import java.util.List;

public interface NearStandard {

    void loadLocalInfos(DefaultCallback<List<NearCircle>, String> callback);

    void loadServerInfos(long minNearCircleId,List<NearCircle> localInfos,DefaultCallback<List<NearCircle>, String> callback);

	void deleteInfo(long nearCircleId,DefaultCallback<boolean, String> callback);

	void setLike(long nearCircleId,boolean isLike,DefaultCallback<boolean, String> callback);
}
