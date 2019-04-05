package com.hwl.beta.ui.near.logic;

import com.hwl.beta.net.near.NetImageInfo;
import com.hwl.beta.ui.near.standard.NearPublishStandard;

import java.util.List;

import io.reactivex.Observable;

public class NearPublishLogic implements NearPublishStandard {

    @Override
    public Observable publishImages(List<String> imgPaths) {
        return null;
    }

    @Override
    public Observable publishContent(String content, List<NetImageInfo> images) {
        return null;
    }
}
