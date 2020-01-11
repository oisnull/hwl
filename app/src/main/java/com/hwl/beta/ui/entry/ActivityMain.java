package com.hwl.beta.ui.entry;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.hwl.beta.location.IHWLLoactionListener;
import com.hwl.beta.location.LocationModel;
import com.hwl.beta.net.NetExceptionCode;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.ui.NetworkBroadcastReceiver;
import com.hwl.beta.R;
import com.hwl.beta.databinding.EntryActivityMainBinding;
import com.hwl.beta.location.BaiduLocationV2;
import com.hwl.beta.sp.UserPosSP;
import com.hwl.beta.ui.TabFragmentPagerAdapter;
import com.hwl.beta.ui.common.PermissionsOperator;
import com.hwl.beta.ui.dialog.DialogUtils;
import com.hwl.beta.ui.ebus.EventBusConstant;
import com.hwl.beta.ui.ebus.EventMessageModel;
import com.hwl.beta.ui.chat.FragmentRecord;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.entry.bean.MainBean;
import com.hwl.beta.ui.near.FragmentNear;
import com.hwl.beta.ui.user.FragmentCenter;
import com.hwl.beta.ui.user.FragmentFriends;
import com.hwl.beta.ui.entry.action.IMainListener;
import com.hwl.beta.ui.entry.logic.MainLogic;
import com.hwl.beta.ui.entry.standard.MainStandard;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class ActivityMain extends BaseActivity {
    FragmentActivity activity;
    EntryActivityMainBinding binding;
    MainStandard mainStandard;
    MainBean mainBean;
    MainListener mainListener;
    NetworkBroadcastReceiver networkBroadcastReceiver;
    BaiduLocationV2 location;
    long exitTime = 0;
    boolean permissionRuning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        activity = this;
        mainListener = new MainListener();
        mainStandard = new MainLogic();
        mainBean = mainStandard.getMainBean();
        binding = DataBindingUtil.setContentView(this, R.layout.entry_activity_main);
        binding.setMainBean(mainBean);
        binding.setAction(mainListener);

        initView();
    }

    private void initView() {
        setBottomNavVisibility(false);
        mainListener.initVPContainer();

        binding.tbTitle.setTitle("位置获取中...")
                .setImageLeftResource(R.drawable.ic_location, 0)
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLocationStatus();
                    }
                })
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopMenu(v);
                    }
                });

        location = new BaiduLocationV2(new HWLLocationListener());
        locationStart();

        networkBroadcastReceiver = new NetworkBroadcastReceiver();
        registerReceiver(networkBroadcastReceiver, new IntentFilter(NetworkBroadcastReceiver
                .ACTION_TAG));

        SQLiteStudioService.instance().start(activity);
    }

    private void locationStart() {
        if (permissionRuning)
            return;
        permissionRuning = true;

        UserPosSP.clearPosInfo();
        DialogUtils.closeLocationDialog();
        if (PermissionsOperator.requestLocation(activity)) {
            binding.tbTitle.setTitle("位置获取中...");
            DialogUtils.showLocationLoadingDialog(activity);
            location.start();
        }
    }

    private void locationStop() {
        DialogUtils.closeLocationLoadingDialog();
        location.stop();
        permissionRuning = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean grant =
                grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        switch (requestCode) {
            case PermissionsOperator.REQUEST_PERMISSION_LOCATION:
                permissionRuning = false;
                if (grant) {
                    locationStart();
                } else {
                    showLocationDialog("定位失败", "没有获取到定位的权限.");
                }
                break;
        }

    }

    @Override
    protected boolean isUseSwipeBackAnimation() {
        return false;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEventMessage(EventMessageModel messageModel) {
        switch (messageModel.getMessageType()) {
            case EventBusConstant.EB_TYPE_TOKEN_INVALID_RELOGIN:
                UITransfer.toReloginDialog(this);
                break;
            case EventBusConstant.EB_TYPE_FRIEND_REQUEST_UPDATE:
            case EventBusConstant.EB_TYPE_CIRCLE_MESSAGE_UPDATE:
                mainBean.setFriendMessageCount(MessageCountSP
                        .getFriendRequestCount() + MessageCountSP
                        .getCircleMessageCount());
                break;
            case EventBusConstant.EB_TYPE_NEAR_CIRCLE_MESSAGE_UPDATE:
                mainBean.setNearMessageCount(MessageCountSP
                        .getNearCircleMessageCount());
                break;
            case EventBusConstant.EB_TYPE_NETWORK_CONNECT_UPDATE:
                locationStart();
                break;
            case EventBusConstant.EB_TYPE_NETWORK_BREAK_UPDATE:
                locationStop();
                break;
        }
    }

    public void showLocationDialog(String title, String content) {
        DialogUtils.closeLocationLoadingDialog();
        DialogUtils.showLocationDialog(activity, title, content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location.isEnd()) {
                    locationStart();
                    if (location.getCurrentStatus() == BaiduLocationV2.POSITIONING)
                        binding.tbTitle.setTitle("位置重新获取中...");
                }
                DialogUtils.closeLocationDialog();
            }
        });
    }

    public void showLocationStatus() {
        switch (location.getCurrentStatus()) {
            case BaiduLocationV2.NOT_START:
            case BaiduLocationV2.POSITIONING:
                showLocationDialog("当前位置", "正在获取当前位置信息...");
                break;
            case BaiduLocationV2.COMPLETE_FAILURE:
                showLocationDialog("定位失败", location.getErrorMessage());
                break;
            case BaiduLocationV2.COMPLETE_SUCCESS:
                showLocationDialog("当前位置", UserPosSP.getPosDesc());
                break;
        }
    }

    public void setBottomNavVisibility(boolean isShow) {
        if (isShow && binding.llNavBottom.isShown()) {
            return;
        }
        binding.llNavBottom.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationStop();
        unregisterReceiver(networkBroadcastReceiver);
        SQLiteStudioService.instance().stop();
    }

    private void showPopMenu(View v) {
        PopupMenu popup = new PopupMenu(activity, v);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.pop_add_user:
                        UITransfer.toUserSearchActivity(activity);
                        break;
                    case R.id.pop_near_group:
                        if (UserPosSP.isExistPosInfo()) {
                            UITransfer.toChatGroupActivity(activity, UserPosSP.getGroupGuid());
                        } else {
                            showLocationStatus();
                        }
                        break;
                    case R.id.pop_near_message:
                        UITransfer.toNearMessagesActivity(activity);
                        break;
                    case R.id.pop_near_publish:
                        UITransfer.toNearPublishActivity(activity);
                        break;
                    case R.id.pop_share_app:
                        UITransfer.toQRCodeActivity(activity);
//                        new AlertDialog.Builder(activity)
//                                .setMessage("如果别人就在你的旁边，你可以通过二维码来分享给TA...")
//                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        UITransfer.toQRCodeActivity(activity);
//                                        dialog.dismiss();
//                                    }
//                                })
//                                .setNegativeButton("用其它方式", new DialogInterface.OnClickListener
//                                () {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        ShareTransfer.shareApp();
//                                    }
//                                })
//                                .show();
                        break;
                    case R.id.pop_open_test:
                        UITransfer.toTestActivity(activity);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(activity, "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class MainListener implements IMainListener {

        public void initVPContainer() {
            List<Fragment> fragments = new ArrayList<>();
            final FragmentNear fragmentNear = new FragmentNear();
            fragments.add(new FragmentRecord());
            fragments.add(fragmentNear);
            fragments.add(new FragmentFriends());
            fragments.add(new FragmentCenter());

            TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter
                    (getSupportFragmentManager(), fragments);
            binding.vpContainer.setAdapter(adapter);
            binding.vpContainer.setCurrentItem(0);
            switchTab(0);

            binding.vpContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int
                        positionOffsetPixels) {
                    //页面在滑动后调用
                }

                @Override
                public void onPageSelected(int position) {
                    //页面跳转完后调用
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    //页面状态改变时调用 1:表示正在滑动 ，2：表示滑动完毕，0：表示什么都没做
                    if (state == 2) {
                        int index = binding.vpContainer.getCurrentItem();
                        fragmentNear.setEmotionStatus(false);
                        switchTab(index);
                    }
                }
            });
        }

        @Override
        public void onTabMessageClick() {
            binding.vpContainer.setCurrentItem(0);
            switchTab(0);
        }

        @Override
        public void onTabNearClick() {
            binding.vpContainer.setCurrentItem(1);
            switchTab(1);
        }

        @Override
        public void onTabFriendClick() {
            binding.vpContainer.setCurrentItem(2);
            switchTab(2);

        }

        @Override
        public void onTabMeClick() {
            binding.vpContainer.setCurrentItem(3);
            switchTab(3);
        }

        int currentViewIndex = -1;

        private void switchTab(int index) {
            if (currentViewIndex == index) return;
            currentViewIndex = index;
            switch (index) {
                case 0:
                    binding.tvMessage.setTextColor(getResources().getColor(R.color.main));
                    binding.tvFriend.setTextColor(getResources().getColor(R.color.black));
                    binding.tvNear.setTextColor(getResources().getColor(R.color.black));
                    binding.tvMe.setTextColor(getResources().getColor(R.color.black));
                    break;
                case 1:
                    binding.tvMessage.setTextColor(getResources().getColor(R.color.black));
                    binding.tvFriend.setTextColor(getResources().getColor(R.color.black));
                    binding.tvNear.setTextColor(getResources().getColor(R.color.main));
                    binding.tvMe.setTextColor(getResources().getColor(R.color.black));
                    break;
                case 2:
                    binding.tvMessage.setTextColor(getResources().getColor(R.color.black));
                    binding.tvFriend.setTextColor(getResources().getColor(R.color.main));
                    binding.tvNear.setTextColor(getResources().getColor(R.color.black));
                    binding.tvMe.setTextColor(getResources().getColor(R.color.black));
                    break;
                case 3:
                    binding.tvMessage.setTextColor(getResources().getColor(R.color.black));
                    binding.tvFriend.setTextColor(getResources().getColor(R.color.black));
                    binding.tvNear.setTextColor(getResources().getColor(R.color.black));
                    binding.tvMe.setTextColor(getResources().getColor(R.color.main));
                    break;
            }
        }
    }

    private class HWLLocationListener implements IHWLLoactionListener {

        private double getMoveDistance(LocationModel model) {
//            LatLng curr = new LatLng(model.latitude, model.longitude);
//            LatLng old = new LatLng(UserPosSP.getLatitude(), UserPosSP.getLontitude());
//            return DistanceUtil.getDistance(old, curr);
            return 0;
        }

        @Override
        public void onSuccess(LocationModel model) {
            binding.tbTitle.setTitle(UserPosSP.getNearDesc());

//            if (UserPosSP.getLongitude() == model.longitude &&
//                    UserPosSP.getLatitude() == model.latitude) {
//                return;
//            }

//            if (!NetworkUtils.isConnected()) {
//                locationStop();
//                return;
//            }

//            Log.d("HWLLocationListener", "当前移动的距离为：" + getMoveDistance(model));

            mainStandard.setLocation(model)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String desc) {
                            binding.tbTitle.setTitle(desc);
                            locationStop();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            locationStop();
                            if (NetExceptionCode.isTokenInvalid(throwable))
                                UITransfer.toReloginDialog(activity);

                            Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }

        @Override
        public void onFailure(String message) {
            binding.tbTitle.setTitle("未知");
            showLocationDialog("定位失败", message);
        }

    }
}
