//package com.hwl.beta.ui.widget;
//
//import android.content.Context;
//import android.graphics.drawable.BitmapDrawable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//
//import com.hwl.beta.R;
//import com.hwl.beta.autolayout.utils.AutoUtils;
//
///**
// * Created by Administrator on 2018/3/24.
// */
//
//public class CircleActionMorePop extends PopupWindow {
//    private int mShowMorePopupWindowWidth;
//    private int mShowMorePopupWindowHeight;
//    private LinearLayout llLikeConatiner;
//    private LinearLayout llCommentContainer;
//    private TextView tvLikeItem;
//    private ImageView ivLikeItem;
//
//    private IActionMoreListener actionMoreListener;
//
//    public void setActionMoreListener(IActionMoreListener actionMoreListener) {
//        this.actionMoreListener = actionMoreListener;
//    }
//
//    public CircleActionMorePop(Context context) {
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View conentView = inflater.inflate(R.layout.circle_action_more_pop, null);
//        setContentView(conentView);
//        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        setBackgroundDrawable(new BitmapDrawable());
//        setTouchable(true);
//        setOutsideTouchable(true);
//
//        conentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        mShowMorePopupWindowWidth = conentView.getMeasuredWidth();
//        mShowMorePopupWindowHeight = conentView.getMeasuredHeight();
//
//        View parent = getContentView();
//        llLikeConatiner = parent.findViewById(R.id.ll_like_container);
//        llCommentContainer = parent.findViewById(R.id.ll_comment_container);
//        tvLikeItem = parent.findViewById(R.id.tv_like_item);
//        ivLikeItem = parent.findViewById(R.id.iv_like_item);
//        parent.findViewById(R.id.ll_close_container).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//            }
//        });
//    }
//
//    public void show(int position, View moreActionView) {
//        show(position, moreActionView, false);
//    }
//
//    public void show(final int position, View moreActionView, boolean isLike) {
//        if (isShowing()) {
//            dismiss();
//        } else {
//            //已经点过赞
//            if (isLike) {
//                tvLikeItem.setText("取消");
//                ivLikeItem.setImageResource(R.drawable.ic_like);
//            } else {
//                tvLikeItem.setText("点赞");
//                ivLikeItem.setImageResource(R.drawable.ic_like_border);
//            }
//            //点赞按钮
//            llLikeConatiner.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    actionMoreListener.onLikeClick(position);
//                    dismiss();
//                }
//            });
//            //评论按钮
//            llCommentContainer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    actionMoreListener.onCommentClick(position);
//                    dismiss();
//                }
//            });
//
//            int heightMoreBtnView = moreActionView.getHeight();
//            int widthMoreBtnView = moreActionView.getWidth();
//            moreActionView.setVisibility(View.INVISIBLE);
//            showAsDropDown(moreActionView, AutoUtils.getPercentWidthSize(10) + widthMoreBtnView - mShowMorePopupWindowWidth,
//                    -(mShowMorePopupWindowHeight + heightMoreBtnView) / 2);
//        }
//    }
//
//    public interface IActionMoreListener {
//
////        void onDeleteClick(int position);
//
//        void onCommentClick(int position);
//
//        void onLikeClick(int position);
//    }
//}
