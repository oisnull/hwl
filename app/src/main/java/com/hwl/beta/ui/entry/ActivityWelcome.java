package com.hwl.beta.ui.entry;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.hwl.beta.R;
import com.hwl.beta.net.user.NetUserInfo;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.immsg.IMClientEntry;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class ActivityWelcome extends FragmentActivity {

    private TextView tvCountdown;
    private int tmrCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        setContentView(R.layout.entry_activity_welcome);
        tvCountdown = findViewById(R.id.tv_countdown);
        tvCountdown.setText(tmrCount + " s");

        load();
    }

    private void load() {
        Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Long sec = tmrCount - (aLong + 1);
                        if (sec <= 0) {
                            check();
                            disposable.dispose();
                        } else {
                            tvCountdown.setText(sec + " s");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void check() {
        NetUserInfo user = UserSP.getUserInfo();
        if (user != null && user.getId() > 0) {
            IMClientEntry.connectServer();
            UITransfer.toMainActivity(this);
        } else {
            UITransfer.toLoginActivityV2(this);
        }
        this.finish();
    }
}
