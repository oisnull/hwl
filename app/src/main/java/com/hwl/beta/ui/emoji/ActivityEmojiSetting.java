package com.hwl.beta.ui.emoji;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.hwl.beta.R;
import com.hwl.beta.databinding.EmojiActivitySettingBinding;
import com.hwl.beta.ui.common.BaseActivity;

public class ActivityEmojiSetting extends BaseActivity {

    Activity activity;
    EmojiActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = EmojiActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    private void initView() {
        binding.tbTitle.setTitle("表情设置")
                .setImageRightHide()
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });


    }
}
