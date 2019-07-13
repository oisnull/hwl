package com.hwl.beta.net.circle.body;

import com.hwl.beta.net.circle.NetCircleCommentInfo;

import java.util.List;

public class GetCircleCommentsResponse {
    public List<NetCircleCommentInfo> getCircleCommentInfos() {
        return CircleCommentInfos;
    }

    private List<NetCircleCommentInfo> CircleCommentInfos;

}
