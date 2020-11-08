package com.hwl.beta.ui.common;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.sp.UserPosSP;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.TestActivity;
import com.hwl.beta.ui.TestActivityLogs;
import com.hwl.beta.ui.chat.ActivityChatGroup;
import com.hwl.beta.ui.chat.ActivityChatGroupSetting;
import com.hwl.beta.ui.chat.ActivityChatGroupSettingEdit;
import com.hwl.beta.ui.circle.ActivityCircleDetail;
import com.hwl.beta.ui.circle.ActivityCircleIndex;
import com.hwl.beta.ui.circle.ActivityCirclePublish;
import com.hwl.beta.ui.circle.ActivityCircleUserIndex;
import com.hwl.beta.ui.TestActivityIm;
import com.hwl.beta.ui.TestActivityLocation;
import com.hwl.beta.ui.chat.ActivityChatUser;
import com.hwl.beta.ui.chat.ActivityChatUserSetting;
import com.hwl.beta.ui.circle.ActivityCircleMessages;
import com.hwl.beta.ui.dialog.ReloginDialogFragment;
import com.hwl.beta.ui.emoji.ActivityEmojiSetting;
import com.hwl.beta.ui.emoji.ActivityEmojiStore;
import com.hwl.beta.ui.entry.ActivityGetpwd;
import com.hwl.beta.ui.entry.ActivityLogin;
import com.hwl.beta.ui.entry.ActivityLoginV2;
import com.hwl.beta.ui.entry.ActivityMain;
import com.hwl.beta.ui.entry.ActivityQRCode;
import com.hwl.beta.ui.entry.ActivityRegister;
import com.hwl.beta.ui.group.ActivityGroup;
import com.hwl.beta.ui.group.ActivityGroupAdd;
import com.hwl.beta.ui.group.ActivityGroupAllUsers;
import com.hwl.beta.ui.imgselect.ActivityImageBrowse;
import com.hwl.beta.ui.imgselect.ActivityImageSelect;
import com.hwl.beta.ui.near.ActivityNearDetail;
import com.hwl.beta.ui.near.ActivityNearMessages;
import com.hwl.beta.ui.near.ActivityNearPublish;
import com.hwl.beta.ui.user.ActivityNewFriend;
import com.hwl.beta.ui.user.ActivityUserEdit;
import com.hwl.beta.ui.user.ActivityUserEditItem;
import com.hwl.beta.ui.user.ActivityUserIndexV2;
import com.hwl.beta.ui.user.ActivityUserSearch;
import com.hwl.beta.ui.video.ActivityVideoPlay;
import com.hwl.beta.ui.video.ActivityVideoSelect;
import com.hwl.beta.ui.entry.ActivityWelcome;
import com.hwl.beta.ui.immsg.IMClientEntry;
import com.hwl.beta.utils.StorageUtils;
import com.hwl.beta.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/27.
 */

public class UITransfer {

    public static void toTestActivity(Activity context) {
        Intent intent = new Intent(context, TestActivity.class);
        context.startActivity(intent);
    }

    public static void toTestActivityLocation(Activity context) {
        Intent intent = new Intent(context, TestActivityLocation.class);
        context.startActivity(intent);
    }

    public static void toTestActivityIM(Activity context) {
        Intent intent = new Intent(context, TestActivityIm.class);
        context.startActivity(intent);
    }

    public static void toTestActivityLogs(Activity context) {
        Intent intent = new Intent(context, TestActivityLogs.class);
        context.startActivity(intent);
    }

    public static void toQRCodeActivity(Activity context) {
        Intent intent = new Intent(context, ActivityQRCode.class);
        context.startActivity(intent);
    }

    public static void toLoginActivity(Activity context) {
        Intent intent = new Intent(context, ActivityLogin.class);
        context.startActivity(intent);
    }

    public static void toLoginActivityV2(Activity context) {
        Intent intent = new Intent(context, ActivityLoginV2.class);
        context.startActivity(intent);
    }

    public static void toRegisterActivity(Activity context) {
        Intent intent = new Intent(context, ActivityRegister.class);
        context.startActivity(intent);
    }

    public static void toGetpwdActivity(Activity context) {
        Intent intent = new Intent(context, ActivityGetpwd.class);
        context.startActivity(intent);
    }

    public static void toWelcomeActivity(Activity context) {
        Intent intent = new Intent(context, ActivityWelcome.class);
        context.startActivity(intent);
    }

    public static void toLogout(Activity context) {
        String userAccount = UserSP.getAccount();
        UserSP.clearUserInfo();
        UserPosSP.clearPosInfo(true);
        DaoUtils.closeDB();
        toWelcomeActivity(context);
        UserSP.setAccount(userAccount);
        IMClientEntry.disconnectServer();
    }

