//package com.hwl.beta.ui.user;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.databinding.DataBindingUtil;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.FragmentActivity;
//import android.view.View;
//import android.widget.Toast;
//
//import com.hwl.beta.R;
//import com.hwl.beta.databinding.ActivityUserEditBinding;
//import com.hwl.beta.net.NetConstant;
//import com.hwl.beta.net.ResponseBase;
//import com.hwl.beta.net.resx.ResxType;
//import com.hwl.beta.net.resx.UploadService;
//import com.hwl.beta.net.resx.body.UpResxResponse;
//import com.hwl.beta.net.user.NetUserInfo;
//import com.hwl.beta.net.user.UserService;
//import com.hwl.beta.net.user.body.SetUserInfoResponse;
//import com.hwl.beta.sp.UserSP;
//import com.hwl.beta.ui.busbean.EventBusConstant;
//import com.hwl.beta.ui.common.BaseActivity;
//import com.hwl.beta.ui.common.UITransfer;
//import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
//import com.hwl.beta.ui.convert.SexAction;
//import com.hwl.beta.ui.dialog.LoadingDialog;
//import com.hwl.beta.ui.imgselect.bean.ImageSelectType;
//import com.hwl.beta.ui.user.action.IUserEditListener;
//import com.hwl.beta.ui.user.bean.ImageViewBean;
//import com.hwl.beta.ui.user.bean.UserEditBean;
//import com.hwl.beta.ui.user.bean.UserEditItemBean;
//import com.hwl.beta.utils.StringUtils;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.io.File;
//
//import io.reactivex.Observable;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.functions.Function;
//
///**
// * Created by Administrator on 2018/1/9.
// */
//
//public class ActivityUserEdit extends BaseActivity {
//
//    ActivityUserEditBinding binding;
//    UserEditBean user;
//    Activity activity;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        activity = this;
//
//        user = new UserEditBean();
//        setUserEditBean();
//
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_edit);
//        binding.setUser(user);
//        binding.setAction(new UserEditListener());
//        binding.setImage(new ImageViewBean(user.getHeadImage()));
//
//        binding.tbTitle
//                .setTitle("我的信息")
//                .setImageRightHide()
//                .setImageLeftClick(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        onBackPressed();
//                    }
//                });
//
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(Integer ebType) {
//        if (ebType == EventBusConstant.EB_TYPE_USER_UPDATE) {
//            setUserEditBean();
//        }
//    }
//
//    private void setUserEditBean() {
//        NetUserInfo netUser = UserSP.getUserInfo();
//        user.setUserId(netUser.getId());
//        user.setName(netUser.getName());
//        user.setHeadImage(netUser.getHeadImage());
//        user.setSymbol(netUser.getSymbol());
//        user.setSex(SexAction.getSexName(netUser.getUserSex()));
//        user.setLifeNotes(netUser.getLifeNotes());
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
////            byte[] bitmap = data.getByteArrayExtra("bitmap");
//            String localPath = data.getStringExtra("localpath");
//            if (StringUtils.isBlank(localPath)) {
//                Toast.makeText(activity, "上传数据不能为空", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            LoadingDialog.show(activity, "正在上传...");
//            UploadService.upImage(new File(localPath), ResxType.USERHEADIMAGE)
//                    .flatMap(new Function<ResponseBase<UpResxResponse>, Observable<ResponseBase<SetUserInfoResponse>>>() {
//                        @Override
//                        public Observable<ResponseBase<SetUserInfoResponse>> apply(ResponseBase<UpResxResponse> response) throws Exception {
//                            if (response != null && response.getResponseBody() != null && response.getResponseBody().isSuccess()) {
//                                return UserService.setUserHeadImage(response.getResponseBody().getOriginalUrl());
//                            } else
//                                throw new Exception("头像上传失败");
//                        }
//                    })
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new NetDefaultObserver<SetUserInfoResponse>() {
//                        @Override
//                        protected void onSuccess(SetUserInfoResponse response) {
//                            LoadingDialog.hide();
//                            if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
//                                UserSP.setUserHeadImage(response.getHeadImage());
//                                EventBus.getDefault().post(EventBusConstant.EB_TYPE_USER_HEAD_UPDATE);
//                                ImageViewBean.loadImage(binding.ivHeaderLook, response.getHeadImage());
//                                Toast.makeText(activity, "头像上传成功", Toast.LENGTH_SHORT).show();
//                            } else {
//                                onError("头像上传失败");
//                            }
//                        }
//
//                        @Override
//                        protected void onError(String resultMessage) {
//                            super.onError(resultMessage);
//                            LoadingDialog.hide();
//                        }
//
//                        @Override
//                        protected void onRelogin() {
//                            UITransfer.toReloginDialog((FragmentActivity) activity);
//                        }
//                    });
//        }
//    }
//
//    public class UserEditListener implements IUserEditListener {
//
//        @Override
//        public void onHeadImageClick() {
//            UITransfer.toImageSelectActivity(activity, ImageSelectType.USER_HEAD, 1);
//        }
//
//        @Override
//        public void onSymbolClick() {
//            UITransfer.toUserEditItemActivity(activity, UserEditItemBean.ACTIONTYPE_SYMBOL, user.getSymbol());
//        }
//
//        @Override
//        public void onNameClick() {
//            UITransfer.toUserEditItemActivity(activity, UserEditItemBean.ACTIONTYPE_NAME, user.getName());
//        }
//
//        @Override
//        public void onSexClick() {
//            UITransfer.toUserEditItemActivity(activity, UserEditItemBean.ACTIONTYPE_SEX, user.getSex());
//        }
//
//        @Override
//        public void onLifeNotesClick() {
//            UITransfer.toUserEditItemActivity(activity, UserEditItemBean.ACTIONTYPE_LIFENOTES, user.getLifeNotes());
//        }
//    }
//}
