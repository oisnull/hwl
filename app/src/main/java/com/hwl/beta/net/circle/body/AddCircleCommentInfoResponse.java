package com.hwl.beta.net.circle.body;

import com.hwl.beta.net.circle.NetCircleCommentInfo;

public class AddCircleCommentInfoResponse {
    public NetCircleCommentInfo CommentInfo;
    public String CircleLastUpdateTime;

    public NetCircleCommentInfo getCommentInfo() {
        return CommentInfo;
    }

    public String getCircleLastUpdateTime() {
        return CircleLastUpdateTime;
    }
}
