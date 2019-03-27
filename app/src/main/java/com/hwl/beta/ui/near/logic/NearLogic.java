package com.hwl.beta.ui.near.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.near.NearCircleService;
import com.hwl.beta.net.near.NetNearCircleMatchInfo;
import com.hwl.beta.net.near.body.DeleteNearCircleInfoResponse;
import com.hwl.beta.net.near.body.GetNearCircleInfosResponse;
import com.hwl.beta.net.near.body.SetNearLikeInfoResponse;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.common.rxext.NetDefaultFunction;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.convert.DBNearCircleAction;
import com.hwl.beta.ui.near.standard.NearStandard;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NearLogic implements NearStandard {

    static int pageCount = 15;
    static int commentPageCount = 10;

    @Override
    public void loadLocalInfos(final DefaultCallback<List<NearCircle>, String> callback) {
        Observable.create(new ObservableOnSubscribe() {
            public void subscribe(ObservableEmitter emitter) throws Exception {
                List<NearCircle> infos = DaoUtils.getNearCircleManagerInstance().getNearCirclesV2(pageCount,
                        commentPageCount);

                emitter.onNext(infos);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<NearCircle>>() {
                    @Override
                    public void accept(List<NearCircle> infos) {
                        callback.success(infos);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        callback.error(throwable.getMessage());
                    }
                });
    }

    @Override
   public void loadServerInfos(final long minNearCircleId,
	   final List<NearCircle> localInfos,
	   final DefaultCallback<List<NearCircle>, String> callback){

//	   NearCircleService.getNearCircleInfos(minNearCircleId, pageCount, minNearCircleId<=0?this.getMatchInfos(localInfos):null)
//                .map(new Function<GetNearCircleInfosResponse, List<NearCircle>>() {
//                    @Override
//                    protected List<NearCircle> onSuccess(GetNearCircleInfosResponse response) {
//						return DBNearCircleAction.convertToNearCircleInfos(response.getNearCircleInfos());
//                    }
//                })
//                .doOnNext(new Consumer<List<NearCircle>>() {
//                    @Override
//                    public void accept(List<NearCircle> infos) {
//						boolean isClearLocalInfo=false;
//						if(infos!=null&&infos.size()>=pageCount&&localInfos!=null&&localInfos.size()>0){
//							isClearLocalInfo = (infos.get(infos.size()-1).getNearCircleId()-localInfos.get(0).getNearCircleId())>1;
//						}
//
//						if(isClearLocalInfo){
//							DaoUtils.getNearCircleManagerInstance().clearAll();
//						}
//
//                        for (int i = 0; i < infos.size(); i++) {
//							DaoUtils.getNearCircleManagerInstance().deleteAll(infos.get(i).getNearCircleId());
//							DaoUtils.getNearCircleManagerInstance().saveAll(infos);
//						}
//                    }
//                })
//				.observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<NearCircle>>() {
//                    @Override
//                    public void accept(List<NearCircle> infos) {
//                        callback.success(infos);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) {
//                        callback.error(throwable.getMessage());
//                    }
//                });
   }

   private List<NetNearCircleMatchInfo> getMatchInfos(List<NearCircle> localInfos){
   	   List<NetNearCircleMatchInfo> matchInfos = new ArrayList<>();
        int length = localInfos.size() > pageCount ? pageCount : localInfos.size();
        for (int i = 0; i < length; i++) {
            if (localInfos.get(i) != null && localInfos.get(i).getNearCircleId() > 0) {
                matchInfos.add(new NetNearCircleMatchInfo(localInfos.get(i).getNearCircleId(), localInfos.get(i).getUpdateTime()));
            }
        }
		return matchInfos;
   }

    @Override
	public void deleteInfo(final long nearCircleId,final DefaultCallback<Boolean, String> callback){
		NearCircleService.deleteNearCircleInfo(nearCircleId)
            .subscribe(new NetDefaultObserver<DeleteNearCircleInfoResponse>() {
                @Override
                protected void onSuccess(DeleteNearCircleInfoResponse response) {
                    if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
						DaoUtils.getNearCircleManagerInstance().deleteAll(nearCircleId);
                        callback.success(true);
                    } else {
                        onError("Delete near circle info failed.");
                    }
                }

                @Override
                protected void onError(String resultMessage) {
                    callback.error(resultMessage);
                }
            });
	}

    @Override
	public void setLike(final long nearCircleId,final boolean isLike,final DefaultCallback<Boolean, String> callback){
        NearCircleService.setNearLikeInfo(isLike ? 1 : 0, nearCircleId)
                .subscribe(new NetDefaultObserver<SetNearLikeInfoResponse>() {
                    @Override
                    protected void onSuccess(SetNearLikeInfoResponse response) {
                        if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
							callback.success(true);
                            if (isLike) {
                                //nearCircleAdapter.addLike(position, null);
                                //NearCircleMessageSend.sendDeleteLikeMessage(info.getInfo().getNearCircleId(), info.getInfo().getPublishUserId()).subscribe();
                            } else {
                                //NearCircleLike likeInfo = new NearCircleLike();
                                //likeInfo.setNearCircleId(info.getInfo().getNearCircleId());
                                //likeInfo.setLikeUserId(myUserId);
                                //likeInfo.setLikeUserName(UserSP.getUserName());
                                //likeInfo.setLikeUserImage(UserSP.getUserHeadImage());
                                //likeInfo.setLikeTime(new Date());
                                //nearCircleAdapter.addLike(position, likeInfo);
                                //NearCircleMessageSend.sendAddLikeMessage(info.getInfo().getNearCircleId(), info.getInfo().getPublishUserId(), info.getNearCircleMessageContent()).subscribe();
                            }
                        } else {
                            onError("Set user like info failed.");
                        }
                    }

                    @Override
                    protected void onError(String resultMessage) {
						callback.error(resultMessage);
                    }
                });
	}
}
