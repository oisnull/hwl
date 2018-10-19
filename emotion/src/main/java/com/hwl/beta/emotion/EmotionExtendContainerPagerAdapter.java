package com.hwl.beta.emotion;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class EmotionExtendContainerPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> datas=null;

    public EmotionExtendContainerPagerAdapter(FragmentManager fm, List<Fragment> datas) {
        super(fm);
        this.datas=datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Fragment getItem(int position) {
        return datas.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
