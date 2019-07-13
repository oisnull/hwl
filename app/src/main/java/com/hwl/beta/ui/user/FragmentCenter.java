package com.hwl.beta.ui.user;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hwl.beta.R;
import com.hwl.beta.databinding.UserFragmentCenterBinding;
import com.hwl.beta.net.user.NetUserInfo;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.ebus.EventBusConstant;
import com.hwl.beta.ui.ebus.EventMessageModel;
import com.hwl.beta.ui.common.BaseFragment;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.user.action.ICenterListener;
import com.hwl.beta.ui.user.bean.CenterBean;
import com.hwl.beta.ui.user.bean.ImageViewBean;
import com.hwl.beta.utils.AppUtils;
import com.hwl.beta.utils.StringUtils;

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
        centerBean.setName(netUser.getName() + " - " + netUser.getId());
        centerBean.setHeadImage(netUser.getHeadImage());
        centerBean.setSymbol(netUser.getSymbol());
    }

    private void setView() {
        if (StringUtils.isBlank(centerBean.getSymbol())) {
            binding.llSymbolContainer.setVisibility(View.GONE);
        } else {
            binding.llSymbolContainer.setVisibility(View.VISIBLE);
        }
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
                        .into(binding.ivHeader);
                break;
            case EventBusConstant.EB_TYPE_USER_SYMBOL_UPDATE:
                binding.tvSymbol.setText(UserSP.getUserSymbol());
                break;
            case EventBusConstant.EB_TYPE_USER_NAME_UPDATE:
                binding.tvName.setText(UserSP.getUserName());
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

        @Override
        public void onCheckUpdateClick() {
//            LoadingDialog.show(activity, "版本检测中,请稍后...");
//            GeneralService.checkVersion()
//                    .subscribe(new NetDefaultObserver<CheckVersionResponse>() {
//                        @Override
//                        protected void onSuccess(final CheckVersionResponse response) {
//                            LoadingDialog.hide();
//                            if (response.isNewVersion()) {
//                                new AlertDialog.Builder(activity)
//                                        .setMessage("检测到最新版本 " + response.getNewVersion() +
// "，是否更新?")
//                                        .setPositiveButton("确定", new DialogInterface
// .OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int
// which) {
//                                                UITransfer.toBrowser(activity, response
// .getDownLoadUrl());
//                                                dialog.dismiss();
//                                            }
//                                        })
//                                        .setNegativeButton("取消", null)
//                                        .show();
//                            } else {
//                                Toast.makeText(activity, "当前已经是最新的版本,不需要更新", Toast
// .LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        protected void onError(String resultMessage) {
//                            super.onError(resultMessage);
//                            LoadingDialog.hide();
//                        }
//                    });
        }

        @Override
        public void onLogoutClick() {
            UITransfer.toLogout(activity);
            activity.finish();
        }
    }
}
