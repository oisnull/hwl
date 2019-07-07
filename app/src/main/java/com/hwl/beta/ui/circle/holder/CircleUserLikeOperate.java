package com.hwl.beta.ui.circle.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.google.android.flexbox.FlexboxLayout;
import com.hwl.beta.R;
import com.hwl.beta.db.entity.CircleLike;
import com.hwl.beta.ui.circle.action.ICircleLikeItemListener;
import com.hwl.beta.ui.user.bean.ImageViewBean;
import com.hwl.beta.utils.DisplayUtils;

import java.util.List;

/**
 * Created by Administrator on 2019/07/03.
 */

public class CircleUserLikeOperate {

    private static FlexboxLayout.LayoutParams getDefaultLayoutParams(Context context) {
        int size = DisplayUtils.dp2px(context, 25);
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(size, size);
        params.rightMargin = 2;
        params.bottomMargin = 2;
        return params;
    }

    public static void setLikeInfos(FlexboxLayout fblLikeContainer, List<CircleLike> likes,
                                    ICircleLikeItemListener itemListener) {
        if (likes == null || likes.size() <= 0) return;

        if (!fblLikeContainer.isShown()) {
            fblLikeContainer.setVisibility(View.VISIBLE);
        }

        Context context = fblLikeContainer.getContext();
        FlexboxLayout.LayoutParams param = getDefaultLayoutParams(context);
        for (int i = 0; i < likes.size(); i++) {
            ImageView iv = createLikeView(context, likes.get(i), itemListener);
            fblLikeContainer.addView(iv, param);
        }
    }

    public static void setLikeInfo(FlexboxLayout fblLikeContainer, CircleLike like,
                                   ICircleLikeItemListener itemListener) {
        if (like == null) return;

        if (isExists(fblLikeContainer, like.getLikeUserId())) return;

        if (!fblLikeContainer.isShown()) {
            fblLikeContainer.setVisibility(View.VISIBLE);
        }

        Context context = fblLikeContainer.getContext();
        ImageView iv = createLikeView(context, like, itemListener);
        fblLikeContainer.addView(iv, getDefaultLayoutParams(context));
    }

    public static void cancelLikeInfo(FlexboxLayout fblLikeContainer, long userId) {
        if (userId <= 0) return;

        int childCount = fblLikeContainer.getChildCount();
        if (childCount <= 0) return;
        for (int i = 0; i < childCount; i++) {
            View iv = fblLikeContainer.getChildAt(i);
            if (((long) iv.getTag(R.id.id_tag_userlike)) == userId) {
                fblLikeContainer.removeViewAt(i);
                break;
            }
        }
    }

    private static boolean isExists(FlexboxLayout fblLikeContainer, long userId) {
        int childCount = fblLikeContainer.getChildCount();
        if (childCount <= 0) return false;
        for (int i = 0; i < childCount; i++) {
            View iv = fblLikeContainer.getChildAt(i);
            if (((long) iv.getTag(R.id.id_tag_userlike)) == userId) {
                return true;
            }
        }
        return false;
    }

    private static ImageView createLikeView(Context context, final CircleLike like,
                                            final ICircleLikeItemListener itemListener) {
        ImageView iv = new ImageView(context);
        ImageViewBean.loadImage(iv, like.getLikeUserImage());
        iv.setTag(R.id.id_tag_userlike, like.getLikeUserId());
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onLikeUserHeadClick(like);
            }
        });
        return iv;
    }
}
