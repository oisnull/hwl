package com.hwl.beta.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.view.View;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.UserActivityInfoEditBinding;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.resx.ResxService;
import com.hwl.beta.net.resx.ResxType;
import com.hwl.beta.net.resx.body.ImageUploadResponse;
import com.hwl.beta.net.user.NetUserInfo;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.SetUserInfoResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.common.rxext.RXDefaultObserver;
import com.hwl.beta.ui.convert.SexAction;
import com.hwl.beta.ui.dialog.LoadingDialog;
import com.hwl.beta.ui.ebus.EventBusConstant;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.beta.ui.ebus.EventMessageModel;
import com.hwl.beta.ui.imgselect.bean.ImageSelectType;
import com.hwl.beta.ui.user.action.IUserEditListener;
import com.hwl.beta.ui.user.bean.UserEditBean;
import com.hwl.beta.ui.user.bean.UserEditItemBean;
import com.hwl.beta.utils.StringUtils;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2018/1/9.
 */

public class ActivityUserEdit extends BaseActivity {

    UserActivityInfoEditBinding binding;
    UserEditBean user;
    FragmentActivity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        user = new UserEditBean();
        setUserEditBean();

        binding = UserActivityInfoEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        binding.setUser(user);
//        binding.setAction(new UserEditListener());
//        binding.setImage(new ImageViewBean(user.getHeadImage()));

        binding.tbTitle
                .setTitle("我的信息")
                .setImageRightHide()
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

    }

    private void setUserEditBean() {
        NetUserInfo netUser = UserSP.getUserInfo();
        user.setUserId(netUser.getId());
        user.setName(netUser.getName());
        user.setHeadImage(netUser.getHeadImage());
        user.setSymbol(netUser.getSymbol());
        user.setSex(SexAction.getSexName(netUser.getUserSex()));
        user.setLifeNotes(netUser.getLifeNotes());
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEventMessage(EventMessageModel messageModel) {
        switch (messageModel.getMessageType()) {
            case EventBusConstant.EB_TYPE_USER_SYMBOL_UPDATE:
                user.setSymbol(UserSP.getUserSymbol());
                break;
            case EventBusConstant.EB_TYPE_USER_NAME_UPDATE:
                user.setName(UserSP.getUserName());
                break;
            case EventBusConstant.EB_TYPE_USER_SEX_UPDATE:
                user.setSex(SexAction.getSexName(UserSP.getUserSex()));
                break;
            case EventBusConstant.EB_TYPE_USER_LIFENOTES_UPDATE:
                user.setLifeNotes(UserSP.getLifeNotes());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            String localPath = data.getStringExtra("localpath");
            if (StringUtils.isBlank(localPath)) {
                Toast.makeText(activity, "上传数据不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            LoadingDialog.show(activity, "正在上传...");
            ResxService.imageUpload(new File(localPath), ResxType.USERHEADIMAGE)
                    .flatMap(new Function<ImageUploadResponse, Observable<SetUserInfoResponse>>() {
                        @Override
                        public Observable<SetUserInfoResponse> apply(ImageUploadResponse response) throws Exception {
                            if (response.getResxImageResult().getSuccess()) {
                                return UserService.setUserHeadImage(response.getResxImageResult().getResxAccessUrl());
                            } else
                                throw new Exception(response.getResxImageResult().getMessage());
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RXDefaultObserver<SetUserInfoResponse>() {
                        @Override
                        protected void onSuccess(SetUserInfoResponse response) {
                            LoadingDialog.hide();
                            if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
                                UserSP.setUserHeadImage(response.getHeadImage());
//                                ImageViewBean.loadImage(binding.ivHeaderLook, response
//                                        .getHeadImage());
                                EventBusUtil.sendUserHeadImageEditEvent(response.getHeadImage());
                                Toast.makeText(activity, "头像上传成功", Toast.LENGTH_SHORT).show();
                            } else {
                                onError("头像上传失败");
                            }
                        }

                        @Override
                        protected void onError(String resultMessage) {
                            super.onError(resultMessage);
                            LoadingDialog.hide();
                        }

                        @Override
                        protected void onRelogin() {
                            UITransfer.toReloginDialog(activity);
                        }
                    });
        }
    }

    public class UserEditListener implements IUserEditListener {

        @Override
        public void onHeadImageClick() {
            UITransfer.toImageSelectActivity(activity, ImageSelectType.USER_HEAD, 1);
        }

        @Override
        public void onSymbolClick() {
            UITransfer.toUserEditItemActivity(activity, UserEditItemBean.ACTIONTYPE_SYMBOL, user
                    .getSymbol());
        }

        @Override
        public void onNameClick() {
            UITransfer.toUserEditItemActivity(activity, UserEditItemBean.ACTIONTYPE_NAME, user
                    .getName());
        }

        @Override
        public void onSexClick() {
            UITransfer.toUserEditItemActivity(activity, UserEditItemBean.ACTIONTYPE_SEX, user
                    .getSex());
        }

        @Override
        public void onLifeNotesClick() {
            UITransfer.toUserEditItemActivity(activity, UserEditItemBean.ACTIONTYPE_LIFENOTES,
                    user.getLifeNotes());
        }
    }
}
