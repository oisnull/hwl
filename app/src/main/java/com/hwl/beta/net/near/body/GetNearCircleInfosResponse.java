package com.hwl.beta.net.near.body;

import com.hwl.beta.net.near.NetNearCircleInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/3/8.
 */

public class GetNearCircleInfosResponse {

    public List<NetNearCircleInfo> getNearCircleInfos() {
        return NearCircleInfos;
    }

    private List<NetNearCircleInfo> NearCircleInfos;
}
