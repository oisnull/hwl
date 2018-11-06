package com.hwl.beta.emotion;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.hwl.beta.emotion.utils.DisplayUtils;
import com.hwl.beta.emotion.utils.EmotionUtils;
import com.hwl.beta.emotion.widget.DotView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class EmotionTemplateFragment extends Fragment {
    private static final String TAG = "EmotionTemplateFragment";
    private int screenWidth;//当前activity屏幕的宽度
    private int padding;
    private int itemWidth;
    private int gvHeight;
    IDefaultEmotionListener defaultEmotionListener;
    protected Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_emotion_template, container, false);
        initView(rootView);
        return rootView;
    }

    public void setDefaultEmotionListener(IDefaultEmotionListener defaultEmotionListener) {
        this.defaultEmotionListener = defaultEmotionListener;
    }

    private void calc() {
        // 获取屏幕宽度
        screenWidth = DisplayUtils.getScreenWidthPixels(getActivity());
        // item的间距
        padding = DisplayUtils.dp2px(getActivity(), 10);
        // 动态计算item的宽度和高度
        itemWidth = (screenWidth - padding * 8) / 7;//DisplayUtils.dp2px(getActivity(), 40);
        //动态计算gridview的总高度
        gvHeight = itemWidth * 3 + padding * 6;
    }

    public void initView(View view) {

        calc();

        ViewPager vpEmotionContainer = view.findViewById(R.id.vp_emotion_container);
        final DotView dvDots = view.findViewById(R.id.dv_dots);

        List<GridView> views = getEmotionContainerDataList();
        dvDots.load(views.size());

        vpEmotionContainer.setAdapter(new EmotionContainerPagerAdapter(views));
        vpEmotionContainer.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, gvHeight));
        vpEmotionContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currIndex = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                dvDots.setSelectDot(position, currIndex);
                currIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * 加载表情面板
     * 思路：获取表情的总数，按每行存放7个表情，动态计算出每个表情所占的宽度大小（包含间距），
     * 而每个表情的高与宽应该是相等的，这里我们约定只存放3行
     * 每个面板最多存放7*3=21个表情，再减去一个删除键，即每个面板包含20个表情
     * 根据表情总数，循环创建多个容量为20的List，存放表情，对于大小不满20进行特殊
     * 处理即可。
     */
    private List<GridView> getEmotionContainerDataList() {
        List<GridView> views = new ArrayList<>();
        List<String> emotionNames = new ArrayList<>(20);
        for (String name : EmotionUtils.getDefaultEmotionMap().keySet()) {
            if (emotionNames.size() == 20) {
                //创建表情容器对象
                GridView gvContainer = createEmotionContainer(emotionNames, screenWidth, padding, itemWidth, gvHeight);
                views.add(gvContainer);
                //重新初始化表情列表
                emotionNames = new ArrayList<>(20);
            } else {
                emotionNames.add(name);
            }
        }
        if (emotionNames.size() > 0) {
            //创建表情容器对象
            GridView gvContainer = createEmotionContainer(emotionNames, screenWidth, padding, itemWidth, gvHeight);
            views.add(gvContainer);
        }
        return views;
    }

    private GridView createEmotionContainer(List<String> emotionNames, int gvWidth, int padding, int itemWidth, int gvHeight) {
        GridView gvContainer = new GridView(context);
        gvContainer.setNumColumns(7);
        gvContainer.setPadding(padding, padding, padding, padding);
        gvContainer.setHorizontalSpacing(padding);
        gvContainer.setVerticalSpacing(padding * 2);
        //设置GridView的宽高
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(gvWidth, gvHeight);
        gvContainer.setLayoutParams(params);
        // 给GridView设置表情图片
        EmotionGridViewAdapter adapter = new EmotionGridViewAdapter(getActivity(), emotionNames, itemWidth);
        adapter.setDefaultEmotionListener(defaultEmotionListener);
        gvContainer.setAdapter(adapter);

        return gvContainer;
    }
}
