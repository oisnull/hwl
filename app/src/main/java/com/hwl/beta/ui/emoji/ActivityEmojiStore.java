package com.hwl.beta.ui.emoji;

import android.app.Activity;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hwl.beta.AppConfig;
import com.hwl.beta.R;
import com.hwl.beta.databinding.EmojiActivityStoreBinding;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.UITransfer;

public class ActivityEmojiStore extends BaseActivity {

    Activity activity;
    EmojiActivityStoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.emoji_activity_store);

        initView();
    }

    private void initView() {
        binding.tbTitle.setTitle("表情商店")
                .setImageRightResource(R.drawable.ic_setting)
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UITransfer.toEmojiSettingActivity(activity);
                    }
                })
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        binding.wvEmojiList.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        binding.wvEmojiList.loadUrl(AppConfig.EMOTION_ENTRANCE_URL);
    }
}