    public static void toMainActivity(Activity context) {
        Intent intent = new Intent(context, ActivityMain.class);
        context.startActivity(intent);
    }
//
//    public static void toUserSettingActivity(Activity context) {
//        Intent intent = new Intent(context, ActivityUserSetting.class);
//        context.startActivity(intent);
//    }
//
//    public static void toUserMessageSettingActivity(Activity context) {
//        Intent intent = new Intent(context, ActivityUserMessageSetting.class);
//        context.startActivity(intent);
//    }
//
//    public static void toUserPrivacySettingActivity(Activity context) {
//        Intent intent = new Intent(context, ActivityUserPrivacySetting.class);
//        context.startActivity(intent);
//    }
//
//    public static void toUserPasswordResetActivity(Activity context) {
//        Intent intent = new Intent(context, ActivityUserPasswordReset.class);
//        context.startActivity(intent);
//    }

    public static void toUserIndexActivity(Context context, long userId, String userName, String
            userImage) {
        Intent intent = new Intent(context, ActivityUserIndexV2.class);
        intent.putExtra("userid", userId);
        intent.putExtra("username", userName);
        intent.putExtra("userimage", userImage);
        context.startActivity(intent);
    }

    public static void toUserIndexV2Activity(Context context, long userId, String userName, String
            userImage) {
        toUserIndexActivity(context, userId, userName, userImage);
    }

    public static void toUserSearchActivity(Activity context) {
        Intent intent = new Intent(context, ActivityUserSearch.class);
        context.startActivity(intent);
    }

    public static void toNewFriendActivity(Activity context) {
        Intent intent = new Intent(context, ActivityNewFriend.class);
        context.startActivity(intent);
    }

    public static void toUserEditActivity(Activity context) {
        Intent intent = new Intent(context, ActivityUserEdit.class);
        context.startActivity(intent);
    }

    public static void toUserEditItemActivity(Activity context, int actoinType, String
            editContent) {
        toUserEditItemActivity(context, actoinType, editContent, 0);
    }

    public static void toUserEditItemActivity(Activity context, int actoinType, String
            editContent, long friendId) {
        Intent intent = new Intent(context, ActivityUserEditItem.class);
        intent.putExtra("actiontype", actoinType);
        intent.putExtra("editcontent", editContent);
        if (friendId > 0) {
            intent.putExtra("friendid", friendId);
        }
        context.startActivity(intent);
    }

    public static void toSystemCamera(Activity activity, int requestCode) {
        toSystemCamera(activity, StorageUtils.getUriForTempFile(), requestCode);
    }

