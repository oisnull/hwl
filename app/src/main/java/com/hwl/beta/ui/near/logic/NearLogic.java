package com.hwl.beta.ui.near.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.near.NearCircleService;
import com.hwl.beta.net.near.NetNearCircleMatchInfo;
import com.hwl.beta.net.near.body.DeleteNearCircleInfoResponse;
import com.hwl.beta.net.near.body.SetNearLikeInfoResponse;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.common.rxext.RXDefaultObserver;
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

   final static int PAGE_COUNT = 15;
   final static int COMMNET_PAGE_COUNT = 10;

    @Override
    public Observalbe<List<NearCircle>> loadLocalInfos() {
		return Observable.fromCallable(new Callable<List<NearCircle>>() {
            @Override
            public List<NearCircle> call() throws Exception {
                return DaoUtils.getNearCircleManagerInstance().getNearCirclesV2(PAGE_COUNT,COMMNET_PAGE_COUNT);
            }
        })
		.subscribeOn(Schedulers.io());
		// .observeOn(AndroidSchedulers.mainThread())
        // .subscribe(new Consumer<List<NearCircle>>() {
            // @Override
            // public void accept(List<NearCircle> infos) {
                // callback.success(infos); //https://github.com/ReactiveX/RxJava/wiki/Creating-Observables
            // }
        // }, new Consumer<Throwable>() {
            // @Override
            // public void accept(Throwable throwable) {
                // callback.error(throwable.getMessage());
            // }
        // });
    }

    @Override
   public Observalbe<List<NearCircle>> loadServerInfos(final long minNearCircleId,final List<NearCircle> localInfos){
		// minNearCircleId <=0 and get new datas
		// minNearCircleId >0 and get old data
	   return NearCircleService.getNearCircleInfos(minNearCircleId, PAGE_COUNT, this.getMatchInfos(minNearCircleId,localInfos))
               .map(new Function<GetNearCircleInfosResponse, List<NearCircle>>() {
                   @Override
                   protected List<NearCircle> apply(GetNearCircleInfosResponse response) {
						return DBNearCircleAction.convertToNearCircleInfos(response.getNearCircleInfos());
                   }
               })
               .doOnNext(new Consumer<List<NearCircle>>() {
                   @Override
                   public void accept(List<NearCircle> infos) {
						boolean isClearLocalInfo=false;
						if(infos!=null&&infos.size()>=PAGE_COUNT&&localInfos!=null&&localInfos.size()>0){
							isClearLocalInfo = (infos.get(infos.size()-1).getNearCircleId()-localInfos.get(0).getNearCircleId())>1;
						}

						if(isClearLocalInfo){
							DaoUtils.getNearCircleManagerInstance().clearAll();
						}

                       for (int i = 0; i < infos.size(); i++) {
							DaoUtils.getNearCircleManagerInstance().deleteAll(infos.get(i).getNearCircleId());
							DaoUtils.getNearCircleManagerInstance().saveAll(infos);
						}
                   }
               });
   }

   private List<NetNearCircleMatchInfo> getMatchInfos(long minNearCircleId,List<NearCircle> localInfos){
   	   List<NetNearCircleMatchInfo> matchInfos = new ArrayList<>();
	   for (int i = 0; i < localInfos.size(); i++) {
			if(minNearCircleId<=0){
				if (localInfos.get(i).getNearCircleId() <= 0) continue;
			}else{
				if (localInfos.get(i).getNearCircleId() >= minNearCircleId) continue;
			}

			matchInfos.add(new NetNearCircleMatchInfo(localInfos.get(i).getNearCircleId(), localInfos.get(i).getUpdateTime()));
			if(matchInfos.size()>=PAGE_COUNT) break;
        }

        // int length = localInfos.size() > PAGE_COUNT ? PAGE_COUNT : localInfos.size();
        // for (int i = 0; i < length; i++) {
            // if (localInfos.get(i) != null && localInfos.get(i).getNearCircleId() > 0) {
                // matchInfos.add(new NetNearCircleMatchInfo(localInfos.get(i).getNearCircleId(), localInfos.get(i).getUpdateTime()));
            // }
        // }
		return matchInfos;
   }

    @Override
	public Observable deleteInfo(final long nearCircleId,final DefaultCallback<Boolean, String> callback){
		return NearCircleService.deleteNearCircleInfo(nearCircleId)
               .map(new Function<DeleteNearCircleInfoResponse,Boolean>() throws Exception {
                   @Override
                   public Boolean apply(DeleteNearCircleInfoResponse response) {
						if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
							DaoUtils.getNearCircleManagerInstance().deleteAll(nearCircleId);
						} else {
							throw new Exception("Delete near circle info failed.");
						}
						return true;
                   }
               });
	}

    @Override
	public Observable setLike(final long nearCircleId,final boolean isLike,final DefaultCallback<Boolean, String> callback){
        NearCircleService.setNearLikeInfo(isLike ? 1 : 0, nearCircleId)
				.map(new Function<SetNearLikeInfoResponse,Boolean>() throws Exception {
                   @Override
                   public Boolean apply(SetNearLikeInfoResponse response) {
						if (response.getStatus() != NetConstant.RESULT_SUCCESS) {
							throw new Exception("Set user like info failed.");
						}
						return true;
                   }
               });
                // .subscribe(new RXDefaultObserver<SetNearLikeInfoResponse>() {
                    // @Override
                    // protected void onSuccess(SetNearLikeInfoResponse response) {
                        // if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
							// callback.success(true);
                            // if (isLike) {
                                nearCircleAdapter.addLike(position, null);
                                NearCircleMessageSend.sendDeleteLikeMessage(info.getInfo().getNearCircleId(), info.getInfo().getPublishUserId()).subscribe();
                            // } else {
                                NearCircleLike likeInfo = new NearCircleLike();
                                likeInfo.setNearCircleId(info.getInfo().getNearCircleId());
                                likeInfo.setLikeUserId(myUserId);
                                likeInfo.setLikeUserName(UserSP.getUserName());
                                likeInfo.setLikeUserImage(UserSP.getUserHeadImage());
                                likeInfo.setLikeTime(new Date());
                                nearCircleAdapter.addLike(position, likeInfo);
                                NearCircleMessageSend.sendAddLikeMessage(info.getInfo().getNearCircleId(), info.getInfo().getPublishUserId(), info.getNearCircleMessageContent()).subscribe();
                            // }
                        // } else {
                            // onError("Set user like info failed.");
                        // }
                    // }

                    // @Override
                    // protected void onError(String resultMessage) {
						// callback.error(resultMessage);
                    // }
                // });
	}
}
