package com.hwl.beta.ui.near.holder;

/**
 * Created by Administrator on 2019/06/26.
 */

public class UserLikeOperate {

	private static FlexboxLayout.LayoutParams getDefaultLayoutParams(Context context){
		int size = DisplayUtils.dp2px(context, 25);
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(size, size);
        params.rightMargin = 2;
        params.bottomMargin = 2;
		return params;
	}

    public static void setLikeInfos(FlexboxLayout fblLikeContainer,List<NearCircleLike> likes,INearLikeItemListener itemListener) {
        if (likes == null || likes.size() <= 0) return;
		
		Context context=fblLikeContainer.getContext();
		FlexboxLayout.LayoutParams param =getDefaultLayoutParams(context);
        for (int i = 0; i < likes.size(); i++) {
			ImageView iv = createLikeView(context,likes.get(i),itemListener);
            fblLikeContainer.addView(iv, param);
        }
    }

	public static void setLikeInfo(FlexboxLayout fblLikeContainer,NearCircleLike like,INearLikeItemListener itemListener){
		if(like==null) return;

		Context context=fblLikeContainer.getContext();
		ImageView iv = createLikeView(context,like,itemListener);
        fblLikeContainer.addView(iv, getDefaultLayoutParams(context));
	}

	public static void cancelLikeInfo(FlexboxLayout fblLikeContainer,long userId){
		if(userId<=0) return;

		int childCount = fblLikeContainer.getChildCount();
		if(childCount<=0) return;
		for (int i=0; i < childCount; i++){
			ImageView iv = fblLikeContainer.getChildAt(i);
			if(((long)iv.getTag(R.id.id_tag_userlike))==userId){
				fblLikeContainer.removeView(i);
				break;
			}
		}
	}

	private static ImageView createLikeView(Context context,NearCircleLike like,INearLikeItemListener itemListener){
		ImageView iv = new ImageView(context);
        ImageViewBean.loadImage(iv, like.getLikeUserImage());
		iv.setTag(R.id.id_tag_userlike,like.getUserId());
		iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onLikeUserHeadClick(like);
                }
            });
		return iv;
	}
}
