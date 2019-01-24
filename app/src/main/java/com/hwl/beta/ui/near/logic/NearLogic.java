package com.hwl.beta.ui.near.logic;

import com.hwl.beta.db.entity.NearCircle;

import java.util.List;

public class NearLogic implements NearStandard{

   static int pageCount = 15;
   static int commentPageCount = 10;
   
   public void loadLocalInfos(final DefaultCallback<List<NearCircle>, String> callback){
		Observable.create(new ObservableOnSubscribe() {
			public void subscribe(ObservableEmitter emitter) throws Exception {
				List<NearCircle> infos = DaoUtils.getNearCircleManagerInstance().getNearCirclesV2(pageCount,commentPageCount);

				emitter.onNext(infos);
			}
		})
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
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

   public void loadServerInfos(final long minNearCircleId,
	   final List<NearCircle> localInfos,
	   final DefaultCallback<List<NearCircle>, String> callback){
	   NearCircleService.getNearCircleInfos(minNearCircleId, pageCount, null)
                .map(new NetDefaultFunction<GetNearCircleInfosResponse, List<NearCircle>>() {
                    @Override
                    protected List<NearCircle> onSuccess(GetNearCircleInfosResponse response)  throws Exception {
						return DBNearCircleAction.convertToNearCircleInfos(response.getNearCircleInfos());
                    }
                })
                .doOnNext(new Consumer<List<NearCircle>>() {
                    @Override
                    public void accept(List<NearCircle> infos) throws Exception {
                        //if (infos != null && infos.size() > 0)
						//{
						//    DaoUtils.getNearCircleManagerInstance().save(nearCircleExt.getInfo());
						//    DaoUtils.getNearCircleManagerInstance().deleteImages(nearCircleExt.getInfo().getNearCircleId());
						//    DaoUtils.getNearCircleManagerInstance().deleteComments(nearCircleExt.getInfo().getNearCircleId());
						//    DaoUtils.getNearCircleManagerInstance().deleteLikes(nearCircleExt.getInfo().getNearCircleId());
						//    DaoUtils.getNearCircleManagerInstance().saveImages(nearCircleExt.getInfo().getNearCircleId(), nearCircleExt.getImages());
						//    DaoUtils.getNearCircleManagerInstance().saveComments(nearCircleExt.getInfo().getNearCircleId(), nearCircleExt.getComments());
						//    DaoUtils.getNearCircleManagerInstance().saveLikes(nearCircleExt.getInfo().getNearCircleId(), nearCircleExt.getLikes());
						//}
                    }
                })
				.observeOn(AndroidSchedulers.mainThread());
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
}