    public static void toSystemCamera(Activity activity, Uri saveUri, int requestCode) {
        if (!PermissionsAction.checkCamera(activity)) return;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent
                    .FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void toImageSelectActivity(final Activity context, final int selectType, final
    int requestCode) {
        toImageSelectActivity(context, selectType, 1, requestCode);
    }

    public static void toImageSelectActivity(Activity context, int selectType, int selectCount,
                                             int requestCode) {
        if (!PermissionsAction.checkCamera(context)) {
            return;
        }

        Intent intent = new Intent(context, ActivityImageSelect.class);
        intent.putExtra("selecttype", selectType);
        intent.putExtra("selectcount", selectCount);
//        context.startActivity(intent);
        context.startActivityForResult(intent, requestCode);
    }

    public static void toImageBrowseActivity(Activity context, String imageUrl) {
        if (StringUtils.isBlank(imageUrl)) return;
        List<String> imgs = new ArrayList<>(1);
        imgs.add(imageUrl);
        toImageBrowseActivity(context, ActivityImageBrowse.MODE_VIEW, 0, imgs);
    }

    public static void toImageBrowseActivity(Activity context, int mode, int position,
                                             List<String> imageUrls) {
        if (imageUrls == null || imageUrls.size() <= 0) return;
        Intent intent = new Intent(context, ActivityImageBrowse.class);
        intent.putExtra("mode", mode);
        intent.putExtra("position", position);
        intent.putStringArrayListExtra("imageurls", (ArrayList<String>) imageUrls);
        context.startActivity(intent);
    }

    public static void toChatUserActivity(Activity context, long userId, String userName, String
            userImage) {
        toChatUserActivity(context, userId, userName, userImage, 0);
    }

    public static void toChatUserActivity(Activity context, long userId, String userName, String
            userImage, long recordId) {
        Intent intent = new Intent(context, ActivityChatUser.class);
        intent.putExtra("userid", userId);
        intent.putExtra("username", userName);
        intent.putExtra("userimage", userImage);
        intent.putExtra("recordid", recordId);
        context.startActivity(intent);
    }

    public static void toVideoSelectActivity(Activity context, int requestCode) {
        Intent intent = new Intent(context, ActivityVideoSelect.class);
        context.startActivityForResult(intent, requestCode);
    }

    public static void toVideoPlayActivity(Activity context, int videoMode, String videoPath) {
        toVideoPlayActivity(context, videoMode, videoPath, 0);
    }

    public static void toVideoPlayActivity(Activity context, int videoMode, String videoPath, int
            requestCode) {
        Intent intent = new Intent(context, ActivityVideoPlay.class);
        intent.putExtra("videopath", videoPath);
        intent.putExtra("videomode", videoMode);
        if (requestCode <= 0) {
            context.startActivity(intent);
        } else {
            context.startActivityForResult(intent, requestCode);
        }
    }

    public static void toChatGroupActivity(Context context, String groupGuid) {
        Intent intent = new Intent(context, ActivityChatGroup.class);
        intent.putExtra("groupguid", groupGuid);
        context.startActivity(intent);
    }

    public static void toNearPublishActivity(Activity context) {
        Intent intent = new Intent(context, ActivityNearPublish.class);
        context.startActivity(intent);
    }

    public static void toNearDetailActivity(Activity context, long nearCircleId) {
        Intent intent = new Intent(context, ActivityNearDetail.class);
        intent.putExtra("nearcircleid", nearCircleId);
        context.startActivity(intent);
    }

    public static void toNearMessagesActivity(Activity context) {
        Intent intent = new Intent(context, ActivityNearMessages.class);
        context.startActivity(intent);
    }

    public static void toCircleIndexActivity(Activity context) {
        Intent intent = new Intent(context, ActivityCircleIndex.class);
        context.startActivity(intent);
    }

    public static void toCirclePublishActivity(Activity context) {
        Intent intent = new Intent(context, ActivityCirclePublish.class);
        context.startActivity(intent);
    }

    public static void toCircleUserIndexActivity(Activity context,
                                                 long viewUserId,
                                                 String viewUserName,
                                                 String viewUserImage) {
        Intent intent = new Intent(context, ActivityCircleUserIndex.class);
        intent.putExtra("viewuserid", viewUserId);
        intent.putExtra("viewusername", viewUserName);
        intent.putExtra("viewuserimage", viewUserImage);
        context.startActivity(intent);
    }

    public static void toCircleDetailActivity(Activity context, long circleId) {
        Intent intent = new Intent(context, ActivityCircleDetail.class);
        intent.putExtra("circleid", circleId);
        context.startActivity(intent);
    }

    public static void toCircleMessagesActivity(Activity context) {
        Intent intent = new Intent(context, ActivityCircleMessages.class);
        context.startActivity(intent);
    }

    public static void toGroupActivity(Activity context) {
        Intent intent = new Intent(context, ActivityGroup.class);
        context.startActivity(intent);
    }

    public static void toGroupAllUsersActivity(Activity context, String groupGuid) {
        Intent intent = new Intent(context, ActivityGroupAllUsers.class);
        intent.putExtra("groupguid", groupGuid);
        context.startActivity(intent);
    }

    public static void toGroupAddActivity(Activity context) {
        toGroupAddActivity(context, ActivityGroupAdd.TYPE_CREATE, null);
    }

    public static void toGroupAddActivity(Activity context, int actionType, String groupGuid) {
        Intent intent = new Intent(context, ActivityGroupAdd.class);
        intent.putExtra("actiontype", actionType);
        intent.putExtra("groupguid", groupGuid);
        context.startActivity(intent);
    }

    public static void toReloginDialog(FragmentActivity fragmentActivity) {
        toReloginDialog(fragmentActivity, null);
    }

    private static ReloginDialogFragment reloginDialogFragment = null;

    public static void toReloginDialog(final FragmentActivity fragmentActivity, String
            hintContent) {
        if (reloginDialogFragment != null && reloginDialogFragment.isVisible()) return;
        reloginDialogFragment = new ReloginDialogFragment();
        if (StringUtils.isNotBlank(hintContent)) {
            reloginDialogFragment.setHintText(hintContent);
        }
        reloginDialogFragment.setReloginClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloginDialogFragment.dismiss();
                reloginDialogFragment = null;
                toLogout(fragmentActivity);
                fragmentActivity.finish();
            }
        });
        reloginDialogFragment.show(fragmentActivity.getSupportFragmentManager(),
                "ReloginDialogFragment");
    }

    public static void toChatGroupSettingActivity(Activity context, String groupGuid) {
        Intent intent = new Intent(context, ActivityChatGroupSetting.class);
        intent.putExtra("groupguid", groupGuid);
        context.startActivity(intent);
    }

    public static void toChatGroupSettingEditActivity(Activity context, String groupGuid, int
            editType, String content) {
        Intent intent = new Intent(context, ActivityChatGroupSettingEdit.class);
        intent.putExtra("groupguid", groupGuid);
        intent.putExtra("edittype", editType);
        intent.putExtra("content", content);
        context.startActivityForResult(intent, editType);
    }

    public static void toChatUserSettingActivity(Activity context, long userId, String userName,
                                                 String userImage) {
        Intent intent = new Intent(context, ActivityChatUserSetting.class);
        intent.putExtra("userid", userId);
        intent.putExtra("username", userName);
        intent.putExtra("userimage", userImage);
        context.startActivity(intent);
    }

    /**
     * 调用第三方浏览器打开
     *
     * @param context
     * @param url     要浏览的资源地址
     */
    public static void toBrowser(Activity context, String url) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
        // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            final ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            // 打印Log   ComponentName到底是什么
//            L.d("componentName = " + componentName.getClassName());
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        }
    }

    public static void toEmojiStoreActivity(Activity context) {
        Intent intent = new Intent(context, ActivityEmojiStore.class);
        context.startActivity(intent);
    }

    public static void toEmojiSettingActivity(Activity context) {
        Intent intent = new Intent(context, ActivityEmojiSetting.class);
        context.startActivity(intent);
    }
}
