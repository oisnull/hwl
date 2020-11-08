package com.hwl.beta.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.core.content.FileProvider;
import android.util.Log;

import com.hwl.beta.HWLApp;

import java.io.File;
import java.io.IOException;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Methods get storage path samples:
 * <p>
 * getCacheDirectory: /storage/emulated/0/Android/data/com.example.google_acmer.asimplecachedemo/cache
 * getCacheDirectory true: /storage/emulated/0/Android/data/com.example.google_acmer.asimplecachedemo/cache
 * getCacheDirectory false: /data/user/0/com.example.google_acmer.asimplecachedemo/cache
 * getIndividualCacheDirectory:/storage/emulated/0/Android/data/com.example.google_acmer.asimplecachedemo/cache/uil-images
 * getOwnCacheDirectory:/storage/emulated/0/JayGoo
 */
public final class StorageUtils {

    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    //    private static final String INDIVIDUAL_DIR_NAME = "uil-images";
    private static final String TAG = "StorageUtils";
    private static final String TEMP_IMAGE_FILE_NAME = "temp.png";
    //与xml/filepaths.xml中的name对应的path配置一样
//    private static final String TEMP_DIR_NAME = "hwl_temp";

    private StorageUtils() {
    }

    public static File getTempImageFile() {
        File file = new File(getTempImageFilePath());
        if (!file.getParentFile().exists()) {
            try {
                file.getParentFile().mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static String getTempImageFilePath() {
        if (Build.VERSION.SDK_INT >= 24)
            return HWLApp.getContext().getCacheDir() + File.separator + TEMP_IMAGE_FILE_NAME;

        return getCacheDirectory(false) + File.separator + TEMP_IMAGE_FILE_NAME;
    }

    public static Uri getUriForTempFile() {
        return getUriForFile(getTempImageFile());
    }

//    public static String getRootPath() {
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//            return Environment.getExternalStorageDirectory().getAbsolutePath(); // filePath:  /sdcard/
//        } else {
//            return Environment.getDataDirectory().getAbsolutePath() + "/data"; // filePath:  /data/data/
//        }
//    }

    public static Uri getUriForFile(File file) {
        if (file == null) {
            throw new NullPointerException();
        }
//        Log.d(TAG, "StorageUtils2=" + file.getAbsolutePath());
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(HWLApp.getContext(), HWLApp.getContext().getPackageName() + ".fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
//        Log.d(TAG, "StorageUtils3=" + uri.getPath());
        return uri;
    }

    /**
     * Returns application cache directory. Cache directory will be created on SD card
     * <i>("/Android/data/[app_package_name]/cache")</i> if card is mounted and app has appropriate permission. Else -
     * Android defines cache directory on device's file system.
     *
     * @return Cache {@link File directory}.<br />
     * <b>NOTE:</b> Can be null in some unpredictable cases (if SD card is unmounted and
     * {@link Context#getCacheDir() Context.getCacheDir()} returns null).
     */
    public static File getCacheDirectory() {
        return getCacheDirectory(true);
    }

    /**
     * Returns application cache directory. Cache directory will be created on SD card
     * <i>("/Android/data/[app_package_name]/cache")</i> (if card is mounted and app has appropriate permission) or
     * on device's file system depending incoming parameters.
     *
     * @param preferExternal Whether prefer external location for cache
     * @return Cache {@link File directory}.<br />
     * <b>NOTE:</b> Can be null in some unpredictable cases (if SD card is unmounted and
     * {@link Context#getCacheDir() Context.getCacheDir()} returns null).
     */
    public static File getCacheDirectory(boolean preferExternal) {
        File appCacheDir = null;
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) { // (sh)it happens (Issue #660)
            externalStorageState = "";
        }
        if (preferExternal && MEDIA_MOUNTED.equals(externalStorageState) && hasExternalStoragePermission(HWLApp.getContext())) {
            appCacheDir = getExternalCacheDir(HWLApp.getContext());
        }
        if (appCacheDir == null) {
            appCacheDir = HWLApp.getContext().getCacheDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + HWLApp.getContext().getPackageName() + "/cache/";
            Log.d(TAG, "Can't define system cache directory! " + cacheDirPath + " will be used.");
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

//    /**
//     * Returns individual application cache directory (for only image caching from ImageLoader). Cache directory will be
//     * created on SD card <i>("/Android/data/[app_package_name]/cache/uil-images")</i> if card is mounted and app has
//     * appropriate permission. Else - Android defines cache directory on device's file system.
//     *
//     * @param context Application context
//     * @return Cache {@link File directory}
//     */
//    public static File getIndividualCacheDirectory(Context context) {
//        File cacheDir = getCacheDirectory(context);
//        File individualCacheDir = new File(cacheDir, INDIVIDUAL_DIR_NAME);
//        if (!individualCacheDir.exists()) {
//            if (!individualCacheDir.mkdir()) {
//                individualCacheDir = cacheDir;
//            }
//        }
//        return individualCacheDir;
//    }

    /**
     * Returns specified application cache directory. Cache directory will be created on SD card by defined path if card
     * is mounted and app has appropriate permission. Else - Android defines cache directory on device's file system.
     *
     * @param context  Application context
     * @param cacheDir Cache directory path (e.g.: "AppCacheDir", "AppDir/cache/images")
     * @return Cache {@link File directory}
     */
    public static File getOwnCacheDirectory(Context context, String cacheDir) {
        File appCacheDir = null;
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
        }
        if (appCacheDir == null || (!appCacheDir.exists() && !appCacheDir.mkdirs())) {
            appCacheDir = context.getCacheDir();
        }
        return appCacheDir;
    }

    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                Log.d(TAG, "Unable to create external cache directory");
                return null;
            }
            try {
                new File(appCacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
                Log.d(TAG, "Can't create \".nomedia\" file in application external cache directory");
            }
        }
        return appCacheDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

}

