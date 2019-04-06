package com.hwl.beta.ui.near.standard;

import java.util.List;

import io.reactivex.Observable;

public interface NearPublishStandard {
    Observable publishInfo(String content, List<String> imagePaths);
}
