package com.hwl.beta.emotion.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;

import com.hwl.beta.emotion.interfaces.IDefaultEmotionListener;
import com.hwl.beta.emotion.model.EmojiPageContainer;
import com.hwl.beta.emotion.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

public class EmotionPagerAdapter extends PagerAdapter {
    private List<View> views = new ArrayList<>();
    private List<EmojiPageContainer> pageContainers = new ArrayList<>();
    private Activity context;
	private IDefaultEmotionListener emotionListener;

    public EmotionPagerAdapter(Context context) {
        this.context = (Activity) context;
    }

    public void setEmotionListener(IDefaultEmotionListener emotionListener) {
        this.emotionListener = emotionListener;
    }

    public void add(EmojiPageContainer pageContainer) {
        pageContainers.add(pageContainer);
        if (views.size() <= 0) {
            createView(0);
        }
    }

    public void clear() {
        pageContainers.clear();
        views.clear();
    }

	public String getPageTag(int position){
		View view = views.get(position);
		if(view==null) return null;

		return view.getTag().toString();
	}

    @Override
    public int getCount() {
        int count = 0;
        for (EmojiPageContainer pageSetEntity : pageContainers) {
            count += pageSetEntity.getPageCount();
        }
        return count;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view;
        if ((position + 1) <= views.size()) {
            view = views.get(position);
        } else {
            view = createView(position);
        }
        Log.d("instantiateItem=", view.getTag().toString());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public EmojiPageContainer getPageContainer(int position) {
        for (EmojiPageContainer pageSetEntity : pageContainers) {
            if (pageSetEntity.getPageCount() > position) {
                return pageSetEntity;
            } else {
                position -= pageSetEntity.getPageCount();
            }
        }
        return null;
    }

    private View createView(int position) {
        EmojiPageContainer container = getPageContainer(position);
        if (container == null) return null;

        int screenWidth = DisplayUtils.getScreenWidthPixels(context);
        int itemPadding = DisplayUtils.dp2px(context, 10);
        int itemWidth = (screenWidth - itemPadding * 8) / container.getLine();

        for (int i = 0; i < container.getPageCount(); i++) {
            GridView gvContainer = new GridView(context);
            gvContainer.setTag(container.getId());
            gvContainer.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            gvContainer.setNumColumns(container.getLine());
            gvContainer.setPadding(itemPadding, itemPadding, itemPadding, itemPadding);
            gvContainer.setHorizontalSpacing(itemPadding);
            gvContainer.setVerticalSpacing(itemPadding * 2);
            gvContainer.setAdapter(new EmotionGridAdapter(context,
                    container.getEmojiPages().get(i),
                    itemWidth,
                    emotionListener));
            views.add(gvContainer);
        }

        return views.get(position);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
