package com.hwl.beta.emotion;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.viewpager.widget.PagerAdapter;

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
        container.removeView((gvs.get(position)));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(gvs.get(position));
        return gvs.get(position);
    }
}
