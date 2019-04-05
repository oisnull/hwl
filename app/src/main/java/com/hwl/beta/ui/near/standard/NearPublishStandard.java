package com.hwl.beta.ui.near.standard;

import com.hwl.beta.net.near.NetImageInfo;

import java.util.List;

import io.reactivex.Observable;

public interface NearPublishStandard {

    Observable publishImages(List<String> imgPaths);

    Observable publishContent(String content, List<NetImageInfo> images);
}
