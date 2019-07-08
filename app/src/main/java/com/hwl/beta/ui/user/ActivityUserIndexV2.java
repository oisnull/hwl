package com.hwl.beta.ui.user;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hwl.beta.R;
import com.hwl.beta.databinding.UserActivityIndexV2Binding;
import com.hwl.beta.ui.common.BaseActivity;

/**
 * Created by Administrator on 2018/1/9.
 */
public class ActivityUserIndexV2 extends BaseActivity {

    Activity activity;
    UserActivityIndexV2Binding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.user_activity_index_v2);

        initView();
    }

    private void initView() {
        binding.tbTitle.setTitle("用户详情")
                .setImageRightResource(R.drawable.v_more)
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                })
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
    }
}
