//package com.hwl.beta.ui.near;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.FragmentActivity;
//import android.view.Gravity;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Toast;
//
//import com.hwl.beta.R;
//import com.hwl.beta.emotion.EmotionDefaultPannel;
//import com.hwl.beta.mq.send.NearCircleMessageSend;
//import com.hwl.beta.net.near.NearCircleService;
//import com.hwl.beta.net.near.body.AddNearCommentResponse;
//import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
//import com.hwl.beta.ui.convert.DBNearCircleAction;
//import com.hwl.beta.utils.StringUtils;
//
//import org.greenrobot.eventbus.EventBus;
//
//public class ActivityCommentPublish extends FragmentActivity {
//
//    Activity activity;
//    EmotionDefaultPannel edpEmotion;
//    long nearCircleId = 0;
//    long publishUserId = 0;
//    String circleContent = "";
//    long replyUserId = 0;
//    String replyUserName = "";
//    boolean isRuning = false;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_comment_publish);
//        activity = this;
//
//        nearCircleId = getIntent().getLongExtra("nearcircleid", 0);
//        if (nearCircleId <= 0) {
//            Toast.makeText(activity, "参数错误", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//        replyUserId = getIntent().getLongExtra("replyuserid", 0);
//        replyUserName = getIntent().getStringExtra("replyusername");
//        publishUserId = getIntent().getLongExtra("publishuserid", 0);
//        circleContent = getIntent().getStringExtra("content");
//
//        edpEmotion = findViewById(R.id.edp_emotion);
//        edpEmotion.setEditTextFocus(false);
//        if (StringUtils.isNotBlank(replyUserName)) {
//            edpEmotion.setHintText("回复 " + replyUserName + " :");
//        } else {
//            edpEmotion.setHintText("输入评论内容");
//        }
//        edpEmotion.setDefaultPannelListener(new EmotionDefaultPannel.IDefaultPannelListener() {
//            @Override
//            public boolean onSendMessageClick(String text) {
//                if (isRuning) return false;
//                isRuning = true;
//
//                if (StringUtils.isBlank(text)) {
//                    Toast.makeText(activity, "发布的内容不能为空", Toast.LENGTH_SHORT).show();
//                    isRuning = false;
//                    return false;
//                }
//                publishComment(text);
//                return false;
//            }
//        });
//    }
//
//    private void publishComment(String content) {
//        edpEmotion.setSendButtonText("正在发送...");
//        NearCircleService.addComment(nearCircleId, content, replyUserId)
//                .subscribe(new NetDefaultObserver<AddNearCommentResponse>() {
//                    @Override
//                    protected void onSuccess(AddNearCommentResponse res) {
//                        isRuning = false;
//                        if (res.getNearCircleCommentInfo() != null && res.getNearCircleCommentInfo().getCommentId() > 0) {
//                            Toast.makeText(activity, "评论发送成功", Toast.LENGTH_SHORT).show();
//                            EventBus.getDefault().post(DBNearCircleAction.convertToNearCircleCommentInfo(res.getNearCircleCommentInfo()));
//                            NearCircleMessageSend.sendAddCommentMessage(
//                                    nearCircleId,
//                                    publishUserId,
//                                    replyUserId,
//                                    replyUserName,
//                                    res.getNearCircleCommentInfo().getCommentId(),
//                                    res.getNearCircleCommentInfo().getContent(),
//                                    circleContent).subscribe();
//                            finish();
//                        } else {
//                            onError("评论发送失败");
//                        }
//                    }
//
//                    @Override
//                    protected void onError(String resultMessage) {
//                        edpEmotion.setSendButtonText("发送");
//                        isRuning = false;
//                        super.onError(resultMessage);
//                    }
//                });
//    }
//
//    @Override
//    public void onAttachedToWindow() {
//        super.onAttachedToWindow();
//
//        View view = getWindow().getDecorView();
//        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
//        lp.gravity = Gravity.BOTTOM;
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
////        lp.alpha = 0.1f;
//        getWindowManager().updateViewLayout(view, lp);
//    }
//}
