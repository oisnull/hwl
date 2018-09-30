package com.hwl.beta.ui.entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.hwl.beta.ui.NetworkBroadcastReceiver;
import com.hwl.beta.R;
import com.hwl.beta.databinding.EntryActivityMainBinding;
import com.hwl.beta.location.BaiduLocation;
import com.hwl.beta.sp.UserPosSP;
import com.hwl.beta.ui.TabFragmentPagerAdapter;
import com.hwl.beta.ui.busbean.EventBusConstant;
import com.hwl.beta.ui.busbean.EventMessageModel;
import com.hwl.beta.ui.chat.FragmentRecord;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.common.ShareTransfer;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.dialog.LocationDialogFragment;
import com.hwl.beta.ui.near.FragmentNear;
import com.hwl.beta.ui.user.FragmentCenter;
import com.hwl.beta.ui.user.FragmentFriends;
import com.hwl.beta.ui.entry.action.IMainListener;
import com.hwl.beta.ui.entry.logic.MainHandle;
import com.hwl.beta.ui.entry.standard.MainStandard;

import java.util.ArrayList;
import java.util.List;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class ActivityMain extends BaseActivity {
    Activity activity;
    EntryActivityMainBinding binding;
    MainStandard mainStandard;
    MainListener mainListener;
    LocationDialogFragment locationTip;
    NetworkBroadcastReceiver networkBroadcastReceiver;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        activity = this;
        mainStandard = new MainHandle();
        mainListener = new MainListener();
        binding = DataBindingUtil.setContentView(this, R.layout.entry_activity_main);
        binding.setMainBean(mainStandard.getMainBean());
        binding.setAction(mainListener);

        initView();
    }

    private void initView() {
        mainListener.initVPContainer();

        binding.tbTitle.setTitle("位置获取中...")
                .setImageLeftResource(R.drawable.ic_location, 0)
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoactionStatus();
                    }
                })
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopMenu(v);
                    }
                });

        initLocation();

        SQLiteStudioService.instance().start(activity);

        networkBroadcastReceiver = new NetworkBroadcastReceiver();
        registerReceiver(networkBroadcastReceiver, new IntentFilter(NetworkBroadcastReceiver
                .ACTION_TAG));
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
    protected void receiveStickyEventMessage(EventMessageModel messageModel) {
        if (messageModel.getMessageType() == EventBusConstant.EB_TYPE_TOKEN_INVALID_RELOGIN) {
            UITransfer.toReloginDialog(this);
        }
    }

    private void initLocation() {
        mainStandard.getLocation(new DefaultCallback<String, String>() {
            @Override
            public void success(String desc) {
                binding.tbTitle.setTitle(desc);
            }

            @Override
            public void error(String errorMessage) {
                binding.tbTitle.setTitle("未知");
                showLocationDialog();
                locationTip.setTitleShow("定位失败");
                locationTip.setContentShow(errorMessage);
            }
        });
    }

    public void showLocationDialog() {
        if (locationTip == null) {
            locationTip = new LocationDialogFragment();
        }
        locationTip.setResetLocationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tbTitle.setTitle("位置重新获取中...");
                initLocation();
                locationTip.dismiss();
            }
        });
        locationTip.show(getSupportFragmentManager(), "LocationDialogFragment");
    }

    public void showLoactionStatus() {
        int status = mainStandard.getLocationStatus();
        switch (status) {
            case BaiduLocation.NOT_START:
            case BaiduLocation.POSITIONING:
                showLocationDialog();
                locationTip.setTitleShow("当前位置");
                locationTip.setContentShow("正在获取当前位置信息...");
                break;
            case BaiduLocation.COMPLETE_FAILD:
                showLocationDialog();
                locationTip.setTitleShow("定位失败");
                locationTip.setContentShow("获取当前位置失败,请检测是否已经连网！");
                break;
            case BaiduLocation.COMPLETE_SUCCESS:
                showLocationDialog();
                locationTip.setTitleShow("当前位置");
                locationTip.setContentShow(UserPosSP.getPosDesc());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
//                        UITransfer.toChatGroupActivity(activity, UserPosSP.getGroupGuid());
                        break;
                    case R.id.pop_near_message:
//                        UITransfer.toNearMessagesActivity(activity);
                        break;
                    case R.id.pop_near_publish:
//                        UITransfer.toNearPublishActivity(activity);
                        break;
                    case R.id.pop_share_app:
                        new AlertDialog.Builder(activity)
                                .setMessage("如果别人就在你的旁边，你可以通过二维码来分享给TA...")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                        UITransfer.toQRCodeActivity(activity);
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("用其它方式", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ShareTransfer.shareApp();
                                    }
                                })
                                .show();

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
            fragments.add(new FragmentRecord());
            fragments.add(new FragmentNear());
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
}
