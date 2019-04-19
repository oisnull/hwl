package com.hwl.beta.ui.circle;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.CircleActivityIndexBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.db.entity.CircleImage;
import com.hwl.beta.db.entity.CircleLike;
import com.hwl.beta.db.ext.CircleExt;
import com.hwl.beta.mq.send.CircleMessageSend;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.ResponseBase;
import com.hwl.beta.net.circle.CircleService;
import com.hwl.beta.net.circle.NetCircleInfo;
import com.hwl.beta.net.circle.NetCircleMatchInfo;
import com.hwl.beta.net.circle.body.DeleteCircleInfoResponse;
import com.hwl.beta.net.circle.body.GetCircleInfosResponse;
import com.hwl.beta.net.circle.body.SetLikeInfoResponse;
import com.hwl.beta.net.resx.ResxType;
import com.hwl.beta.net.resx.UploadService;
import com.hwl.beta.net.resx.body.UpResxResponse;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.SetUserCircleBackImageResponse;
import com.hwl.beta.net.user.body.SetUserInfoResponse;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.busbean.EventActionCircleComment;
import com.hwl.beta.ui.busbean.EventActionCircleLike;
import com.hwl.beta.ui.busbean.EventBusConstant;
import com.hwl.beta.ui.busbean.EventUpdateFriendRemark;
import com.hwl.beta.ui.circle.action.ICircleItemListener;
import com.hwl.beta.ui.circle.adp.CircleIndexAdapter;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.KeyBoardAction;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.common.rxext.NetDefaultFunction;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.convert.DBCircleAction;
import com.hwl.beta.ui.dialog.LoadingDialog;
import com.hwl.beta.ui.imgselect.ActivityImageBrowse;
import com.hwl.beta.ui.imgselect.bean.ImageSelectType;
import com.hwl.beta.ui.user.bean.ImageViewBean;
import com.hwl.beta.ui.widget.CircleActionMorePop;
import com.hwl.beta.utils.NetworkUtils;
import com.hwl.beta.utils.StringUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ActivityCircleIndex extends BaseActivity {

    Activity activity;
    CircleActivityIndexBinding binding;
    CircleIndexAdapter circleAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        binding = DataBindingUtil.setContentView(activity, R.layout.circle_activity_index);
        initView();
    }

    // @Subscribe(threadMode = ThreadMode.MAIN)
    // public void refreshInfo(Integer ebType) {
        // if (ebType == EventBusConstant.EB_TYPE_CIRCLE_INFO_UPDATE) {
            // binding.refreshLayout.autoRefresh();
        // } else if (ebType == EventBusConstant.EB_TYPE_CIRCLE_MESSAGE_UPDATE) {
            // circleAdapter.notifyItemChanged(1);
        // }
    // }

    // @Subscribe(threadMode = ThreadMode.MAIN)
    // public void addComment(EventActionCircleComment action) {
        // if (action.getActionType() == EventBusConstant.EB_TYPE_ACTINO_ADD) {
            // circleAdapter.addComment(action.getComment());
        // } else if (action.getActionType() == EventBusConstant.EB_TYPE_ACTINO_REMOVE) {
            // circleAdapter.removeComment(action.getComment());
        // }
    // }

    // @Subscribe(threadMode = ThreadMode.MAIN)
    // public void addLike(EventActionCircleLike action) {
        // if (action.getActionType() == EventBusConstant.EB_TYPE_ACTINO_ADD) {
            // circleAdapter.addLike(action.getLike());
        // } else if (action.getActionType() == EventBusConstant.EB_TYPE_ACTINO_REMOVE) {
            // circleAdapter.removeLike(action.getLike());
        // }
    // }

    // @Subscribe(threadMode = ThreadMode.MAIN)
    // public void updateRemark(EventUpdateFriendRemark remark) {
        // if (remark == null || remark.getFriendId() <= 0)
            // return;

        // DaoUtils.getCircleManagerInstance().updateCircleFriendList(circles, remark.getFriendId(), new Function() {
            // @Override
            // public Object apply(Object o) throws Exception {
                // circleAdapter.notifyItemChanged((Integer) o);
                // return o;
            // }
        // });
    // }

    private void initView() {
        binding.tbTitle.setTitle("我的圈子")
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //UITransfer.toCirclePublishActivity(activity);
                    }
                })
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
				
        circleAdapter = new CircleIndexAdapter(activity, new CircleItemListener());
        binding.rvCircleContainer.setAdapter(circleAdapter);
        binding.rvCircleContainer.setLayoutManager(new LinearLayoutManager(activity));
        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //loadCircleFromServer(0);
            }
        });
        binding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                //loadCircleFromServer(circles.get(circles.size() - 1).getInfo().getCircleId());
            }
        });

        binding.refreshLayout.autoRefresh();
        binding.refreshLayout.setEnableLoadMore(false);
    }

    // private List<CircleExt> getCircles() {
        // List<CircleExt> infos = DaoUtils.getCircleManagerInstance().getCircles(pageCount);
        // if (infos == null) {
            // infos = new ArrayList<>();
        // }
        // if (infos.size() == 0) {
            // infos.add(new CircleExt(CircleExt.CircleNullItem));
        // }
        // infos.add(0, new CircleExt(CircleExt.CircleHeadItem));
        // infos.add(1, new CircleExt(CircleExt.CircleMsgcountItem));
        // return infos;
    // }

    // private void showResult() {
        // if (containerEmptyView()) {
            // binding.refreshLayout.setEnableLoadMore(false);
        // } else {
            // binding.refreshLayout.setEnableLoadMore(true);
        // }
        // binding.refreshLayout.finishRefresh();
        // binding.refreshLayout.finishLoadMore();
       binding.pbLoading.setVisibility(View.GONE);
    // }

    // private boolean containerEmptyView() {
        // for (int i = 0; i < circles.size(); i++) {
            // if (circles.get(i).getCircleItemType() == CircleExt.CircleNullItem) {
                // return true;
            // }
        // }
        // return false;
    // }

    // private void removeEmptyView() {
        // for (int i = 0; i < circles.size(); i++) {
            // if (circles.get(i).getCircleItemType() == CircleExt.CircleNullItem) {
                // circles.remove(i);
                // break;
            // }
        // }
    // }

    // @Override
    // protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        // if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            // String localPath = data.getStringExtra("localpath");
            // if (StringUtils.isBlank(localPath)) {
                // Toast.makeText(activity, "上传数据不能为空", Toast.LENGTH_SHORT).show();
                // return;
            // }
            // LoadingDialog.show(activity, "正在上传...");
            // UploadService.upImage(new File(localPath), ResxType.CIRCLEBACK)
                    // .flatMap(new Function<ResponseBase<UpResxResponse>, Observable<ResponseBase<SetUserCircleBackImageResponse>>>() {
                        // @Override
                        // public Observable<ResponseBase<SetUserCircleBackImageResponse>> apply(ResponseBase<UpResxResponse> response) throws Exception {
                            // if (response != null && response.getResponseBody() != null && response.getResponseBody().isSuccess()) {
                                // return UserService.setUserCircleBackImage(response.getResponseBody().getOriginalUrl());
                            // } else
                                // throw new Exception("圈子封面上传失败");
                        // }
                    // })
                    // .observeOn(AndroidSchedulers.mainThread())
                    // .subscribe(new NetDefaultObserver<SetUserCircleBackImageResponse>() {
                        // @Override
                        // protected void onSuccess(SetUserCircleBackImageResponse response) {
                            // LoadingDialog.hide();
                            // if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
                                // UserSP.setUserCirclebackimage(response.getCircleBackImageUrl());
                                // circleAdapter.notifyItemChanged(0);
                                // Toast.makeText(activity, "圈子封面上传成功", Toast.LENGTH_SHORT).show();
                            // } else {
                                // onError("圈子封面上传失败");
                            // }
                        // }

                        // @Override
                        // protected void onError(String resultMessage) {
                            // super.onError(resultMessage);
                            // LoadingDialog.hide();
                        // }
                    // });
        // }
    // }

    /*
     * 首先获取本地已经存在的第一页的数据id和updatetime
     * 根据id和updatetime去服务器请求数据，如果服务器端获取的数据中有存在的，就匹配是否需要更新，如果要更新则返回到客户端，否则返回null
     * 客户端同样也要再次进行判断，如果更新时间不匹配，则进行存储，否则不存储到本地
     * */
    // private void loadCircleFromServer(final long minCircleId) {
        // if (!NetworkUtils.isConnected()) {
            // showResult();
            // return;
        // }

        // List<NetCircleMatchInfo> circleMatchInfos = new ArrayList<>();
        // if (minCircleId <= 0) {
            // int length = circles.size() > pageCount ? pageCount : circles.size();
            // for (int i = 0; i < length; i++) {
                // if (circles.get(i).getInfo() != null && circles.get(i).getInfo().getCircleId() > 0) {
                    // circleMatchInfos.add(new NetCircleMatchInfo(circles.get(i).getInfo().getCircleId(), circles.get(i).getInfo().getUpdateTime()));
                // }
            // }
        // }

        // CircleService.getCircleInfos(minCircleId, pageCount, circleMatchInfos)
                // .flatMap(new NetDefaultFunction<GetCircleInfosResponse, NetCircleInfo>() {
                    // @Override
                    // protected ObservableSource<NetCircleInfo> onSuccess(GetCircleInfosResponse response) {
                        // if (response.getCircleInfos() != null && response.getCircleInfos().size() > 0) {
                            // removeEmptyView();
                            // return Observable.fromIterable(response.getCircleInfos());
                        // }
                        // return Observable.fromIterable(new ArrayList<NetCircleInfo>());
                    // }
                // })
                // .observeOn(AndroidSchedulers.mainThread())
                // .map(new Function<NetCircleInfo, CircleExt>() {
                    // @Override
                    // public CircleExt apply(NetCircleInfo info) {
                        // CircleExt circleBean = new CircleExt(CircleExt.CircleIndexItem);
                        // if (info != null && info.getCircleId() > 0) {
                            // circleBean.setInfo(DBCircleAction.convertToCircleInfo(info));
                            // circleBean.setImages(DBCircleAction.convertToCircleImageInfos(info.getCircleId(), info.getPublishUserId(), info.getImages()));
                            // circleBean.setComments(DBCircleAction.convertToCircleCommentInfos(info.getCommentInfos()));
                            // circleBean.setLikes(DBCircleAction.convertToCircleLikeInfos(info.getLikeInfos()));
                            // return circleBean;
                        // }
                        // return circleBean;
                    // }
                // })
                // .doOnNext(new Consumer<CircleExt>() {
                    // @Override
                    // public void accept(CircleExt circleExt) {
                        // if (circleExt != null && circleExt.getInfo() != null) {
                            // DaoUtils.getCircleManagerInstance().save(circleExt.getInfo());
                            // DaoUtils.getCircleManagerInstance().deleteImages(circleExt.getInfo().getCircleId());
                            // DaoUtils.getCircleManagerInstance().deleteComments(circleExt.getInfo().getCircleId());
                            // DaoUtils.getCircleManagerInstance().deleteLikes(circleExt.getInfo().getCircleId());
                            // DaoUtils.getCircleManagerInstance().saveImages(circleExt.getInfo().getCircleId(), circleExt.getImages());
                            // DaoUtils.getCircleManagerInstance().saveComments(circleExt.getInfo().getCircleId(), circleExt.getComments());
                            // DaoUtils.getCircleManagerInstance().saveLikes(circleExt.getInfo().getCircleId(), circleExt.getLikes());
                        // }
                    // }
                // })
                // .subscribe(new Observer<CircleExt>() {

                    // int updateCount = 0;
                    // int insertCount = 1;

                    // @Override
                    // public void onSubscribe(Disposable d) {

                    // }

                    // @Override
                    // public void onNext(CircleExt circleExt) {
                        // if (circleExt != null && circleExt.getInfo() != null) {
                            // boolean isExists = false;
                            // for (int i = 0; i < circles.size(); i++) {
                                // if (circles.get(i).getCircleItemType() == CircleExt.CircleIndexItem &&
                                        // circles.get(i).getInfo().getCircleId() == circleExt.getInfo().getCircleId()) {
                                    // circles.remove(i);
                                    // circles.add(i, circleExt);
                                    // updateCount++;
                                    // isExists = true;
                                    // break;
                                // }
                            // }
                            // if (!isExists) {
                                // if (minCircleId > 0) {
                                    // circles.add(circleExt);
                                // } else {
                                    // circles.add(insertCount, circleExt);
                                // }
                                // updateCount++;
                                // insertCount++;
                            // }
                        // }
                    // }

                    // @Override
                    // public void onError(Throwable e) {
                       //Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                        // showResult();
                        // if (e != null && e.getMessage().equals(NetConstant.RESPONSE_RELOGIN)) {
                            // UITransfer.toReloginDialog((FragmentActivity) activity);
                        // }
                    // }

                    // @Override
                    // public void onComplete() {
                        // showResult();
                        // if (updateCount > 0) {
                            // circleAdapter.notifyItemRangeChanged(0, circles.size());
                        // }
                    // }
                // });
    // }

    private class CircleItemListener implements ICircleItemListener {

        private CircleActionMorePop mMorePopupWindow;
        boolean isRuning = false;

        private CircleExt getCircleInfo(long circleId) {
            if (circleId <= 0) return null;
            for (int i = 0; i < circles.size(); i++) {
                if (circles.get(i).getInfo() != null && circles.get(i).getInfo().getCircleId() > 0 && circles.get(i).getInfo().getCircleId() == circleId) {
                    return circles.get(i);
                }
            }
            return null;
        }

        @Override
        public void onItemViewClick(View view) {
            KeyBoardAction.hideSoftInput(view);
        }

        @Override
        public void onCircleBackImageClick() {
            final Dialog circleBackImageDialog = new Dialog(activity, R.style.BottomDialog);
            LinearLayout root = (LinearLayout) LayoutInflater.from(activity).inflate(
                    R.layout.circle_back_image_action_dialog, null);
            root.findViewById(R.id.btn_change).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UITransfer.toImageSelectActivity(activity, ImageSelectType.CIRCLE_BACK_IMAGE, 1);
                    circleBackImageDialog.dismiss();
                }
            });
            circleBackImageDialog.setContentView(root);
            circleBackImageDialog.show();
        }

        @Override
        public void onMyUserHeadClick() {
            UITransfer.toCircleUserIndexActivity(activity, UserSP.getUserId(), UserSP.getUserName(), UserSP.getUserHeadImage(), UserSP.getUserCirclebackimage(), UserSP.getLifeNotes());
        }

        @Override
        public void onUserHeadClick(Circle info) {
            UITransfer.toUserIndexActivity(activity, info.getPublishUserId(), info.getPublishUserName(), info.getPublishUserImage());
        }

        @Override
        public void onLikeUserHeadClick(CircleLike likeInfo) {
            UITransfer.toUserIndexActivity(activity, likeInfo.getLikeUserId(), likeInfo.getLikeUserName(), likeInfo.getLikeUserImage());
        }

        @Override
        public void onCommentUserClick(CircleComment comment) {
            UITransfer.toUserIndexActivity(activity, comment.getCommentUserId(), comment.getCommentUserName(), comment.getCommentUserImage());
        }

        @Override
        public void onReplyUserClick(CircleComment comment) {
            UITransfer.toUserIndexActivity(activity, comment.getReplyUserId(), comment.getReplyUserName(), comment.getReplyUserImage());
        }

        @Override
        public void onCommentContentClick(CircleComment comment) {
            CircleExt currnetInfo = this.getCircleInfo(comment.getCircleId());
            if (currnetInfo == null) return;
            if (comment.getCommentUserId() == myUserId) {
                UITransfer.toCircleCommentPublishActivity(activity, comment.getCircleId(), currnetInfo.getInfo().getPublishUserId(), currnetInfo.getCircleMessageContent());
            } else {
                UITransfer.toCircleCommentPublishActivity(activity, comment.getCircleId(), currnetInfo.getInfo().getPublishUserId(), comment.getCommentUserId(), comment.getCommentUserName(), currnetInfo.getCircleMessageContent());
            }
        }

        @Override
        public void onContentClick() {

        }

        @Override
        public void onMoreActionClick(final View view, int position) {
            final CircleExt info = circles.get(position);
            if (info == null) return;
            if (mMorePopupWindow == null) {
                mMorePopupWindow = new CircleActionMorePop(activity);
            }
            mMorePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    view.setVisibility(View.VISIBLE);
                }
            });
            mMorePopupWindow.setActionMoreListener(new CircleActionMorePop.IActionMoreListener() {
                @Override
                public void onCommentClick(int position) {
                    UITransfer.toCircleCommentPublishActivity(activity, info.getInfo().getCircleId(), info.getInfo().getPublishUserId(), info.getCircleMessageContent());
                }

                @Override
                public void onLikeClick(int position) {
                    setLikeInfo(position, info);
                }
            });
            mMorePopupWindow.show(position, view, info.getInfo().getIsLiked());
        }

        private void setLikeInfo(final int position, final CircleExt info) {
            if (isRuning || info == null || info.getInfo() == null || info.getInfo().getCircleId() <= 0)
                return;
            isRuning = true;
            final boolean isLiked = info.getInfo().getIsLiked();
            CircleService.setLikeInfo(isLiked ? 0 : 1, info.getInfo().getCircleId())
                    .subscribe(new NetDefaultObserver<SetLikeInfoResponse>() {
                        @Override
                        protected void onSuccess(SetLikeInfoResponse response) {
                            isRuning = false;
                            if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
                                if (isLiked) {
                                    circleAdapter.addLike(position, null);
                                    CircleMessageSend.sendDeleteLikeMessage(info.getInfo().getCircleId(), info.getInfo().getPublishUserId()).subscribe();
                                } else {
                                    CircleLike likeInfo = new CircleLike();
                                    likeInfo.setCircleId(info.getInfo().getCircleId());
                                    likeInfo.setLikeUserId(myUserId);
                                    likeInfo.setLikeUserName(UserSP.getUserName());
                                    likeInfo.setLikeUserImage(UserSP.getUserHeadImage());
                                    likeInfo.setLikeTime(new Date());
                                    circleAdapter.addLike(position, likeInfo);
                                    CircleMessageSend.sendAddLikeMessage(info.getInfo().getCircleId(), info.getInfo().getPublishUserId(), info.getCircleMessageContent()).subscribe();
                                }
                            } else {
                                onError("操作失败");
                            }
                        }

                        @Override
                        protected void onError(String resultMessage) {
                            super.onError(resultMessage);
                            isRuning = false;
                        }
                    });
        }

        @Override
        public void onMoreCommentClick() {

        }

        @Override
        public void onDeleteClick(final int position, final Circle info) {
            new AlertDialog.Builder(activity)
                    .setMessage("信息删除后,不能恢复,确认删除 ?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteCircle(position, info.getCircleId());
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }

        private void deleteCircle(final int position, final long circleId) {
            if (isRuning) return;
            isRuning = true;
            LoadingDialog.show(activity);
            CircleService.deleteCircleInfo(circleId)
                    .subscribe(new NetDefaultObserver<DeleteCircleInfoResponse>() {
                        @Override
                        protected void onSuccess(DeleteCircleInfoResponse response) {
                            if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
                                DaoUtils.getCircleManagerInstance().delete(circleId);
                                DaoUtils.getCircleManagerInstance().deleteImages(circleId);
                                DaoUtils.getCircleManagerInstance().deleteComments(circleId);
                                DaoUtils.getCircleManagerInstance().deleteLikes(circleId);
                                circleAdapter.remove(position);
                                LoadingDialog.hide();
                                isRuning = false;
                            } else {
                                onError("删除失败");
                            }
                        }

                        @Override
                        protected void onError(String resultMessage) {
                            super.onError(resultMessage);
                            isRuning = false;
                            LoadingDialog.hide();
                        }
                    });
        }

        @Override
        public void onPublishClick() {
            UITransfer.toCirclePublishActivity(activity);
        }

        @Override
        public void onMsgcountClick() {
            UITransfer.toCircleMessagesActivity(activity);
        }

        @Override
        public void onImageClick(int position, List<CircleImage> images) {
            if (images != null && images.size() > 0) {
                List<String> imageUrls = new ArrayList<>(images.size());
                for (int i = 0; i < images.size(); i++) {
                    imageUrls.add(images.get(i).getImageUrl());
                }
                UITransfer.toImageBrowseActivity(activity, ActivityImageBrowse.MODE_VIEW, position, imageUrls);
            }
        }
    }
}
