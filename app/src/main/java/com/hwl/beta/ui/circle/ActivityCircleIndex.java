package com.hwl.beta.ui.circle;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.CircleActivityIndexBinding;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.db.entity.CircleImage;
import com.hwl.beta.db.entity.CircleLike;
import com.hwl.beta.db.ext.CircleExt;
import com.hwl.beta.ui.circle.action.ICircleItemListener;
import com.hwl.beta.ui.circle.adp.CircleIndexAdapter;
import com.hwl.beta.ui.circle.logic.CircleLogic;
import com.hwl.beta.ui.circle.standard.CircleStandard;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.KeyBoardAction;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.dialog.LoadingDialog;
import com.hwl.beta.ui.imgselect.ActivityImageBrowse;
import com.hwl.beta.ui.imgselect.bean.ImageSelectType;
import com.hwl.beta.ui.widget.CircleActionMorePop;
import com.hwl.beta.utils.NetworkUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class ActivityCircleIndex extends BaseActivity {

    FragmentActivity activity;
    CircleActivityIndexBinding binding;
    CircleStandard circleStandard;
    CircleIndexAdapter circleAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        circleStandard = new CircleLogic();
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

    // DaoUtils.getCircleManagerInstance().updateCircleFriendList(circles, remark.getFriendId(),
    // new Function() {
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
                loadServerInfos(0);
            }
        });
        binding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                //loadCircleFromServer(circles.get(circles.size() - 1).getInfo().getCircleId());
            }
        });

        binding.refreshLayout.setEnableLoadMore(false);
        this.loadLocalInfos();
    }

    private void loadLocalInfos() {
        circleStandard.loadLocalInfos()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Circle>>() {
                    @Override
                    public void accept(List<Circle> infos) {
                        circleAdapter.updateInfos(infos);
//                        loadServerInfos(0);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadServerInfos(long infoId) {
        if (!NetworkUtils.isConnected()) {
            showResult();
            return;
        }

//        final boolean isRefresh = infoId == 0;
        circleStandard.loadServerInfos(infoId, circleAdapter.getInfos())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Circle>>() {
                    @Override
                    public void accept(List<Circle> infos) {
                        circleAdapter.updateInfos(infos);
                        showResult();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        showResult();
                        Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showResult() {
        if (circleAdapter.getItemCount() <= 0) {
            circleAdapter.setEmptyInfo();
            binding.refreshLayout.setEnableLoadMore(false);
        }
        binding.refreshLayout.finishRefresh();
        binding.refreshLayout.finishLoadMore();
    }

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
    // .flatMap(new Function<ResponseBase<UpResxResponse>,
    // Observable<ResponseBase<SetUserCircleBackImageResponse>>>() {
    // @Override
    // public Observable<ResponseBase<SetUserCircleBackImageResponse>> apply
    // (ResponseBase<UpResxResponse> response) throws Exception {
    // if (response != null && response.getResponseBody() != null && response.getResponseBody()
    // .isSuccess()) {
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

    private class CircleItemListener implements ICircleItemListener {

        private CircleActionMorePop mMorePopupWindow;
        boolean isRunning = false;

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
                    UITransfer.toImageSelectActivity(activity, ImageSelectType
                            .CIRCLE_BACK_IMAGE, 1);
                    circleBackImageDialog.dismiss();
                }
            });
            circleBackImageDialog.setContentView(root);
            circleBackImageDialog.show();
        }

        @Override
        public void onMyUserHeadClick() {
//            UITransfer.toCircleUserIndexActivity(activity, UserSP.getUserId(), UserSP
//            .getUserName(), UserSP.getUserHeadImage(), UserSP.getUserCirclebackimage(), UserSP
//            .getLifeNotes());
        }

        @Override
        public void onUserHeadClick(Circle info) {
            UITransfer.toUserIndexActivity(activity, info.getPublishUserId(),
                    info.getPublishUserName(), info.getPublishUserImage());
        }

        @Override
        public void onLikeUserHeadClick(CircleLike likeInfo) {
            UITransfer.toUserIndexActivity(activity, likeInfo.getLikeUserId(),
                    likeInfo.getLikeUserName(), likeInfo.getLikeUserImage());
        }

        @Override
        public void onCommentUserClick(CircleComment comment) {
            UITransfer.toUserIndexActivity(activity, comment.getCommentUserId(),
                    comment.getCommentUserName(), comment.getCommentUserImage());
        }

        @Override
        public void onReplyUserClick(CircleComment comment) {
            UITransfer.toUserIndexActivity(activity, comment.getReplyUserId(),
                    comment.getReplyUserName(), comment.getReplyUserImage());
        }

        @Override
        public void onCommentContentClick(CircleComment comment) {
//            CircleExt currnetInfo = this.getCircleInfo(comment.getCircleId());
//            if (currnetInfo == null) return;
//            if (comment.getCommentUserId() == myUserId) {
//                UITransfer.toCircleCommentPublishActivity(activity, comment.getCircleId(),
//                currnetInfo.getInfo().getPublishUserId(), currnetInfo.getCircleMessageContent());
//            } else {
//                UITransfer.toCircleCommentPublishActivity(activity, comment.getCircleId(),
//                currnetInfo.getInfo().getPublishUserId(), comment.getCommentUserId(), comment
//                .getCommentUserName(), currnetInfo.getCircleMessageContent());
//            }
        }

        @Override
        public void onContentClick() {

        }

        @Override
        public void onMoreActionClick(final View view, int position, final Circle info) {
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
//                    UITransfer.toCircleCommentPublishActivity(activity, info.getCircleId(),
//                    info.getPublishUserId(), info
//                    .getCircleMessageContent());
                }

                @Override
                public void onLikeClick(int position) {
                    setLike(position, info);
                }
            });
            mMorePopupWindow.show(position, view, info.getIsLiked());
        }

        private void setLike(final int position, Circle info) {
            if (isRunning) return;
            isRunning = true;

            final boolean isLike = !info.getIsLiked();
            circleStandard.setLike(info, isLike)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<CircleLike>() {
                        @Override
                        public void accept(CircleLike info) {
                            isRunning = false;
                            if (isLike)
                                circleAdapter.addLike(position, info);
                            else
                                circleAdapter.addLike(position, null);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            isRunning = false;
                            Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
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

        private void deleteCircle(final int position, long nearCircleId) {
            LoadingDialog.show(activity);
            circleStandard.deleteInfo(nearCircleId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) {
                            circleAdapter.remove(position);
                            LoadingDialog.hide();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            LoadingDialog.hide();
                            Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        @Override
        public void onPublishClick() {
            //UITransfer.toCirclePublishActivity(activity);
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
                UITransfer.toImageBrowseActivity(activity, ActivityImageBrowse.MODE_VIEW,
                        position, imageUrls);
            }
        }
    }
}
