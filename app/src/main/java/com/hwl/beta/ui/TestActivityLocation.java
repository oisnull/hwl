package com.hwl.beta.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hwl.beta.R;
import com.hwl.beta.location.BaiduLocationV2;
import com.hwl.beta.location.IHWLLoactionListener;
import com.hwl.beta.location.LocationModel;
import com.hwl.beta.sp.UserPosSP;
import com.hwl.beta.ui.common.BaseActivity;

public class TestActivityLocation extends BaseActivity {

    Activity mActivity;
    BaiduLocationV2 locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_location);
        mActivity = this;

        initView();
    }

    private void initView() {
        final TextView tvStatus = findViewById(R.id.tv_status);
        tvStatus.setText("正在加载定位数据...");

        locationService = new BaiduLocationV2(new IHWLLoactionListener() {
            @Override
            public void onSuccess(LocationModel result) {
                //判断本地存储的位置是否与当前定位的位置是否一样，如果一样则不做任何操作
                if (UserPosSP.getLongitude() == result.longitude && UserPosSP.getLatitude() ==
                        result.latitude) {
                    tvStatus.setText("与本地存储的位置数据一样：" + UserPosSP.getNearDesc());
                    return;
                }

                tvStatus.setText("当前定位的数据为：" + new Gson().toJson(result));
            }

            @Override
            public void onFailure(String message) {
                tvStatus.setText(message);
            }
        });
        locationService.start();
    }
}
