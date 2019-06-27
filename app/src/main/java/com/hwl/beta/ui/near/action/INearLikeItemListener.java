package com.hwl.beta.ui.near.action;

import android.view.View;

import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleImage;
import com.hwl.beta.db.entity.NearCircleLike;

import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */

public interface INearLikeItemListener {
    void onLikeUserHeadClick(NearCircleLike likeInfo);
}
