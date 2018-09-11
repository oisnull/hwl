package com.hwl.beta.ui.user;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.FragmentCenterBinding;
import com.hwl.beta.net.general.GeneralService;
import com.hwl.beta.net.general.body.CheckVersionResponse;
import com.hwl.beta.net.user.NetUserInfo;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.user.action.ICenterListener;
import com.hwl.beta.ui.user.bean.CenterBean;
import com.hwl.beta.ui.user.bean.ImageViewBean;
import com.hwl.beta.utils.AppUtils;
import com.hwl.beta.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2017/12/27.
 */

public class FragmentCenter extends Fragment {
    FragmentCenterBinding binding;
    Activity activity;
    CenterBean centerBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();

        centerBean = new CenterBean();
        setCenterBean();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_center, container, false);
        binding.setUser(centerBean);
        binding.setAction(new CenterListener());
        binding.setImage(new ImageViewBean(centerBean.getHeadImage()));

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setView();
        return binding.getRoot();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Integer ebType) {
//        if (ebType == EventBusConstant.EB_TYPE_USER_UPDATE) {
//            setCenterBean();
//            setView();
//        } else if (ebType == EventBusConstant.EB_TYPE_USER_HEAD_UPDATE) {
//            ImageViewBean.loadImage(binding.ivHeader, UserSP.getUserHeadImage());
//        }
    }

    private void setCenterBean() {
        NetUserInfo netUser = UserSP.getUserInfo();
        centerBean.setName(netUser.getName() + " - " + netUser.getId());
        centerBean.setHeadImage(netUser.getHeadImage());
        centerBean.setSymbol(netUser.getSymbol());
    }

    private void setView(){
        if (StringUtils.isBlank(centerBean.getSymbol())) {
            binding.llSymbolContainer.setVisibility(View.GONE);
        } else {
            binding.llSymbolContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
//            //Log.d("当前刷新的图片：", UserSP.getUserHeadImage());
//            Glide.with(this).load(UserSP.getUserHeadImage())
//                    .placeholder(R.drawable.empty_photo)
//                    .error(R.drawable.empty_photo)
//                    .into(ivHeader);
//            tvName.setText(UserSP.getUserName());
//            tvAccount.setText(UserSP.getUserSymbol());
        }
    }

    public class CenterListener implements ICenterListener {

        @Override
        public void onInfoClick() {
//            UITransfer.toUserEditActivity(activity);
        }

        @Override
        public void onHeadImageClick() {
//            UITransfer.toImageBrowseActivity(activity, UserSP.getUserHeadImage());
        }

        @Override
        public void onCircleClick() {
//            UITransfer.toCircleUserIndexActivity(activity, UserSP.getUserId(), UserSP.getUserName(), UserSP.getUserHeadImage(), UserSP.getUserCirclebackimage(), UserSP.getLifeNotes());
        }

        @Override
        public void onSettingClick() {
//            UITransfer.toUserSettingActivity(activity);
        }

        @Override
        public void onMessageClick() {
            Toast.makeText(activity, AppUtils.getAppVersionName(), Toast.LENGTH_LONG).show();
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
//                                        .setMessage("检测到最新版本 " + response.getNewVersion() + "，是否更新?")
//                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                UITransfer.toBrowser(activity, response.getDownLoadUrl());
//                                                dialog.dismiss();
//                                            }
//                                        })
//                                        .setNegativeButton("取消", null)
//                                        .show();
//                            } else {
//                                Toast.makeText(activity, "当前已经是最新的版本,不需要更新", Toast.LENGTH_SHORT).show();
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
