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

	   NearCircleService.getNearCircleInfos(minNearCircleId, pageCount, minNearCircleId<=0?this.getMatchInfos(localInfos):null)
                .map(new NetDefaultFunction<GetNearCircleInfosResponse, List<NearCircle>>() {
                    @Override
                    protected List<NearCircle> onSuccess(GetNearCircleInfosResponse response)  throws Exception {
						return DBNearCircleAction.convertToNearCircleInfos(response.getNearCircleInfos());
                    }
                })
                .doOnNext(new Consumer<List<NearCircle>>() {
                    @Override
                    public void accept(List<NearCircle> infos) throws Exception {
						boolean isClearLocalInfo=false;
						if(infos!=null&&infos.size()>=pageCount&&localInfos!=null&&localInfos.size()>0){
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
}
