package com.hwl.beta.ui.user;

import android.app.Activity;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hwl.beta.BuildConfig;
import com.hwl.beta.R;
import com.hwl.beta.databinding.UserFragmentCenterBinding;
import com.hwl.beta.net.general.GeneralService;
import com.hwl.beta.net.general.NetAppVersionInfo;
import com.hwl.beta.net.general.body.CheckVersionResponse;
import com.hwl.beta.net.user.NetUserInfo;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.rxext.RXDefaultObserver;
import com.hwl.beta.ui.dialog.LoadingDialog;
import com.hwl.beta.ui.ebus.EventBusConstant;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.beta.ui.ebus.EventMessageModel;
import com.hwl.beta.ui.common.BaseFragment;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.user.action.ICenterListener;
import com.hwl.beta.ui.user.bean.CenterBean;
import com.hwl.beta.ui.user.bean.ImageViewBean;
import com.hwl.beta.utils.AppUtils;
import com.hwl.beta.utils.StringUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/27.
 */

public class FragmentCenter extends BaseFragment {
    UserFragmentCenterBinding binding;
    Activity activity;
    CenterBean centerBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        activity = getActivity();

        centerBean = new CenterBean();
        setCenterBean();
        binding = DataBindingUtil.inflate(inflater, R.layout.user_fragment_center, container,
                false);
        binding.setUser(centerBean);
        binding.setAction(new CenterListener());
        binding.setImage(new ImageViewBean(centerBean.getHeadImage()));

        setView();
        return binding.getRoot();
    }

    private void setCenterBean() {
        NetUserInfo netUser = UserSP.getUserInfo();
        if (BuildConfig.DEBUG) {
            centerBean.setName(netUser.getName() + " - " + netUser.getId());
        } else {
            centerBean.setName(netUser.getName());
        }
        centerBean.setHeadImage(netUser.getHeadImage());
        centerBean.setSymbol(netUser.getSymbol());
    }

    private void setView() {
        if (StringUtils.isBlank(centerBean.getSymbol())) {
            binding.llSymbolContainer.setVisibility(View.GONE);
        } else {
            binding.llSymbolContainer.setVisibility(View.VISIBLE);
        }

        if (MessageCountSP.getAppVersionCount() > 0)
            binding.ivAppVersionNum.setVisibility(View.VISIBLE);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEventMessage(EventMessageModel messageModel) {
        switch (messageModel.getMessageType()) {
            case EventBusConstant.EB_TYPE_USER_HEAD_UPDATE:
                Glide.with(this).load(UserSP.getUserHeadImage())
                        .placeholder(R.drawable.empty_photo)
                        .error(R.drawable.empty_photo)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(binding.ivHeader);
                break;
            case EventBusConstant.EB_TYPE_USER_SYMBOL_UPDATE:
                binding.tvSymbol.setText(UserSP.getUserSymbol());
                break;
            case EventBusConstant.EB_TYPE_USER_NAME_UPDATE:
                binding.tvName.setText(UserSP.getUserName());
                break;
            case EventBusConstant.EB_TYPE_APP_VERSION_UPDATE:
                if (MessageCountSP.getAppVersionCount() > 0)
                    binding.ivAppVersionNum.setVisibility(View.VISIBLE);
                else
                    binding.ivAppVersionNum.setVisibility(View.GONE);
                break;
        }
    }

    public class CenterListener implements ICenterListener {

        @Override
        public void onInfoClick() {
            UITransfer.toUserEditActivity(activity);
        }

        @Override
        public void onHeadImageClick() {
            UITransfer.toImageBrowseActivity(activity, UserSP.getUserHeadImage());
        }

        @Override
        public void onCircleClick() {
            UITransfer.toCircleUserIndexActivity(activity, UserSP.getUserId(),
                    UserSP.getUserName(), UserSP.getUserHeadImage());
        }

        @Override
        public void onSettingClick() {
//            UITransfer.toUserSettingActivity(activity);
        }

        @Override
        public void onCircleMessageClick() {
            UITransfer.toCircleMessagesActivity(activity);
        }

        private void VersionTip(boolean isUpgrade, NetAppVersionInfo versionInfo) {
            MessageCountSP.setAppVersionInfo(null);
            EventBusUtil.sendAppVersionUpdateEvent();

            if (isUpgrade) {
                if (versionInfo == null) {
                    Toast.makeText(activity, "没有找到新的版本", Toast.LENGTH_SHORT).show();
                    return;
                }
                UIData data = UIData.create()
                        .setTitle("有新版本 " + versionInfo.getAppVersion())
                        .setContent(versionInfo.getUpgradeLog())
                        .setDownloadUrl(versionInfo.getDownloadUrl());
                AllenVersionChecker
                        .getInstance()
                        .downloadOnly(data)
                        .executeMission(activity);
            } else {
                Toast.makeText(activity, "当前已经是最新的版本", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCheckUpdateClick() {
            if (MessageCountSP.getAppVersionCount() > 0) {
                NetAppVersionInfo versionInfo = MessageCountSP.getAppVersionInfo();
                boolean isUp = AppUtils.isUpgrade(versionInfo.getAppVersion(),
                        AppUtils.getAppVersionName());
                VersionTip(isUp, versionInfo);
            } else {
                LoadingDialog.show(activity, "版本检测中,请稍后...");
                GeneralService.checkVersion()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RXDefaultObserver<CheckVersionResponse>() {
                            @Override
                            protected void onSuccess(CheckVersionResponse response) {
                                LoadingDialog.hide();
                                VersionTip(response.isNewVersion(), response.getAppVersionInfo());
                            }

                            @Override
                            protected void onError(String message) {
                                super.onError(message);
                                LoadingDialog.hide();
                            }
                        });
            }
        }

        @Override
        public void onLogoutClick() {
            UITransfer.toLogout(activity);
            activity.finish();
        }
    }
}
