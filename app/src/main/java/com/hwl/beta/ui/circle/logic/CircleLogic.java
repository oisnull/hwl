package com.hwl.beta.ui.circle.logic;

import com.hwl.beta.db.DaoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CircleLogic implements CircleStandard {

    final static int PAGE_COUNT = 15;
    final static int COMMNET_PAGE_COUNT = 10;

    @Override
    public Observable<List<Circle>> loadLocalInfos() {
        return Observable.fromCallable(new Callable<List<Circle>>() {
            @Override
            public List<Circle> call() throws Exception {
                return DaoUtils.getCircleManagerInstance().getCirclesV2(PAGE_COUNT,COMMNET_PAGE_COUNT);
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<Circle>> loadServerInfos(final long minCircleId,
                                                        final List<Circle> localInfos) {
        // minCircleId <=0 and get new data
        // minCircleId >0 and get old data
        return CircleService.getCircleInfos(minCircleId, PAGE_COUNT,
                this.getMatchInfos(minCircleId, localInfos))
                .map(new Function<GetCircleInfosResponse, List<Circle>>() {
                    @Override
                    public List<Circle> apply(GetCircleInfosResponse response) {
                        List<Circle> infos =
                                DBCircleAction.convertToCircleInfos(response.getCircleInfos());
                        if (infos == null) infos = new ArrayList<>();
                        return infos;
                    }
                })
                .doOnNext(new Consumer<List<Circle>>() {
                    @Override
                    public void accept(List<Circle> infos) {
                        boolean isClearLocalInfo = false;
                        if (infos != null && infos.size() >= PAGE_COUNT && localInfos != null && localInfos.size() > 0) {
                            isClearLocalInfo =
                                    (infos.get(infos.size() - 1).getCircleId() - localInfos.get(0).getCircleId()) > 1;
                        }

                        if (isClearLocalInfo) {
                            DaoUtils.getCircleManagerInstance().clearAll();
                        }

                        for (int i = 0; i < infos.size(); i++) {
                            DaoUtils.getCircleManagerInstance().deleteAll(infos.get(i).getCircleId());
                            DaoUtils.getCircleManagerInstance().saveAll(infos);
                        }
                    }
                });
    }

    private List<NetCircleMatchInfo> getMatchInfos(long minCircleId,
                                                       List<Circle> localInfos) {
        List<NetCircleMatchInfo> matchInfos = new ArrayList<>();
        for (int i = 0; i < localInfos.size(); i++) {
            if (minCircleId <= 0) {
                if (localInfos.get(i).getCircleId() <= 0) continue;
            } else {
                if (localInfos.get(i).getCircleId() >= minCircleId) continue;
            }

            matchInfos.add(new NetCircleMatchInfo(localInfos.get(i).getCircleId(),
                    localInfos.get(i).getUpdateTime()));
            if (matchInfos.size() >= PAGE_COUNT) break;
        }
        return matchInfos;
    }

    @Override
    public Observable deleteInfo(final long CircleId) {
        return CircleService.deleteCircleInfo(CircleId)
                .map(new Function<DeleteCircleInfoResponse, Boolean>() {
                    @Override
                    public Boolean apply(DeleteCircleInfoResponse response) throws Exception {
                        if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
                            DaoUtils.getCircleManagerInstance().deleteAll(CircleId);
                        } else {
                            throw new Exception("Delete circle info failed.");
                        }
                        return true;
                    }
                });
    }

    @Override
    public Observable setLike(final long CircleId, final boolean isLike) {
        return CircleService.setLikeInfo(isLike ? 1 : 0, CircleId)
                .map(new Function<SetLikeInfoResponse, Boolean>() {
                    @Override
                    public Boolean apply(SetLikeInfoResponse response) throws Exception {
                        if (response.getStatus() != NetConstant.RESULT_SUCCESS) {
                            throw new Exception("Set user like info failed.");
                        }
                        return true;
                    }
                });
    }
}
