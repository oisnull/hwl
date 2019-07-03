//package com.hwl.beta.ui.near.imp;
//
///**
// * Created by Administrator on 2019/06/25.
// */
//
//public class NearEmotionPanelListener implements EmotionDefaultPanelV2.IPanelListener {
//    private Activity activity;
//    private int position;
//    private NearCircle info;
//    private long replyUserId;
//    private Consumer<NearCircleComment> sendCallback;
//	
//	public NearEmotionPanelListener(Activity activity,Consumer<NearCircleComment> sendCallback){
//        this.activity = activity;
//        this.sendCallback = sendCallback;
//	}
//
//    public void setNearCircleInfo(int position, NearCircle info) {
//        this.position = position;
//        this.info = info;
//        this.replyUserId = 0;
//    }
//
//    public void setNearCircleInfo(int position, NearCircle info, long replyUserId) {
//        setNearCircleInfo(position, info);
//        this.replyUserId = replyUserId;
//    }
//	
//    @Override
//    public void cancelClick() {
//        sendCallback.accept(null);
//    }
//	
//    @Override
//    public boolean sentClick(String content) {
//        if (StringUtils.isBlank(content)) {
//            Toast.makeText(activity, "发送的内容不能为空", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//	
//        LoadingDialog.show(activity, "正在发送,请稍后...");
//        nearStandard.addComment(info, content, 0)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<NearCircleComment>() {
//                    @Override
//                    public void accept(NearCircleComment info) throws Exception {
//                        LoadingDialog.hide();
//                        sendCallback.run();
//                        nearCircleAdapter.addComment(info);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) {
//                        LoadingDialog.hide();
//                        Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//	
//        return true;
//    }
//}
