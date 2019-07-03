package com.hwl.beta.ui.circle;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.CircleActivityUserIndexBinding;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.circle.action.ICircleUserItemListener;
import com.hwl.beta.ui.circle.adp.CircleUserIndexAdapter;
import com.hwl.beta.ui.circle.logic.CircleUserLogic;
import com.hwl.beta.ui.circle.standard.CircleUserStandard;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.utils.NetworkUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class ActivityCircleUserIndex extends BaseActivity {

    FragmentActivity activity;
    CircleActivityUserIndexBinding binding;
    CircleUserStandard circleStanad;
    CircleUserIndexAdapter circleAdapter;
    CircleUserStandard circleStandard;
    Friend currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long viewUserId = getIntent().getLongExtra("viewuserid", 0);
        if (viewUserId <= 0) {
            Toast.makeText(activity, "用户不存在", Toast.LENGTH_SHORT).show();
            finish();
        }

        activity = this;
        circleStanad = new CircleUserLogic(viewUserId, getIntent().getStringExtra("viewusername")
                , getIntent().getStringExtra("viewuserimage"));
        currentUser = circleStanad.getLocalUserInfo();
        binding = DataBindingUtil.setContentView(activity, R.layout.circle_activity_user_index);
        initView();
    }

    private void initView() {
        binding.tbTitle
                .setTitle(currentUser.getId() == UserSP.getUserId() ? "我的动态" : "TA的动态")
                .setImageRightResource(R.drawable.ic_sms)
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UITransfer.toCircleMessagesActivity(activity);
                    }
                })
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        circleAdapter = new CircleUserIndexAdapter(activity, new CircleUserItemListener());
        binding.rvCircleContainer.setAdapter(circleAdapter);
        binding.rvCircleContainer.setLayoutManager(new LinearLayoutManager(activity));

//      binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//          @Override
//          public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//          }
//      });
        binding.refreshLayout.setEnableRefresh(false);
        //binding.refreshLayout.setEnableLoadMore(false);
        binding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                loadServerInfos(circleAdapter.getMinId());
            }
        });

        this.loadServerUserInfo();
        this.loadLocalInfos();
    }

    private void loadLocalInfos() {
        circleStandard.loadLocalInfos()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Circle>>() {
                    @Override
                    public void accept(List<Circle> infos) {
                        circleAdapter.addInfos(infos);
                        loadServerInfos(0);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadServerInfos(long infoId) {
        if (!NetworkUtils.isConnected()) {
            showResult();
            return;
        }

        final boolean isRefresh = infoId == 0;
        circleStandard.loadServerInfos(infoId, circleAdapter.getInfos())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Circle>>() {
                    @Override
                    public void accept(List<Circle> infos) {
                        circleAdapter.updateInfos(infos);
                        showResult();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        showResult();
                        Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showResult() {
        if (circleAdapter.getItemCount() <= 0) {
            circleAdapter.setEmptyInfo();
            binding.refreshLayout.setEnableLoadMore(false);
        }
        binding.refreshLayout.finishLoadMore();
    }

    private void loadServerUserInfo() {
        circleStandard.loadServerUserInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Friend>() {
                    @Override
                    public void accept(Friend info) {
                        circleAdapter.updateHead(info);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        //Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT)
                        // .show();
                    }
                });
    }

    public class CircleUserItemListener implements ICircleUserItemListener {

        @Override
        public void onItemNullViewClick() {
            UITransfer.toCirclePublishActivity(activity);
        }

        @Override
        public void onItemViewClick(Circle info) {
            //UITransfer.toCircleDetailActivity(activity, 0, info);
        }

        @Override
        public void onBackImageClick() {

        }
    }
}
