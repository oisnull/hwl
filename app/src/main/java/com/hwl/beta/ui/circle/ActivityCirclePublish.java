package com.hwl.beta.ui.circle;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hwl.beta.R;
import com.hwl.beta.databinding.CircleActivityPulishBinding;
import com.hwl.beta.db.entity.CircleImage;
import com.hwl.beta.emotion.IDefaultEmotionListener;
import com.hwl.beta.net.ResponseBase;
import com.hwl.beta.net.circle.CircleService;
import com.hwl.beta.net.circle.body.AddCircleInfoResponse;
import com.hwl.beta.net.near.NetImageInfo;
import com.hwl.beta.net.resx.ResxType;
import com.hwl.beta.net.resx.UploadService;
import com.hwl.beta.net.resx.body.UpResxResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.dialog.LoadingDialog;
import com.hwl.beta.ui.imgcompress.CompressChatImage;
import com.hwl.beta.ui.imgselect.bean.ImageBean;
import com.hwl.beta.ui.imgselect.bean.ImageSelectType;
import com.hwl.beta.utils.ScreenUtils;
import com.hwl.beta.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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

		binding.ecpEmotion.setEditText(binding.etEmotionText)
                .setLocalSoftInputHeight(AppInstallStatus.getSoftInputHeight())
                .setContentContainerView(binding.llCirclePublishContainer);

        binding.llCirclePublishContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.ecpEmotion.setContentContainerHeight(binding.llCirclePublishContainer.getHeight() - binding.tbTitle.getHeight());
            }
        });
    }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            binding.iavImages.setImagePaths(data.getExtras().<ImageBean>getParcelableArrayList("selectimages"));
        }
    }

    private void publishInfo() {
        LoadingDialog.show(activity, "正在发布,请稍后...");
        publishStandard.publishInfo(binding.etEmotionText.getText() + "",binding.iavImages.getImagePaths())
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
