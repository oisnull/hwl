package com.hwl.beta.ui.emoji;

import android.app.Activity;

public class ActivityEmojiSetting extends BaseActivity {

    Activity activity;
    EmojiActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.emoji_activity_setting);
        initView();
    }

    private void initView() {
        binding.tbTitle.setTitle("表情设置")
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

       
    }
}
