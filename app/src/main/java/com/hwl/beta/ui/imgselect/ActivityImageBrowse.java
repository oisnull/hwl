package com.hwl.beta.ui.imgselect;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ImgselectActivityViewBinding;
import com.hwl.beta.swipeback.SwipeBackHelper;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.imgselect.adp.ImagePagerAdapter;

import java.util.List;

public class ActivityImageBrowse extends BaseActivity {
    public final static int MODE_VIEW = 0;
    public final static int MODE_ACTION = 1;

    ImgselectActivityViewBinding binding;
    Activity activity;
    int mode;
    int imagePosotion;
    List<String> imageUrls;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        mode = getIntent().getIntExtra("mode", 0);
        imagePosotion = getIntent().getIntExtra("position", 0);
        imageUrls = getIntent().getStringArrayListExtra("imageurls");
        if (imageUrls == null || imageUrls.size() <= 0) {
            Toast.makeText(activity, "图片加载失败", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        binding = ImgselectActivityViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    private void initView() {
        if (mode == MODE_VIEW) {
            binding.tvSend.setVisibility(View.GONE);
        } else {
            binding.tvSend.setVisibility(View.VISIBLE);
        }

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.pvpImages.setAdapter(new ImagePagerAdapter(activity, imageUrls));
        binding.pvpImages.setCurrentItem(imagePosotion);
        binding.pvpImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //页面在滑动后调用
            }

            @Override
            public void onPageSelected(int position) {
                //页面跳转完后调用
                imagePosotion = position;
                setCount();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //页面状态改变时调用 1:表示正在滑动 ，2：表示滑动完毕，0：表示什么都没做
                if (state == 2) {
                }
            }
        });

        setCount();
    }

    private void setCount() {
        if (imageUrls.size() <= 1) {
            binding.tvCount.setVisibility(View.GONE);
        } else {
            binding.tvCount.setVisibility(View.VISIBLE);
            binding.tvCount.setText((imagePosotion + 1) + "/" + imageUrls.size());
        }
        if(imagePosotion<=0){
            SwipeBackHelper.getCurrentPage(activity).setDisallowInterceptTouchEvent(false);
        }else{
            SwipeBackHelper.getCurrentPage(activity).setDisallowInterceptTouchEvent(true);
        }
    }
}
