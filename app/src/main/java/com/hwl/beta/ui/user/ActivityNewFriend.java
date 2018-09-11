//package com.hwl.beta.ui.user;
//
//import android.app.Activity;
//import android.content.DialogInterface;
//import android.databinding.DataBindingUtil;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AlertDialog;
//import android.view.View;
//import android.widget.Toast;
//
//import com.hwl.beta.R;
//import com.hwl.beta.databinding.ActivityNewFriendBinding;
//import com.hwl.beta.db.DaoUtils;
//import com.hwl.beta.db.entity.ChatRecordMessage;
//import com.hwl.beta.db.entity.ChatUserMessage;
//import com.hwl.beta.db.entity.Friend;
//import com.hwl.beta.db.entity.FriendRequest;
//import com.hwl.beta.mq.MQConstant;
//import com.hwl.beta.mq.send.ChatMessageSend;
//import com.hwl.beta.net.NetConstant;
//import com.hwl.beta.net.user.NetUserInfo;
//import com.hwl.beta.net.user.UserService;
//import com.hwl.beta.net.user.body.AddFriendResponse;
//import com.hwl.beta.sp.MessageCountSP;
//import com.hwl.beta.sp.UserSP;
//import com.hwl.beta.ui.busbean.EventBusConstant;
//import com.hwl.beta.ui.common.BaseActivity;
//import com.hwl.beta.ui.common.rxext.MQDefaultObserver;
//import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
//import com.hwl.beta.ui.convert.DBFriendAction;
//import com.hwl.beta.ui.dialog.LoadingDialog;
//import com.hwl.beta.ui.user.action.INewFriendItemListener;
//import com.hwl.beta.ui.user.adp.NewFriendAdapter;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by Administrator on 2018/1/8.
// */
//
//public class ActivityNewFriend extends BaseActivity {
//
//    Activity activity;
//    List<FriendRequest> friendRequests;
//    NewFriendAdapter friendAdapter;
//    ActivityNewFriendBinding binding;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        activity = this;
//        friendRequests = DaoUtils.getFriendRequestManagerInstance().getAll();
//        if (friendRequests == null) {
//            friendRequests = new ArrayList<>();
//        } else {
//            MessageCountSP.setFriendRequestCount(0);//清空好友请求数据
//            EventBus.getDefault().post(EventBusConstant.EB_TYPE_FRIEND_REQUEST_UPDATE);
//        }
//        friendAdapter = new NewFriendAdapter(activity, friendRequests, new NewFriendItemListener());
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_friend);
//        binding.setFriendAdapter(friendAdapter);
//
//        binding.tbTitle
//                .setTitle("好友请求列表")
//                .setImageRightHide()
//                .setImageLeftClick(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        onBackPressed();
//                    }
//                });
//    }
//
//    public class NewFriendItemListener implements INewFriendItemListener {
//
//        @Override
//        public void onHeadImageClick(FriendRequest friendRequest) {
//
//        }
//
//        @Override
//        public void onRemarkClick(String remark) {
//            Toast.makeText(activity, remark, Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onCancelClick(View view, final FriendRequest friendRequest) {
//            new AlertDialog.Builder(activity)
//                    .setMessage("好友请求删除后,不能恢复,确认删除 ?")
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            DaoUtils.getFriendRequestManagerInstance().delete(friendRequest);
//                            friendRequests.remove(friendRequest);
//                            friendAdapter.notifyDataSetChanged();
//                            MessageCountSP.setFriendRequestCount(friendRequests.size());
//                            dialog.dismiss();
//                        }
//                    })
//                    .setNegativeButton("取消", null)
//                    .show();
//        }
//
//        @Override
//        public void onAddClick(View view, final FriendRequest friendRequest) {
//            LoadingDialog.show(activity);
//            UserService.addFriend(friendRequest.getFriendId(), friendRequest.getFriendName())
//                    .subscribe(new NetDefaultObserver<AddFriendResponse>() {
//                        @Override
//                        protected void onSuccess(AddFriendResponse response) {
//                            if (response != null && response.getStatus() == NetConstant.RESULT_SUCCESS && response.getFriendInfo() != null) {
//                                Toast.makeText(activity, "添加好友成功", Toast.LENGTH_SHORT).show();
//                                Friend friend = DBFriendAction.convertToFriendInfo(response.getFriendInfo());
//                                sendSuccessMessage(friend);//发送好友添加成功的聊天消息
//                                DaoUtils.getFriendManagerInstance().save(friend);
//                                DaoUtils.getFriendRequestManagerInstance().delete(friendRequest);
//                                friendRequests.remove(friendRequest);
//                                friendAdapter.notifyDataSetChanged();
//                                EventBus.getDefault().post(friend);
//                                MessageCountSP.setFriendRequestCount(friendRequests.size());
//                            }
//                            LoadingDialog.hide();
//                        }
//
//                        @Override
//                        protected void onError(String resultMessage) {
//                            super.onError(resultMessage);
//                            LoadingDialog.hide();
//                        }
//                    });
//        }
//
//        private void sendSuccessMessage(final Friend friend) {
//
//            final String content = "我们已经成为好友了";
//            ChatMessageSend.sendChatFriendRequestMessage(friend.getId(), content).subscribe(new MQDefaultObserver() {
//                @Override
//                protected void onSuccess() {
//                    //发送成功后，将这条消息更新到自己的本地数据库中
//                    NetUserInfo user = UserSP.getUserInfo();
//                    ChatRecordMessage record = new ChatRecordMessage();
//                    record.setRecordType(MQConstant.CHAT_RECORD_TYPE_USER);
//                    record.setToUserId(friend.getId());
//                    record.setToUserName(friend.getName());
//                    record.setToUserHeadImage(friend.getHeadImage());
//                    record.setFromUserId(user.getId());
//                    record.setFromUserName(user.getShowName());
//                    record.setFromUserHeadImage(user.getHeadImage());
//                    record.setRecordImage(friend.getHeadImage());
//                    record.setTitle(friend.getName());
//                    record.setContentType(MQConstant.CHAT_MESSAGE_CONTENT_TYPE_WORD);
//                    record.setContent(content);
////                    record.setUnreadCount(0);
//                    record.setSendTime(new Date());
//                    record = DaoUtils.getChatRecordMessageManagerInstance().addOrUpdate(record);
//
//                    ChatUserMessage message = new ChatUserMessage();
//                    message.setFromUserId(user.getId());
//                    message.setFromUserName(user.getShowName());
//                    message.setFromUserHeadImage(user.getHeadImage());
//                    message.setToUserId((int) friend.getId());
//                    message.setContentType(MQConstant.CHAT_MESSAGE_CONTENT_TYPE_WORD);
//                    message.setContent(content);
//                    message.setSendStatus(MQConstant.CHAT_SEND_SUCCESS);
//                    message.setSendTime(new Date());
//                    DaoUtils.getChatUserMessageManagerInstance().save(message);
//
//                    EventBus.getDefault().post(record);
//                    EventBus.getDefault().post(message);
//                }
//            });
//
////            MQChatMessage.sendChatFriendRequestMessage((int) friend.getId(), content, new IMessageCallBack() {
////                @Override
////                public void onSuccess() {
////                    //添加成功后，将这条消息更新到自己的本地数据库中  private Long msgId;
////                    UserInfo user = UserSP.getUserInfo();
////                    ChatRecordMessage record = new ChatRecordMessage();
////                    record.setRecordType(MQ_Constant.CHAT_RECORD_TYPE_USER);
////                    record.setToUserId((int) friend.getId());
////                    record.setToUserName(friend.getName());
////                    record.setToUserHeadImage(friend.getHeadImage());
////                    record.setFromUserId(user.getId());
////                    record.setFromUserName(user.getShowName());
////                    record.setFromUserHeadImage(user.getHeadImage());
////                    record.setRecordImage(friend.getHeadImage());
////                    record.setTitle(friend.getName());
////                    record.setContentType(MQ_Constant.CHAT_MESSAGE_CONTENT_TYPE_WORD);
////                    record.setContent(content);
////                    record.setUnreadCount(0);
////                    record.setSendTime(new Date());
////                    record.setRecordId(DaoUtils.getChatRecordMessageManagerInstance().addOrUpdate(record));
////
////                    ChatUserMessage message = new ChatUserMessage();
////                    message.setFromUserId(user.getId());
////                    message.setFromUserName(user.getShowName());
////                    message.setFromUserHeadImage(user.getHeadImage());
////                    message.setToUserId((int) friend.getId());
////                    message.setContentType(MQ_Constant.CHAT_MESSAGE_CONTENT_TYPE_WORD);
////                    message.setContent(content);
////                    message.setSendStatus(MQ_Constant.CHAT_SEND_SUCCESS);
////                    message.setSendTime(new Date());
////                    DaoUtils.getChatUserMessageManagerInstance().save(message);
////
////                    Intent it = new Intent(BaseActivity.BD_MAIN_NAME);
////                    it.putExtra("bd_type", BaseActivity.BD_TYPE_CHAT_USER_MESSAGE);
////                    Bundle bundle = new Bundle();
////                    bundle.putSerializable("record", record);
////                    it.putExtras(bundle);
////                    HWLApp.getContext().sendBroadcast(it);
////                }
////
////                @Override
////                public void onFaild(String resultMsg) {
////                    Log.d("NewFriendActivity", resultMsg);
////                }
////            });
//        }
//    }
//}
