package com.hwl.beta.ui.emoji;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hwl.beta.R;
import com.hwl.beta.databinding.EmojiActivityStoreBinding;
import com.hwl.beta.ui.common.BaseActivity;

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
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
        binding.wvEmojiList.loadUrl("http://www.baidu.com");
    }
}
