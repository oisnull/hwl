package com.hwl.beta.ui.entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityMainBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.SetUserPosRequest;
import com.hwl.beta.net.user.body.SetUserPosResponse;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.sp.UserPosSP;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.TabFragmentPagerAdapter;
import com.hwl.beta.ui.common.ShareTransfer;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.entry.action.IMainListener;
import com.hwl.beta.ui.entry.bean.MainBean;
import com.hwl.beta.ui.user.FragmentCenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class ActivityMain extends FragmentActivity {
    Activity activity;
    ActivityMainBinding binding;
    MainBean mainBean;
    MainListener mainListener;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        activity = this;
        mainBean = new MainBean(MessageCountSP.getChatMessageCount(), MessageCountSP.getNearCircleMessageCount(), MessageCountSP.getFriendRequestCount(), MessageCountSP.getCircleMessageCount(), 0);
        mainListener = new MainListener();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setMainBean(mainBean);
        binding.setAction(mainListener);

        initView();
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
    }

    private void initView() {
        mainListener.initLocation();
        mainListener.initVPContainer();
        binding.tbTitle.setTitle("位置获取中...")
                .setImageLeftResource(R.drawable.ic_location, 0)
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainListener.showLoactionStatus();
                    }
                })
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopMenu(v);
                    }
                });

