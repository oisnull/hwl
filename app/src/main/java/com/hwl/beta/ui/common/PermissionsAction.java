package com.hwl.beta.ui.common;

import android.Manifest;
import android.app.Activity;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class PermissionsAction {

//Ex: https://www.cnblogs.com/dingxiansen/p/10670707.html
//doc:https://github.com/googlesamples/easypermissions
//doc:https://www.jianshu.com/p/6e306729bb0d

    private static boolean isHas = false;

    public static boolean checkCamera(Activity activity) {
        isHas = false;
//        , Manifest.permission
//                .MOUNT_UNMOUNT_FILESYSTEMS
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions
                .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        isHas = aBoolean;
                    }
                });

        return isHas;
    }

    public static boolean checkVoice(Activity activity) {
        isHas = false;
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions
                .request(Manifest.permission.RECORD_AUDIO)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        isHas = aBoolean;
                    }
                });

        return isHas;
    }

    public static boolean checkLocation(Activity activity) {
        isHas = false;
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions
                .request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        isHas = aBoolean;
                    }
                });

        return isHas;
//        int checkPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest
//        .permission.ACCESS_COARSE_LOCATION);
//        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest
//            .permission.ACCESS_COARSE_LOCATION}, 1);
//            Log.d("TTTT", "弹出提示");
//            return;
//        } else {
//            mLocationClient.start();
//        }
    }
}
