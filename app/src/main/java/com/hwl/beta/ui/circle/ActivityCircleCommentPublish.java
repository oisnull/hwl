//package com.hwl.beta.ui.circle;
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
//import com.hwl.beta.net.circle.CircleService;
//import com.hwl.beta.net.circle.body.AddCircleCommentInfoResponse;
//import com.hwl.beta.ui.convert.DBCircleAction;
//import com.hwl.beta.utils.StringUtils;
//
//import org.greenrobot.eventbus.EventBus;
//
//public class ActivityCircleCommentPublish extends FragmentActivity {
//
//    Activity activity;
////    EmotionDefaultPannel edpEmotion;
//    long circleId = 0;
//    long publishUserId = 0;
//    String circleContent = "";
//    long replyUserId = 0;
//    String replyUserName = "";
//    boolean isRuning = false;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.circle_activity_comment_publish);
//        activity = this;
//
//        circleId = getIntent().getLongExtra("circleid", 0);
//        if (circleId <= 0) {
//            Toast.makeText(activity, "参数错误", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//        replyUserId = getIntent().getLongExtra("replyuserid", 0);
//        replyUserName = getIntent().getStringExtra("replyusername");
//        publishUserId = getIntent().getLongExtra("publishuserid", 0);
//        circleContent = getIntent().getStringExtra("content");
//
////        edpEmotion = findViewById(R.id.edp_emotion);
////        edpEmotion.setEditTextFocus(false);
////        if (StringUtils.isNotBlank(replyUserName)) {
////            edpEmotion.setHintText("回复 " + replyUserName + " :");
////        } else {
////            edpEmotion.setHintText("输入评论内容");
////        }
////        edpEmotion.setDefaultPannelListener(new EmotionDefaultPannel.IDefaultPannelListener() {
////            @Override
////            public boolean onSendMessageClick(String text) {
////                if (isRuning) return false;
////                isRuning = true;
////
////                if (StringUtils.isBlank(text)) {
////                    Toast.makeText(activity, "发布的内容不能为空", Toast.LENGTH_SHORT).show();
////                    isRuning = false;
////                    return false;
////                }
////                publishComment(text);
////                return false;
////            }
////        });
//    }
//
//    private void publishComment(String content) {
//        edpEmotion.setSendButtonText("正在发送...");
//        CircleService.addComment(circleId, content, replyUserId)
//                .subscribe(new NetDefaultObserver<AddCircleCommentInfoResponse>() {
//                    @Override
//                    protected void onSuccess(AddCircleCommentInfoResponse res) {
//                        isRuning = false;
//                        if (res.getCommentInfo() != null && res.getCommentInfo().getCommentId() > 0) {
//                            Toast.makeText(activity, "发布成功", Toast.LENGTH_SHORT).show();
//                            EventBus.getDefault().post(new EventActionCircleComment(EventBusConstant.EB_TYPE_ACTINO_ADD, DBCircleAction.convertToCircleCommentInfo(res.getCommentInfo())));
//                            CircleMessageSend.sendAddCommentMessage(
//                                    circleId,
//                                    publishUserId,
//                                    replyUserId,
//                                    replyUserName,
//                                    res.getCommentInfo().getCommentId(),
//                                    res.getCommentInfo().getContent(),
//                                    circleContent).subscribe();
//                            finish();
//                        } else {
//                            onError("发布失败");
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
