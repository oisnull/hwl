package com.hwl.beta.emotion;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class EmotionContainerPagerAdapter extends PagerAdapter {
    private List<GridView> gvs;

    public EmotionContainerPagerAdapter(List<GridView> gvs) {
        this.gvs = gvs;
    }

    @Override
    public int getCount() {
        return gvs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((gvs.get(position)));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(gvs.get(position));
        return gvs.get(position);
    }
}
