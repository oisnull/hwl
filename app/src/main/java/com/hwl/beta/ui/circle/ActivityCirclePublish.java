package com.hwl.beta.ui.circle;

import android.app.Activity;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.view.View;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.CircleActivityPulishBinding;
import com.hwl.beta.ui.circle.logic.CirclePublishLogic;
import com.hwl.beta.ui.circle.standard.CirclePublishStandard;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.dialog.LoadingDialog;
import com.hwl.beta.ui.imgselect.bean.ImageBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class ActivityCirclePublish extends BaseActivity {
    CircleActivityPulishBinding binding;
    FragmentActivity activity;
    CirclePublishStandard publishStandard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        publishStandard = new CirclePublishLogic();

        binding = DataBindingUtil.setContentView(activity, R.layout.circle_activity_pulish);
        initView();
    }

    public void initView() {
        binding.tbTitle.setTitle("发布圈子信息")
                .setTitleRightText("发布")
                .setTitleRightBackground(R.drawable.bg_top)
                .setImageRightHide()
                .setTitleRightShow()
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                })
                .setTitleRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        publishInfo();
                    }
                });

        binding.edpEmotion.setEditText(binding.etEmotionText);
        binding.edpEmotion.setPanelVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            binding.iavImages.setImagePaths(data.getExtras().<ImageBean>getParcelableArrayList(
                    "selectimages"));
        }
    }

    private void publishInfo() {
        LoadingDialog.show(activity, "正在发布,请稍后...");
        publishStandard.publishInfo(binding.etEmotionText.getText() + "",
                binding.iavImages.getImagePaths())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) {
                        Toast.makeText(getApplicationContext(), "发布成功", Toast.LENGTH_SHORT).show();
                        LoadingDialog.hide();
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        LoadingDialog.hide();
                    }
                });
    }

}