//        MessageReceive.start();
//        SQLiteStudioService.instance().start(activity);
//        activity.registerReceiver(networkBroadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
//        EventBus.getDefault().postSticky(EventBusConstant.EB_TYPE_NEAR_INFO_UPDATE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateMessageCount(Integer ebType) {
//        if (ebType == EventBusConstant.EB_TYPE_FRIEND_REQUEST_UPDATE) {
//            mainBean.setFriendMessageCount(MessageCountSP.getFriendRequestCount());
//        } else if (ebType == EventBusConstant.EB_TYPE_CHAT_MESSAGE_UPDATE) {
//            mainBean.setChatMessageCount(MessageCountSP.getChatMessageCount());
//        } else if (ebType == EventBusConstant.EB_TYPE_NEAR_CIRCLE_MESSAGE_UPDATE) {
//            mainBean.setNearMessageCount(MessageCountSP.getNearCircleMessageCount());
//        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void userLogoutHint(UserLogoutMessageBean messageBean) {
//        if (messageBean == null) return;
//        UITransfer.toReloginDialog(this, messageBean.getHintContent());
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        activity.unregisterReceiver(networkBroadcastReceiver);
//        EventBus.getDefault().unregister(this);
////        MessageReceive.stop();
//        SQLiteStudioService.instance().stop();
    }

    private void showPopMenu(View v) {
        PopupMenu popup = new PopupMenu(activity, v);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.pop_add_user:
//                        UITransfer.toUserSearchActivity(activity);
//                        break;
//                    case R.id.pop_near_group:
//                        UITransfer.toChatGroupActivity(activity, UserPosSP.getGroupGuid());
//                        break;
//                    case R.id.pop_near_message:
//                        UITransfer.toNearMessagesActivity(activity);
//                        break;
//                    case R.id.pop_near_publish:
//                        UITransfer.toNearPublishActivity(activity);
//                        break;
//                    case R.id.pop_share_app:
//                        new AlertDialog.Builder(activity)
//                                .setMessage("如果别人就在你的旁边，你可以通过二维码来分享给TA...")
//                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        UITransfer.toQRCodeActivity(activity);
//                                        dialog.dismiss();
//                                    }
//                                })
//                                .setNegativeButton("用其它方式", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        ShareTransfer.shareApp();
//                                    }
//                                })
//                                .show();
//
//                        break;
//                    case R.id.pop_open_test:
//                        UITransfer.toTestActivity(activity);
//                        break;
//                }
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
//        LocationService locationService;
//        LocationDialogFragment locationTip;

        @Override
        public void initVPContainer() {
            List<Fragment> fragments = new ArrayList<>();
//            fragments.add(new FragmentRecord());
//            fragments.add(new FragmentNear());
//            fragments.add(new FragmentUser());
            fragments.add(new FragmentCenter());

            TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), fragments);
            binding.vpContainer.setAdapter(adapter);
            binding.vpContainer.setCurrentItem(0);
            switchTab(0);

            binding.vpContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
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
                        switchTab(index);
                    }
                }
            });
        }

        @Override
        public void initLocation() {
//            locationService = new LocationService(new LocationService.OnLocationListener() {
//                @Override
//                public void onSuccess(LocationService.ResultModel result) {
//                    binding.tbTitle.setTitle(result.getNearDesc());
//                    //判断本地存储的位置是否与当前定位的位置是否一样，如果一样则不做任何操作
//                    if (UserPosSP.getLontitude() == result.lontitude && UserPosSP.getLatitude() == result.latitude) {
//                        return;
//                    }
//
//                    //调用api将当前位置存储到服务器
//                    final SetUserPosRequest request = new SetUserPosRequest();
//                    request.setUserId(UserSP.getUserId());
//                    request.setLastGroupGuid(UserPosSP.getGroupGuid());
//                    request.setLatitude(result.latitude + "");
//                    request.setLongitude(result.lontitude + "");
//                    request.setCountry(result.country);
//                    request.setProvince(result.province);
//                    request.setCity(result.city);
//                    request.setDistrict(result.district);
//                    request.setStreet(result.street);
//                    request.setDetails(result.addr);
//                    UserService.setUserPos(request)
//                            .subscribe(new NetDefaultObserver<SetUserPosResponse>() {
//                                @Override
//                                protected void onSuccess(SetUserPosResponse res) {
//                                    if (res.getStatus() == NetConstant.RESULT_SUCCESS) {
//                                        binding.tbTitle.setTitle(UserPosSP.getNearDesc());
//
//                                        //往本地存储一份定位数据
//                                        UserPosSP.setUserPos(res.getUserPosId(), res.getUserGroupGuid(), Float.parseFloat(request.getLatitude()), Float.parseFloat(request.getLongitude()), request.getCountry(), request.getProvince(), request.getCity(), request.getDistrict(), request.getStreet(), request.getDetails());
//                                        if (res.getGroupUserInfos() != null && res.getGroupUserInfos().size() > 0) {
//                                            //保存组和组用户数据到本地
//                                            GroupInfo groupInfo = DBGroupAction.convertToNearGroupInfo(res.getUserGroupGuid(), res.getGroupUserInfos().size(), DBGroupAction.convertToGroupUserImages(res.getGroupUserInfos()));
//                                            DaoUtils.getGroupInfoManagerInstance().add(groupInfo);
//                                            DaoUtils.getGroupUserInfoManagerInstance().addListAsync(DBGroupAction.convertToGroupUserInfos(res.getGroupUserInfos()));
//                                            EventBus.getDefault().post(new EventActionGroup(EventBusConstant.EB_TYPE_GROUP_IMAGE_UPDATE, groupInfo));
//                                        }
//                                    }
//                                }
//                            });
//                }
//
//                @Override
//                public void onFaild(LocationService.ResultInfo info) {
//                    binding.tbTitle.setTitle(UserPosSP.getNearDesc());
//                    showLocationDialog();
//                    locationTip.setTitleShow("定位失败");
//                    locationTip.setContentShow(info.status + " : " + info.message);
//                }
//            });
//            locationService.start();
        }

        public void showLocationDialog() {
//            if (locationTip == null) {
//                locationTip = new LocationDialogFragment();
//            }
//            locationTip.setResetLocationClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    binding.tbTitle.setTitle("位置重新获取中...");
//                    locationService.start();
//                    locationTip.dismiss();
//                }
//            });
//            locationTip.show(getSupportFragmentManager(), "LocationDialogFragment");
        }

        public void showLoactionStatus() {
//            int status = locationService.getCurrentLocationStatus();
//            switch (status) {
//                case LocationService.NOT_START:
//                case LocationService.POSITIONING:
//                    showLocationDialog();
//                    locationTip.setTitleShow("当前位置");
//                    locationTip.setContentShow("正在获取当前位置信息...");
//                    break;
//                case LocationService.COMPLETE_FAILD:
//                    showLocationDialog();
//                    locationTip.setTitleShow("定位失败");
//                    locationTip.setContentShow("获取当前位置失败,请检测是否已经连网！");
//                    break;
//                case LocationService.COMPLETE_SUCCESS:
//                    showLocationDialog();
//                    locationTip.setTitleShow("当前位置");
//                    locationTip.setContentShow(UserPosSP.getPosDesc());
//                    break;
//            }
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
                    //binding.nearFragment.loadData();
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

//    private BroadcastReceiver networkBroadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//            if (networkInfo != null && networkInfo.isAvailable()) {
////                Toast.makeText(context, "当前网络可用", Toast.LENGTH_SHORT).show();
//                EventBus.getDefault().post(EventBusConstant.EB_TYPE_NETWORK_CONNECT_UPDATE);
//            } else {
////                Toast.makeText(context, "当前网络不可用", Toast.LENGTH_SHORT).show();
//                EventBus.getDefault().post(EventBusConstant.EB_TYPE_NETWORK_BREAK_UPDATE);
//            }
//        }
//    };
}
